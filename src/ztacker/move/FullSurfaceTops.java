package ztacker.move;

import ztacker.matrix.Matrix;

public final class FullSurfaceTops {

    private final PartialSurfaceTops[] ptops;

    private final int min;
    private final int max;

    public FullSurfaceTops(PartialSurfaceTops[] ptops, int min, int max) {
        this.ptops = ptops;
        this.min = min;
        this.max = max;
    }

    public int[] getLeftTops() {
        return ptops[Matrix.LEFT_SURFACE_INDEX].getTops();
    }

    public int[] getMidTops() {
        return ptops[Matrix.MID_SURFACE_INDEX].getTops();
    }

    public int[] getRightTops() {
        return ptops[Matrix.RIGHT_SURFACE_INDEX].getTops();
    }

    public int getLeftMin() {
        return ptops[Matrix.LEFT_SURFACE_INDEX].getMin();
    }

    public int getMidMin() {
        return ptops[Matrix.MID_SURFACE_INDEX].getMin();
    }

    public int getRightMin() {
        return ptops[Matrix.RIGHT_SURFACE_INDEX].getMin();
    }

    public int getLeftMax() {
        return ptops[Matrix.LEFT_SURFACE_INDEX].getMax();
    }

    public int getMidMax() {
        return ptops[Matrix.MID_SURFACE_INDEX].getMax();
    }

    public int getRightMax() {
        return ptops[Matrix.RIGHT_SURFACE_INDEX].getMax();
    }

    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }

    public static FullSurfaceTops constructFullSurfaceTop(Matrix matrix) {
        PartialSurfaceTops[] ptops
                = new PartialSurfaceTops[Matrix.NUM_PARTIAL_GRIDS];
        int[] mins = new int[ptops.length];
        int[] maxs = new int[ptops.length];

        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;

        for (int pg = 0; pg < ptops.length; pg++) {
            int[] top = extractTopSurface(matrix.getGrid()[pg]);
            if (pg != 0) {
                top = convertTo3TopSurface(top);
            }
            extractMinMax(top, mins, maxs, pg);

            if (mins[pg] < min) {
                min = mins[pg];
            }

            if (maxs[pg] > max) {
                max = maxs[pg];
            }

            ptops[pg] = new PartialSurfaceTops(top, mins[pg], maxs[pg]);
        }

        return new FullSurfaceTops(ptops, min, max);
    }

    private static int[] extractTopSurface(long[] pgrid) {
        int[] scan = new int[Matrix.PARTIAL_GRID_WIDTH];
        int[] tops = new int[Matrix.PARTIAL_GRID_WIDTH];

        for (int i = 0; i < scan.length; i++) {
            scan[i] = 0b1000 >>> i;
        }

        for (int i = Matrix.MAX_PROCESS_HEIGHT - 1; i >= 0; i--) {
            int shift = Matrix.PARTIAL_GRID_WIDTH
                    * (i % Matrix.HALF_BORDER);
            long section = 0b1111L << shift;
            long current = (pgrid[i / Matrix.HALF_BORDER] & section) >>> shift;

            if (current != 0L) {
                for (int j = 0; j < scan.length; j++) {
                    if ((current & scan[j]) != 0L) {
                        tops[j] = i + 1;
                        scan[j] = 0;
                    }
                }

                if ((scan[0] | scan[1] | scan[2] | scan[3]) == 0) {
                    break;
                }
            }
        }

        return tops;
    }

    private static int[] convertTo3TopSurface(int[] tops) {
        int[] tops3 = new int[3];
        System.arraycopy(tops, 0, tops3, 0, tops3.length);

        return tops3;
    }

    private static void extractMinMax(int[] tops, int[] mins, int[] maxs,
            int pg) {
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;

        for (int i = 0; i < tops.length; i++) {
            if (tops[i] < min) {
                min = tops[i];
            }

            if (tops[i] > max) {
                max = tops[i];
            }
        }

        mins[pg] = min;
        maxs[pg] = max;
    }
}
