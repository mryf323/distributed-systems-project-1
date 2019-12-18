package message;

import node.DirectedEdge;

public class Connect extends BaseMessage {

    public final long L;

    public Connect(long L, DirectedEdge edge) {
        super(edge);
        this.L = L;
    }

    @Override
    public String toString() {
        return "Connect{" +
                "level=" + L +
                ", edge=" + edge +
                '}';
    }
}
