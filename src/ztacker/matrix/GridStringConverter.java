package ztacker.matrix;


public final class GridStringConverter {
        
    private GridStringConverter() {
    }
    
    public static String convertGrid(long[][] grid) {
        String gridString = "";
        for (int i = Matrix.MAX_PROCESS_HEIGHT - 1; i >= 0; i--) {
            for (int j = 0; j < Matrix.NUM_PARTIAL_GRIDS; j++) {
                gridString += convertPartialGridSlice(grid[j], i, j != 0);
            }
            gridString += "\n";
        }
        
        return gridString;
    }
    
    public static String convertHalfGrid(long[] halfGrid) {
        String gridString = "";
        for (int i = Matrix.HALF_BORDER- 1; i >= 0; i--) {
            for (int j = 0; j < Matrix.NUM_PARTIAL_GRIDS; j++) {
                gridString += convertPartialGridSlice(
                        new long[]{halfGrid[j], 0L}, i, j != 0);
            }
            gridString += "\n";
        }
        
        return gridString;
    }
    
    public static String convertPartialGrid(long[] partial, boolean triple) {
        String partialStr = "";
        for (int i = Matrix.MAX_PROCESS_HEIGHT - 1; i >= 0; i--) {
            partialStr += convertPartialGridSlice(partial, i, triple) + "\n";
        }
        
        return partialStr;
    }
    
    private static String convertPartialGridSlice(long[] partial, 
            int height, boolean triple) {
        int shift = (height % Matrix.HALF_BORDER) * 
                        Matrix.PARTIAL_GRID_WIDTH;
        
        long section = 0b1111L << shift;
        long current = (partial[height / Matrix.HALF_BORDER] & section) 
                        >>> shift;
        
        String str = String.format("%4s",
                Long.toBinaryString(current)).replace(" ", "0");
        if (triple) {
            str = str.substring(0, str.length() - 1);
        }
        
        return str;
    }
}
