package node;

import message.*;
import node.state_reactor.NodeStateHolder;
import node.state_reactor.Reactor;
import node.state_reactor.event.InBranchChange;
import node.state_reactor.event.LNChange;
import node.state_reactor.event.SEChange;
import node.state_reactor.event.SNChange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import port.EdgePort;
import se.sics.kompics.*;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import node.state_reactor.NodeStateHolder.NodeState;
import topology.UndirectedEdge;

public class Node extends ComponentDefinition {

    private static Logger logger = LoggerFactory.getLogger(Node.class);

    private final Reactor reactor = new Reactor();
    private final NodeStateHolder state = new NodeStateHolder(reactor);

    private final String nodeName;
    private final Positive<EdgePort> receive = positive(EdgePort.class);
    private final Negative<EdgePort> send = negative(EdgePort.class);

    private final Set<DirectedEdge> neighbours;


    private DirectedEdge findRelevantNeighbour(BaseMessage message) {
        return neighbours.stream().filter(e -> e.dst.equals(message.edge.src))
                .findAny().orElseThrow(() -> new RuntimeException("Problem in topology"));
    }

    public Node(InitMessage initMessage) {
        nodeName = initMessage.nodeName;
        this.neighbours = initMessage.neighbours
                .stream().map(e -> new DirectedEdge(e.src, e.dst, e.w, reactor)).collect(Collectors.toSet());
        subscribe(startHandler, control);
        subscribe(stopHandler, control);
        subscribe(connectHandler, receive);
        subscribe(initiateHandler, receive);
        subscribe(testHandler, receive);
        subscribe(acceptHandler, receive);
        subscribe(rejectHandler, receive);
        subscribe(reportHandler, receive);
        subscribe(changeRootHandler, receive);
    }

    /*1*/ private Handler<Start> startHandler = new Handler<Start>() {
        @Override
        public void handle(Start event) {
            if (state.getSN() == NodeState.SLEEPING)
                wakeup();
            reactor.react();
        }
    };

    /*2*/ private void wakeup() {
        DirectedEdge m = neighbours.stream()
                .min(Comparator.comparing(e -> e.w))
                .orElseThrow(() -> new RuntimeException("Nodes must not be isolated."));

        m.setSE(DirectedEdge.State.BRANCH);
        state.setLN(0);
        state.setSN(NodeState.FOUND);
        state.setFindCount(0);
        trigger(new Connect(0, m), send);
    }

    /*3*/ private Handler<Connect> connectHandler = new Handler<Connect>() {
        @Override
        public void handle(Connect connect) {
            if (!connect.edge.dst.equals(nodeName))
                return;
            logger.info(" Node {} received {}", nodeName, connect);
            DirectedEdge j = findRelevantNeighbour(connect);

            if (state.getSN() == NodeState.SLEEPING)
                wakeup();
            if (connect.L < state.getLN()) {
                j.setSE(DirectedEdge.State.BRANCH);
                trigger(new Initiate(state.getLN(), state.getFN(), state.getSN(), j), send);
                if (state.getSN() == NodeState.FIND)
                    state.incFindCount();
            } else if (j.getSE() == DirectedEdge.State.BASIC)
                reactor.queue(connect, connectHandler, new SEChange(j), new LNChange());

            else
                trigger(new Initiate(state.getLN() + 1, j.w, NodeState.FIND, j), send);
            reactor.react();
        }
    };

    /*4*/ private Handler<Initiate> initiateHandler = new Handler<Initiate>() {
        @Override
        public void handle(Initiate initiate) {
            if (!initiate.edge.dst.equals(nodeName))
                return;
            logger.info("Node {} received {}", nodeName, initiate);
            DirectedEdge j = findRelevantNeighbour(initiate);

            state.setLN(initiate.L); state.setFN(initiate.F); state.setSN(initiate.S); state.setInBranch(j);
            state.setBestEdge(null); state.setBestWt(Long.MAX_VALUE);
            List<DirectedEdge> branchNeighbours = neighbours
                    .stream().filter(e -> e.getSE() == DirectedEdge.State.BRANCH && !e.equals(j))
                    .collect(Collectors.toList());
            for (DirectedEdge i : branchNeighbours) {
                trigger(new Initiate(initiate.L, initiate.F, initiate.S, i), send);
                if (initiate.S == NodeState.FIND)
                    state.incFindCount();
            }

            if (initiate.S == NodeState.FIND)
                test();
            reactor.react();
        }

    };

    /*5*/ private void test() {

        Optional<DirectedEdge> min = neighbours
                .stream().filter(n -> n.getSE() == DirectedEdge.State.BASIC)
                .min(Comparator.comparing(n -> n.w));

        if (min.isPresent()) {
            state.setTestEdge(min.get());
            trigger(new Test(state.getLN(), state.getFN(), state.getTestEdge()), send);
        } else {
            state.setTestEdge(null);
            report();
        }
    }

    /*6*/ private Handler<Test> testHandler = new Handler<Test>() {
        @Override
        public void handle(Test test) {
            if (!test.edge.dst.equals(nodeName))
                return;
            logger.info("Node {} received {}", nodeName, test);
            DirectedEdge j = findRelevantNeighbour(test);

            if (state.getSN() == NodeState.SLEEPING) wakeup();

            if (test.L > state.getLN())
                reactor.queue(test, testHandler, new LNChange());
            else if (test.F != state.getFN())
                trigger(new Accept(j), send);
            else {
                if (j.getSE() == DirectedEdge.State.BASIC)
                    j.setSE(DirectedEdge.State.REJECTED);
                if (!state.getTestEdge().equals(j))
                    trigger(new Reject(j), send);
                else
                    test();
            }
            reactor.react();
        }
    };

    /*7*/ private Handler<Accept> acceptHandler = new Handler<Accept>() {
        @Override
        public void handle(Accept accept) {
            if (!accept.edge.dst.equals(nodeName))
                return;
            logger.info("Node {} received {}", nodeName, accept);
            DirectedEdge j = findRelevantNeighbour(accept);

            state.setTestEdge(null);
            if (j.w < state.getBestWt()) {
                state.setBestEdge(j);
                state.setBestWt(j.w);
            }
            report();
            reactor.react();
        }
    };

    /*8*/ Handler<Reject> rejectHandler = new Handler<Reject>() {
        @Override
        public void handle(Reject accept) {
            if (!accept.edge.dst.equals(nodeName))
                return;
            logger.info("Node {} received {}", nodeName, accept);
            DirectedEdge j = findRelevantNeighbour(accept);

            if (j.getSE() == DirectedEdge.State.BASIC)
                j.setSE(DirectedEdge.State.REJECTED);
            test();
            reactor.react();
        }
    };

    /*9*/ private void report() {
        if (state.getFindCount() == 0 && state.getTestEdge() == null) {
            state.setSN(NodeState.FOUND);
            trigger(new Report(state.getBestWt(), state.getInBranch()), send);
        }
    }

    /*10*/ private Handler<Report> reportHandler = new Handler<Report>() {
        @Override
        public void handle(Report report) {
            if (!report.edge.dst.equals(nodeName))
                return;
            logger.info("Node {} received {}", nodeName, report);
            DirectedEdge j = findRelevantNeighbour(report);

            if (!j.equals(state.getInBranch())) {
                state.decFindCount();
                if (report.w < state.getBestWt()) {
                    state.setBestWt(report.w);
                    state.setBestEdge(j);
                }
                report();
            } else if (state.getSN() == NodeState.FIND)
                reactor.queue(report, reportHandler, new InBranchChange(), new SNChange());
            else if (report.w > state.getBestWt())
                changeRoot();
            else if (report.w == state.getBestWt() && report.w == Long.MAX_VALUE) {
                logger.info("Node {} halted", nodeName);
                Kompics.asyncShutdown();
            }
            reactor.react();
        }
    };

    /*11*/ private void changeRoot() {
        if (state.getBestEdge().getSE() == DirectedEdge.State.BRANCH)
            trigger(new ChangeRoot(state.getBestEdge()), send);
        else {
            trigger(new Connect(state.getLN(), state.getBestEdge()), send);
            state.getBestEdge().setSE(DirectedEdge.State.BRANCH);
        }
    }

    /*12*/ private Handler<ChangeRoot> changeRootHandler = new Handler<ChangeRoot>() {
        @Override
        public void handle(ChangeRoot changeRoot) {
            if (!changeRoot.edge.dst.equals(nodeName))
                return;
            logger.info("Node {} received {}", nodeName, changeRoot);
            changeRoot();
            reactor.react();
        }
    };
    private Handler<Kill> stopHandler = new Handler<Kill>() {
        @Override
        public void handle(Kill event) {
            logger.info("Node {} stopped", nodeName);
            DirectedEdge inBranch = state.getInBranch();
            InBoundBranchAggregator.getInstance().add(new UndirectedEdge(inBranch.src, inBranch.dst, inBranch.w));
        }
    };

}
