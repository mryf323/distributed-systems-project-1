package message;

import node.DirectedEdge;

public class Test extends BaseMessage {

    public long L;
    public long F;

    public Test(DirectedEdge edge) {
        super(edge);
    }

    public Test(long l, long f, DirectedEdge edge) {
        super(edge);
        this.L = l;
        this.F = f;
    }

    @Override
    public String toString() {
        return "Test{" +
                "L=" + L +
                ", F=" + F +
                ", edge=" + edge +
                '}';
    }
}
