package ztacker.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public final class ObjectLocatorReader extends LineReader {
    
    private static final int HEX_RADIX = 16;
    
    private final File file;
    
    public ObjectLocatorReader(File file) {
        this.file = file;
    }
    
    public int[][] readCorners() {
        ArrayList<int[]> corners = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = readNextLine(br)) != null) {
                corners.add(processCorner(removeCommentFrom(line)));
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            return corners.toArray(new int[0][]);
        }
    }
    
    private int[] processCorner(String line) {
        ArrayList<Integer> corner = new ArrayList<>();
        line = line.replace("[", "").replace("]", "");
        String[] rgbs = line.split(",");
        for (String rgb : rgbs) {
            corner.add((int)Long.parseLong(rgb.trim(), HEX_RADIX));
        }
        
        int[] cornerArray = new int[corner.size()];
        for (int i = 0; i < corner.size(); i++) {
            cornerArray[i] = corner.get(i);
        }
        
        return cornerArray;
    }
}
