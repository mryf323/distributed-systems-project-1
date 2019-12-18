package topology;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import static java.util.Arrays.asList;

public class UndirectedEdge {

    public final String src;
    public final String dst;
    public final long w;

    public UndirectedEdge(String src, String dst, long w) {
        this.src = src;
        this.dst = dst;
        this.w = w;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UndirectedEdge graphEdge = (UndirectedEdge) o;
        return (src.equals(graphEdge.src) && dst.equals(graphEdge.dst)) ||
                (src.equals(graphEdge.dst) && dst.equals(graphEdge.src));
    }

    @Override
    public int hashCode() {
        List<String> items = asList(src, dst);
        items.sort(Comparator.comparing(Function.identity()));
        return Objects.hash(items.get(0), items.get(1));
    }

    @Override
    public String toString() {
        return "GraphEdge{" +
                "src='" + src + '\'' +
                ", dst='" + dst + '\'' +
                ", w=" + w +
                '}';
    }
}
