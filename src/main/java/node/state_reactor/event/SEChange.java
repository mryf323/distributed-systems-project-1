package node.state_reactor.event;

import node.DirectedEdge;

import java.util.Objects;

public class SEChange implements StateEvent {
    private final DirectedEdge edge;

    public SEChange(DirectedEdge edge) {
        this.edge = edge;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SEChange)) return false;
        SEChange seChange = (SEChange) o;
        return Objects.equals(edge, seChange.edge);
    }

    @Override
    public int hashCode() {
        return Objects.hash(edge);
    }
}
