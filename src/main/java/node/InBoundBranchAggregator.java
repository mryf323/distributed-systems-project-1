package node;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import topology.UndirectedEdge;

import java.util.HashSet;
import java.util.Set;

public class InBoundBranchAggregator {
    private static Logger logger = LoggerFactory.getLogger(InBoundBranchAggregator.class);
    private static InBoundBranchAggregator instance;

    private InBoundBranchAggregator(){}
    private Set<UndirectedEdge> inBoundBranches = new HashSet<>();

    public static InBoundBranchAggregator getInstance() {
        if (instance == null)
            instance = new InBoundBranchAggregator();
        return instance;
    }

    public void add(UndirectedEdge edge) {
        inBoundBranches.add(edge);
        logger.debug("New edge {} added", edge);
    }

    public Set<UndirectedEdge> getInBoundBranches() {
        return inBoundBranches;
    }
}
