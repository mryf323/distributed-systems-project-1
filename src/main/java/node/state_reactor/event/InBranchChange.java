package node.state_reactor.event;

import java.util.Objects;

public class InBranchChange implements StateEvent {

    @Override
    public int hashCode() {
        return Objects.hashCode(this.getClass());
    }

    @Override
    public boolean equals(Object obj) {
        return this.getClass().equals(obj.getClass());
    }
}
