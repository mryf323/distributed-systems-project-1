package message;

import node.Node;
import se.sics.kompics.Init;
import topology.UndirectedEdge;

import java.util.Set;

public class InitMessage extends Init<Node> {

    public String nodeName;
    public Set<UndirectedEdge> neighbours;

    public InitMessage(String nodeName, Set<UndirectedEdge> neighbours) {
        this.nodeName = nodeName;
        this.neighbours = neighbours;
    }
}
