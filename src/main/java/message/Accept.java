package message;

import node.DirectedEdge;

public class Accept extends BaseMessage {
    public Accept(DirectedEdge edge) {
        super(edge);
    }

    @Override
    public String toString() {
        return "Accept{" +
                "edge=" + edge +
                '}';
    }
}
