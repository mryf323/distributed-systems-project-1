package node.state_reactor;

import node.DirectedEdge;
import node.state_reactor.event.InBranchChange;
import node.state_reactor.event.LNChange;
import node.state_reactor.event.SNChange;

public class NodeStateHolder {


    private final Reactor reactor;

    public NodeStateHolder(Reactor reactor) {
        this.reactor = reactor;
    }


    public static enum NodeState {
        SLEEPING,
        FIND,
        FOUND;
    }


    private long findCount;
    private long FN;
    private DirectedEdge bestEdge;
    private long bestWt;
    private DirectedEdge inBranch;
    private NodeState SN = NodeState.SLEEPING; //node state
    private long LN; // level number
    private DirectedEdge testEdge;


    public long getFindCount() {
        return findCount;
    }

    public void setFindCount(long findCount) {
        this.findCount = findCount;
    }

    public long getFN() {
        return FN;
    }

    public void setFN(long FN) {
        this.FN = FN;
    }

    public DirectedEdge getBestEdge() {
        return bestEdge;
    }

    public void setBestEdge(DirectedEdge bestEdge) {
        this.bestEdge = bestEdge;
    }

    public long getBestWt() {
        return bestWt;
    }

    public void setBestWt(long bestWt) {
        this.bestWt = bestWt;
    }

    public DirectedEdge getInBranch() {
        return inBranch;
    }

    public void setInBranch(DirectedEdge inBranch) {
        this.inBranch = inBranch;
        reactor.afterStateChanged(new InBranchChange());
    }

    public NodeState getSN() {
        return SN;
    }

    public void setSN(NodeState SN) {
        this.SN = SN;
        reactor.afterStateChanged(new SNChange());
    }

    public long getLN() {
        return LN;
    }

    public void setLN(long LN) {
        this.LN = LN;
        reactor.afterStateChanged(new LNChange());
    }

    public DirectedEdge getTestEdge() {
        return testEdge;
    }

    public void setTestEdge(DirectedEdge testEdge) {
        this.testEdge = testEdge;
    }

    public void incFindCount() {
        this.findCount++;
    }

    public void decFindCount() {
        this.findCount--;
    }

}
