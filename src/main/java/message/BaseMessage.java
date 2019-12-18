package message;

import node.DirectedEdge;
import se.sics.kompics.KompicsEvent;

public abstract class BaseMessage implements KompicsEvent {
    public DirectedEdge edge;

    public BaseMessage(DirectedEdge edge) {
        this.edge = edge;
    }

}
