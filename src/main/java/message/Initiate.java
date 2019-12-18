package message;

import node.DirectedEdge;
import node.state_reactor.NodeStateHolder;

public class Initiate extends BaseMessage {

    public final long L;
    public final long F;
    public final NodeStateHolder.NodeState S;

    public Initiate(long L, long F, NodeStateHolder.NodeState S, DirectedEdge edge) {
        super(edge);
        this.L = L;
        this.F = F;
        this.S = S;
    }

    @Override
    public String toString() {
        return "Initiate{" +
                "level=" + L +
                ", fragmentName='" + F + '\'' +
                ", nodeState=" + S +
                ", edge=" + edge +
                '}';
    }
}
