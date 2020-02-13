package ztacker.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import ztacker.move.pf.PFCycle;
import ztacker.move.PresetMove;
import ztacker.tetromino.Tetromino;

public final class PFCycleReader extends PresetMoveReader {

    private final File file;

    public PFCycleReader(File file) {
        this.file = file;
    }

    public ArrayList<PFCycle> readPFCycles() throws
            FileNotFoundException, IOException {
        BufferedReader br = new BufferedReader(new FileReader(file));
        ArrayList<PresetMove> preset = readPresetMoves(br);

        return groupPresetMoves(br, preset);
    }
    
    private ArrayList<PFCycle> groupPresetMoves(BufferedReader br, 
            ArrayList<PresetMove> preset) throws IOException {
        ArrayList<PFCycle> cycles = new ArrayList<>();
        
        String line;
        while ((line = readNextLine(br)) != null) {
            cycles.add(
                    processCycleLine(removeCommentFrom(line), preset));
        }
        
        return cycles;
    }

    private PFCycle processCycleLine(String line, 
            ArrayList<PresetMove> preset) {
        String spl[] = line.split("/");
        
        int total = Integer.parseInt(spl[1]);
        line = spl[0].replace("{", "").replace("}", "");
        
        String[] indeces = line.split(",");
        HashMap<Tetromino, ArrayList<PresetMove>> cyclesMap = new HashMap<>();
        for (String index : indeces) {
            PresetMove move = preset.get(Integer.parseInt(index));
            if (cyclesMap.get(move.getType()) == null) {
                cyclesMap.put(move.getType(), new ArrayList<>());
            }
            
            cyclesMap.get(move.getType()).add(move);
        }
        
        return new PFCycle(cyclesMap, total);
    }
}
