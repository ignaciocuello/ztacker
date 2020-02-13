package ztacker.matrix;

public final class GridModifier {

    public static final long RELEVANT
            = 0b1111_1111_1111_1111_1111_1111_1111_1111_1111_1111_1111_1111L;

    private static final long SINGLE_WIDTH_RAY
            = 0b1000_1000_1000_1000_1000_1000_1000_1000_1000_1000_1000_1000L;

    private GridModifier() {
    }

    public static void set(long[][] dest, long[][] src) {
        dest[0][0] |= src[0][0] & RELEVANT;
        dest[0][1] |= src[0][1] & RELEVANT;

        dest[1][0] |= src[1][0] & RELEVANT;
        dest[1][1] |= src[1][1] & RELEVANT;

        dest[2][0] |= src[2][0] & RELEVANT;
        dest[2][1] |= src[2][1] & RELEVANT;
    }

    public static void clear(long[][] dest, long[][] src) {
        dest[0][0] &= ~src[0][0] & RELEVANT;
        dest[0][1] &= ~src[0][1] & RELEVANT;

        dest[1][0] &= ~src[1][0] & RELEVANT;
        dest[1][1] &= ~src[1][1] & RELEVANT;

        dest[2][0] &= ~src[2][0] & RELEVANT;
        dest[2][1] &= ~src[2][1] & RELEVANT;
    }

    public static boolean isRowPrint(long[][] grid, long print, int rowNum) {
        return (getRow(grid, rowNum) & print) == print;
    }

    public static long getRow(long[][] grid, int rowNum) {
        int half = rowNum / Matrix.HALF_BORDER;
        int index = rowNum % Matrix.HALF_BORDER;

        int shift = index * Matrix.PARTIAL_GRID_WIDTH;
        long row = (((grid[0][half] & (0b1111L << shift)) >>> shift) << 6)
                | (((grid[1][half] & (0b1111L << shift)) >>> (shift + 1)) << 3)
                | ((grid[2][half] & (0b1111L << shift)) >>> (shift + 1));
        return row & RELEVANT;
    }
    
    public static void clearGridPortion(long[][] grid,
            int cFrom, int cTo) {
        long[][] upper = clearDownwards(grid, cTo);
        long[][] lower = clearUpwards(grid, cFrom);
        upper = shiftDown(upper, cTo - cFrom + 1);

        for (int partial = 0; partial < grid.length; partial++) {
            for (int half = 0; half < grid[partial].length; half++) {
                grid[partial][half]
                        = (upper[partial][half] | lower[partial][half]) & RELEVANT;
            }
        }
    }

    public static void revertClearGridPortion(long[][] grid,
            int cFrom, int cTo) {
        long[][] upper = clearDownwards(grid, cFrom - 1);
        long[][] lower = clearUpwards(grid, cFrom);
        upper = shiftUp(upper, cTo - cFrom + 1);

        long[][] fill = new long[][]{
            {RELEVANT, RELEVANT},
            {RELEVANT, RELEVANT},
            {RELEVANT, RELEVANT}
        };

        fill = clearUpwards(fill, cTo + 1);
        fill = clearDownwards(fill, cFrom - 1);

        for (int partial = 0; partial < grid.length; partial++) {
            for (int half = 0; half < grid[partial].length; half++) {
                grid[partial][half]
                        = (upper[partial][half]
                        | lower[partial][half] | fill[partial][half]) & RELEVANT;
            }
        }
    }

    public static long[][] clearUpwards(long[][] grid, int from) {
        long[][] m = new long[grid.length][grid[0].length];
        for (int i = 0; i < grid.length; i++) {
            System.arraycopy(grid[i], 0, m[i], 0, grid[i].length);
        }

        if (from < Matrix.HALF_BORDER) {
            long filter = ~(~0L << (from * Matrix.PARTIAL_GRID_WIDTH));

            for (int i = 0; i < m.length; i++) {
                m[i][1] = 0L;
                m[i][0] &= filter;
                m[i][0] &= RELEVANT;
            }
        } else {
            from -= Matrix.HALF_BORDER;
            long filter = ~(~0L << (from * Matrix.PARTIAL_GRID_WIDTH));

            for (int i = 0; i < m.length; i++) {
                m[i][1] &= filter;
                m[i][1] &= RELEVANT;
            }
        }

        return m;
    }

    public static long[][] clearDownwards(long[][] grid, int from) {
        from++;
        long[][] m = new long[grid.length][grid[0].length];
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                m[i][j] = grid[i][j];
                m[i][j] &= RELEVANT;
            }
        }

        if (from < Matrix.HALF_BORDER) {
            long filter = ~0L << (from * Matrix.PARTIAL_GRID_WIDTH);

            for (int i = 0; i < m.length; i++) {
                m[i][0] &= filter;
                m[i][0] &= RELEVANT;
            }
        } else {
            from -= Matrix.HALF_BORDER;
            long filter = ~0L << (from * Matrix.PARTIAL_GRID_WIDTH);

            for (int i = 0; i < m.length; i++) {
                m[i][1] &= filter;
                m[i][1] &= RELEVANT;
                m[i][0] = 0L;
            }
        }

        return m;
    }

    public static void setUpwardRayExtension(long[] halfGrid,
            long[] freeHard) {
        for (int i = 0; i < halfGrid.length; i++) {
            long mp = halfGrid[i];
            for (int x = 0; x < Matrix.PARTIAL_GRID_WIDTH; x++) {
                for (int y = 0; y < Matrix.HALF_BORDER; y++) {
                    long portion
                            = (0b1000L >>> x)
                            << (Matrix.PARTIAL_GRID_WIDTH * y);
                    if ((mp & portion) != 0L) {
                        freeHard[i]
                                |= (SINGLE_WIDTH_RAY >>> x)
                                << (Matrix.PARTIAL_GRID_WIDTH * y);
                        freeHard[i] &= RELEVANT;
                        break;
                    }
                }
            }
        }
    }

    public static void setOrGridHalf(long[] halfGrid, long[] or) {
        for (int i = 0; i < halfGrid.length; i++) {
            halfGrid[i] |= or[i];
            halfGrid[i] &= RELEVANT;
        }
    }

    public static void clearOrGridHalf(long[] halfGrid, long[] or) {
        for (int i = 0; i < halfGrid.length; i++) {
            halfGrid[i] &= ~or[i];
            halfGrid[i] &= RELEVANT;
        }
    }

    public static long[][] shiftUp(long[][] grid, int offset) {
        return new long[][]{
            shiftUp(grid[0], offset),
            shiftUp(grid[1], offset),
            shiftUp(grid[2], offset)};
    }

    public static long[][] shiftDown(long[][] grid, int offset) {
        return new long[][]{
            shiftDown(grid[0], offset),
            shiftDown(grid[1], offset),
            shiftDown(grid[2], offset)};
    }

    public static long[] shiftUp(long[] partialGrid, int offset) {
        if (offset < Matrix.HALF_BORDER) {
            int shift = offset * Matrix.PARTIAL_GRID_WIDTH;
            int overflow = (Matrix.HALF_BORDER - offset)
                    * Matrix.PARTIAL_GRID_WIDTH;
            return new long[]{
                partialGrid[0] << shift,
                ((partialGrid[1] << shift) | (partialGrid[0] >>> overflow))
                & RELEVANT
            };
        } else {
            int overflow = (offset - Matrix.HALF_BORDER)
                    * Matrix.PARTIAL_GRID_WIDTH;
            return new long[]{0L,
                (partialGrid[0] << overflow) & RELEVANT};
        }
    }

    public static long[] shiftDown(long[] partialGrid, int offset) {
        if (offset < Matrix.HALF_BORDER) {
            int shift = offset * Matrix.PARTIAL_GRID_WIDTH;
            int overflow = (Matrix.HALF_BORDER - offset)
                    * Matrix.PARTIAL_GRID_WIDTH;
            return new long[]{
                ((partialGrid[0] >>> shift) | (partialGrid[1] << overflow))
                & RELEVANT,
                partialGrid[1] >>> shift
            };
        } else {
            int overflow = (offset - Matrix.HALF_BORDER)
                    * Matrix.PARTIAL_GRID_WIDTH;
            return new long[]{(partialGrid[1] >>> overflow) & RELEVANT, 0L};
        }
    }

    public static boolean isFree(long[] halfGrid, long[] free) {
        return free != null
                && ((halfGrid[0] & free[0]) == 0L
                && (halfGrid[1] & free[1]) == 0L
                && (halfGrid[2] & free[2]) == 0L);
    }

    public static boolean isFilled(long[] halfGrid, long[] fill) {
        return (halfGrid[0] & fill[0]) == fill[0]
                && (halfGrid[1] & fill[1]) == fill[1]
                && (halfGrid[2] & fill[2]) == fill[2];
    }

    public static boolean isFree(long[][] grid, long[] free) {
        return (grid[0][0] & free[0]) == 0L
                && (grid[1][0] & free[1]) == 0L
                && (grid[2][0] & free[2]) == 0L;
    }

    public static boolean isFilled(long[][] grid, long[] fill) {
        return (grid[0][0] & fill[0]) == fill[0]
                && (grid[1][0] & fill[1]) == fill[1]
                && (grid[2][0] & fill[2]) == fill[2];
    }

    public static boolean isFree(long[][] grid, long[][] free) {
        return (grid[0][0] & free[0][0]) == 0L
                && (grid[1][0] & free[1][0]) == 0L
                && (grid[2][0] & free[2][0]) == 0L
                && (grid[0][1] & free[0][1]) == 0L
                && (grid[1][1] & free[1][1]) == 0L
                && (grid[2][1] & free[2][1]) == 0L;
    }

    public static boolean isFilled(long[][] grid, long[][] fill) {
        return (grid[0][0] & fill[0][0]) == fill[0][0]
                && (grid[1][0] & fill[1][0]) == fill[1][0]
                && (grid[2][0] & fill[2][0]) == fill[2][0]
                && (grid[0][1] & fill[0][1]) == fill[0][1]
                && (grid[1][1] & fill[1][1]) == fill[1][1]
                && (grid[2][1] & fill[2][1]) == fill[2][1];
    }

    public static long[] normalizeGrid(long[][] grid) {
        int yOffset = getYOffset(grid);
        long[][] normGrid = shiftDown(grid, yOffset);

        return new long[]{
            normGrid[0][0],
            normGrid[1][0],
            normGrid[2][0]};
    }
    
    public static int getYOffset(long[][] grid) {
        int yOffset = 0;
        for (int i = 0; i < Matrix.MAX_PROCESS_HEIGHT; i++) {
            int half = i / Matrix.HALF_BORDER;
            long section = 0b1111L << (i % Matrix.HALF_BORDER)
                    * Matrix.PARTIAL_GRID_WIDTH;
            if (((grid[0][half] | grid[1][half] | grid[2][half])
                    & section) != 0) {
                yOffset = i;
                break;
            }
        }
        
        return yOffset;
    }

    public static long[][] copy(long[][] grid) {
        if (grid == null) {
            return null;
        }

        long[][] cgrid = new long[grid.length][grid[0].length];
        for (int i = 0; i < cgrid.length; i++) {
            System.arraycopy(grid[i], 0, cgrid[i], 0, cgrid[i].length);
        }

        return cgrid;
    }
}
