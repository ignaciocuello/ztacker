package ztacker.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import ztacker.matrix.Matrix;

public class GridReader extends LineReader {
    
    public static final int BINARY_RADIX = 2;
    public static final String GRID_HEADER = "[M]";
    public static final String KEY_HEADER = "[K]";
    
    public ArrayList<long[]> readGrids(BufferedReader br) throws IOException {        
        ArrayList<long[]> grids = new ArrayList<>();
        
        String line = readNextLine(br);
        //line may be null so compariosn from GRID_HEADER needed
        if (!GRID_HEADER.equals(line)) {
            throw new IllegalArgumentException();
        }
        
        while ((line = readNextLine(br)) != null && !line.equals(KEY_HEADER)) {
            grids.add(readGrid(removeCommentFrom(line)));
        }
        
        if (!line.equals(KEY_HEADER)) {
            throw new IllegalArgumentException();
        }
        
        return grids;
    }
    
     public long[] readGrid(String line) {
         if (line.equalsIgnoreCase("NULL")) {
             return null;
         }
         
        long[] grid = new long[Matrix.NUM_PARTIAL_GRIDS];
        String[] partial = line.split("/");
        
        if (grid.length != partial.length) {
            throw new IllegalArgumentException();
        }
        
        for (int i = 0; i < grid.length; i++) {
            grid[i] = Long.parseLong(
                    partial[i].replace("_", ""), BINARY_RADIX);
        }
                
        return grid;
     }
}
