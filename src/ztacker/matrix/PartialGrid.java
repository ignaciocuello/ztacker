package ztacker.matrix;

import java.util.Arrays;

public final class PartialGrid {
    
    private final long[] pgrid;
    
    public PartialGrid(long[] pgrid) {
        this.pgrid = pgrid;
    }
    
    @Override
    public boolean equals(Object o) {
        return o instanceof PartialGrid ? 
                Arrays.equals(pgrid, ((PartialGrid)o).pgrid): false;
    }
    
    @Override
    public int hashCode() {
        return Arrays.hashCode(pgrid);
    }
}
