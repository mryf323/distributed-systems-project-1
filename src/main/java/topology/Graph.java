package topology;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Graph {

    public Set<String> nodes;
    public Set<UndirectedEdge> graphEdges;

    public Graph(Set<String> nodes, Set<UndirectedEdge> graphEdges) {
        this.nodes = nodes;
        this.graphEdges = graphEdges;
    }
    public Set<UndirectedEdge> getNeighbours(String node){
        return Stream.concat(graphEdges.stream().filter(e -> e.src.equals(node)),
                graphEdges.stream().filter(e -> e.dst.equals(node)).map(e -> new UndirectedEdge(node, e.src, e.w))
        ).collect(Collectors.toSet());
    }


}
