package ztacker.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import ztacker.tetromino.Tetromino;

public final class QueueCapturerReader extends LineReader {

    private static final int HEX_RADIX = 16;

    private final File file;

    private HashMap<Integer, Tetromino> colorMap;
    private int[] queueX;
    private int[] queueY;

    public QueueCapturerReader(File file) {
        this.file = file;
    }

    public void readData() {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            colorMap = processColorMap(readNextCleanLine(br));
            queueX = processQueue(readNextCleanLine(br));
            queueY = processQueue(readNextCleanLine(br));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private HashMap<Integer, Tetromino> processColorMap(String line) {
        HashMap<Integer, Tetromino> cmap = new HashMap<>();
        line = line.replace("{", "").replace("}", "");
        String[] tuples = line.split(",");

        for (String tuple : tuples) {
            tuple = tuple.replace("(", "").replace(")", "");
            String[] split = tuple.split("/");

            cmap.put((int)Long.parseLong(split[0], HEX_RADIX),
                    Tetromino.valueOf(split[1] + "_TYPE"));
        }
        
        return cmap;
    }

    private int[] processQueue(String line) {
        ArrayList<Integer> intList = new ArrayList<>();
        
        line = line.replace("[", "").replace("]", "");
        String[] ints = line.split(",");
        for (String i : ints) {
            intList.add(Integer.parseInt(i));
        }
        
        int[] intArray = new int[intList.size()];
        for (int i = 0; i < intArray.length; i++) {
            intArray[i] = intList.get(i);
        }
        
        return intArray;
    }
    
    public HashMap<Integer, Tetromino> getColorMap() {
        return colorMap;
    }
    
    public int[] getQueueX() {
        return queueX;
    }
    
    public int[] getQueueY() {
        return queueY;
    }
}
