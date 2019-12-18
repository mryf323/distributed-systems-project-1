package message;

import node.DirectedEdge;

public class Reject extends BaseMessage{
    public Reject(DirectedEdge j) {
        super(j);
    }

    @Override
    public String toString() {
        return "Reject{" +
                "edge=" + edge +
                '}';
    }
}
