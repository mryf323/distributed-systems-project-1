package node;

import file_io.MstWriter;
import message.InitMessage;
import file_io.GraphParser;
import port.EdgePort;
import se.sics.kompics.Channel;
import se.sics.kompics.Component;
import se.sics.kompics.ComponentDefinition;
import se.sics.kompics.Kompics;
import topology.Graph;
import topology.UndirectedEdge;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class App extends ComponentDefinition {

    private Map<String, Component> components = new HashMap<>();

    public App() throws FileNotFoundException {
        createTopology();
    }

    public static void main(String[] args) throws InterruptedException, IOException {
        Kompics.createAndStart(App.class);
        Kompics.waitForTermination();
        Set<UndirectedEdge> inBoundBranches = InBoundBranchAggregator.getInstance().getInBoundBranches();
        MstWriter.getInstance().writeMst(inBoundBranches);
    }

    private void createTopology() throws FileNotFoundException {
        Graph graph = GraphParser.getInstance().parseFile(new File("tables.txt"));
        for (String node : graph.nodes) {
            Component component = create(Node.class, new InitMessage(node,
                    graph.getNeighbours(node))
            );
            components.put(node, component);
        }
        for (UndirectedEdge edge : graph.graphEdges) {
            connect(components.get(edge.src).getPositive(EdgePort.class),
                    components.get(edge.dst).getNegative(EdgePort.class),
                    Channel.TWO_WAY);
            connect(components.get(edge.src).getNegative(EdgePort.class),
                    components.get(edge.dst).getPositive(EdgePort.class),
                    Channel.TWO_WAY);
        }
    }
}
