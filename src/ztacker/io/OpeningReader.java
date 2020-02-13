package ztacker.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import ztacker.move.PresetMove;
import ztacker.move.zt.Opening;
import ztacker.tetromino.Tetromino;

public final class OpeningReader extends PresetMoveReader {
    
    private final File file;
    
    public OpeningReader(File file) {
        this.file = file;
    }
    
    public ArrayList<Opening> readOpenings(
            HashMap<Tetromino, ArrayList<PresetMove>> dest) throws 
            FileNotFoundException, IOException {
        BufferedReader br = new BufferedReader(new FileReader(file));
        ArrayList<PresetMove> preset = readPresetMoves(br);
        
        return generateOpenings(br, preset, dest);
    }
    
    private ArrayList<Opening> generateOpenings(BufferedReader br,
            ArrayList<PresetMove> preset, 
            HashMap<Tetromino, ArrayList<PresetMove>> dest) throws IOException {
        ArrayList<Opening> openings = new ArrayList<>();
        
        String line;
        while ((line = readNextLine(br)) != null) {
            openings.add(processOpening(removeCommentFrom(line), preset));
        }
        
        for (PresetMove m : preset) {
            if (dest.get(m.getType()) == null) {
                dest.put(m.getType(), new ArrayList<>());
            }
            
            dest.get(m.getType()).add(m);
        }
        
        return openings;
    }
    
    private Opening processOpening(String line,
            ArrayList<PresetMove> preset) {
        line = line.replace("{", "").replace("}", "");
        
        String[] indeces = line.split(",");
        ArrayList<PresetMove> moves = new ArrayList<>();
        for (String index : indeces) {
            PresetMove move = preset.get(Integer.parseInt(index));
            moves.add(move);
        }
        
        return new Opening(moves);
    }
    
    
    
    
}
