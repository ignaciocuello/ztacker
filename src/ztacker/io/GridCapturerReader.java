package ztacker.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public final class GridCapturerReader extends LineReader {

    private final File file;

    private int gridX;

    private int gridY;
    private int gridUpperY;

    private int gridXStep;
    private int gridYStep;

    public GridCapturerReader(File file) {
        this.file = file;
    }

    public void readData() {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            gridX = Integer.parseInt(readNextCleanLine(br));
            gridY = Integer.parseInt(readNextCleanLine(br));
            gridUpperY = Integer.parseInt(readNextCleanLine(br));
            gridXStep = Integer.parseInt(readNextCleanLine(br));
            gridYStep = Integer.parseInt(readNextCleanLine(br));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    public int getGridX() {
        return gridX;
    }

    public int getGridY() {
        return gridY;
    }

    public int getGridUpperY() {
        return gridUpperY;
    }

    public int getGridXStep() {
        return gridXStep;
    }

    public int getGridYStep() {
        return gridYStep;
    }
}
