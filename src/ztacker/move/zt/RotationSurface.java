package ztacker.move.zt;

import ztacker.matrix.GridModifier;
import ztacker.matrix.Matrix;

public final class RotationSurface {

    private static final long OVERFLOW = 0b1111_1111_1111_1111L
            << (Matrix.PARTIAL_GRID_WIDTH * Matrix.HALF_BORDER);

    private final long config;
    private final long fill;

    private final int min;
    private final int max;

    public RotationSurface(long config, long fill, int min, int max) {
        this.config = config;
        this.fill = fill;

        this.min = min;
        this.max = max;
    }

    @Override
    public String toString() {
        return Long.toBinaryString(config);
    }

    public long[] getGridFromBaseSurface(
            LeftBaseSurface base, long[] pgrid) {
        long[] cgrid = new long[2];
        long[] fgrid = new long[2];

        int height = base.getHeight();
        int index = base.getIndex();
        if (index < min || index > max || height >= Matrix.MAX_REAL_HEIGHT) {
            return null;
        }

        int shift = index - min;
        long shiftedConfig = config >>> shift;
        long shiftedFill = fill >>> shift;

        if (height >= Matrix.HALF_BORDER - 3
                && height <= Matrix.HALF_BORDER - 1) {
            long c = shiftedConfig << (Matrix.PARTIAL_GRID_WIDTH * height);
            long f = shiftedFill << (Matrix.PARTIAL_GRID_WIDTH * height);

            long c1 = (c & OVERFLOW) >>> (Matrix.PARTIAL_GRID_WIDTH
                    * Matrix.HALF_BORDER);
            long c0 = c & GridModifier.RELEVANT;

            long f1 = (f & OVERFLOW) >>> (Matrix.PARTIAL_GRID_WIDTH
                    * Matrix.HALF_BORDER);
            long f0 = f & GridModifier.RELEVANT;

            cgrid[1] = c1;
            cgrid[0] = c0;

            fgrid[1] = f1;
            fgrid[0] = f0;
        } else {
            long c = shiftedConfig
                    << (Matrix.PARTIAL_GRID_WIDTH
                    * (height % Matrix.HALF_BORDER));
            long f = shiftedFill
                    << (Matrix.PARTIAL_GRID_WIDTH
                    * (height % Matrix.HALF_BORDER));

            c &= GridModifier.RELEVANT;
            f &= GridModifier.RELEVANT;

            cgrid[height / Matrix.HALF_BORDER] = c;
            fgrid[height / Matrix.HALF_BORDER] = f;
        }

        for (int i = 0; i < pgrid.length; i++) {
            if ((pgrid[i] & cgrid[i]) != 0
                    || (pgrid[i] & fgrid[i]) != fgrid[i]) {
                return null;
            }
        }

        return cgrid;
    }
}
