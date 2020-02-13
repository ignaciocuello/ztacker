package ztacker.robot.out;
        
import java.util.Arrays;

public final class HalfGrid {
    
    private final long[] norm;
    
    public HalfGrid(long[] norm) {
        this.norm = norm;
    }
    
    public long[] getNorm() {
        return norm;
    }
    
    @Override
    public int hashCode() {
        return Arrays.hashCode(norm);
    }
    
    @Override
    public boolean equals(Object o) {
        return o instanceof HalfGrid ?
                Arrays.equals(((HalfGrid) o).norm, norm) : false;
    }
}
