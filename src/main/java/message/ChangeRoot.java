package message;

import node.DirectedEdge;

public class ChangeRoot extends BaseMessage {

    public ChangeRoot(DirectedEdge edge) {
        super(edge);
    }

    @Override
    public String toString() {
        return "ChangeRoot{" +
                "edge=" + edge +
                '}';
    }
}
