package ztacker.move;

public final class PartialSurfaceTops {
    
    private final int[] tops;
    
    private final int min;
    private final int max;
    
    public PartialSurfaceTops(int[] tops, int min, int max) {
        this.tops = tops;
        this.min = min;
        this.max = max;
    }
    
    public int[] getTops() {
        return tops;
    }
    
    public int getMin() {
        return min;
    }
    
    public int getMax() {
        return max;
    }
}
