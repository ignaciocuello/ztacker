package ztacker.move.zt;

public final class MidSurfacePair {

    private final MidSurfaceDiscriminator discr;
    private final long[][] grid;

    public MidSurfacePair(MidSurfaceDiscriminator discr, long[][] grid) {
        this.discr = discr;
        this.grid = grid;
    }

    public MidSurfaceDiscriminator getDiscriminator() {
        return discr;
    }

    public long[][] getGrid() {
        return grid;
    }
}
