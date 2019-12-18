package message;

import node.DirectedEdge;

public class Report extends BaseMessage {

    public final long w;

    public Report(long w, DirectedEdge edge) {
        super((edge));
        this.w = w;
    }

    @Override
    public String toString() {
        return "Report{" +
                "w=" + w +
                ", edge=" + edge +
                '}';
    }
}
