package node;

import node.state_reactor.Reactor;
import node.state_reactor.event.SEChange;

import java.util.Objects;

public class DirectedEdge {

    public final String src;
    public final String dst;
    public final long w;
    private State SE = State.BASIC;
    private final Reactor reactor;

    public enum State {
        BRANCH,
        BASIC,
        REJECTED
    }

    public DirectedEdge(String src, String dst, long w, Reactor reactor) {
        this.src = src;
        this.dst = dst;
        this.w = w;
        this.reactor = reactor;
    }

    @Override
    public String toString() {
        return "Edge{" +
                "SE=" + SE +
                ", src='" + src + '\'' +
                ", dst='" + dst + '\'' +
                ", w=" + w +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DirectedEdge)) return false;
        DirectedEdge that = (DirectedEdge) o;
        return src.equals(that.src) &&
                dst.equals(that.dst);
    }

    @Override
    public int hashCode() {
        return Objects.hash(src, dst);
    }

    public State getSE() {
        return SE;
    }

    public void setSE(State SE) {
        this.SE = SE;
        reactor.afterStateChanged(new SEChange(this));
    }
}
