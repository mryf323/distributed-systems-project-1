package file_io;

import topology.UndirectedEdge;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;

public class MstWriter {
    private MstWriter() {
    }

    private static MstWriter instance;

    public static MstWriter getInstance() {
        if (instance == null)
            instance = new MstWriter();
        return instance;
    }

    public void writeMst(Set<UndirectedEdge> edges) throws IOException {
        FileWriter fileWriter = new FileWriter("mst.txt");
        PrintWriter printWriter = new PrintWriter(fileWriter);
        edges.forEach(e -> printWriter.printf("%s-%s,%d%n", e.src, e.dst, e.w));
        printWriter.close();
    }
}
