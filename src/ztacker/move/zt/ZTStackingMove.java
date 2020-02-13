package ztacker.move.zt;

import ztacker.move.Move;
import ztacker.move.PresetMove;
import ztacker.move.SoftDropCommands;
import ztacker.tetromino.Tetromino;

public final class ZTStackingMove extends Move {
    
    private final int surfaceIndex;
    private PresetMove openingKey;
    
    public ZTStackingMove(long[][] grid, int surfaceIndex, Tetromino type) {
        this(grid, surfaceIndex, type, false);
    }
    
    public ZTStackingMove(long[][] grid, int surfaceIndex, Tetromino type, 
            boolean hold) {
        this(grid, surfaceIndex, type, hold, null);
    }
    
    public ZTStackingMove(long[][] grid, int surfaceIndex, Tetromino type, 
            boolean hold, SoftDropCommands softDrop) {
        super(grid, type, hold, softDrop);
        this.surfaceIndex = surfaceIndex;
    }
    
    public ZTStackingMove(long[][] grid, int surfaceIndex, Tetromino type, 
            boolean hold, SoftDropCommands softDrop, 
            int clearFrom, int clearTo) {
        super(grid, type, hold, softDrop, clearFrom, clearTo);
        this.surfaceIndex = surfaceIndex;
    }

    public int getSurfaceIndex() {
        return surfaceIndex;
    }
    
    public PresetMove getOpeningKey() {
        return openingKey;
    }
    
    public void setOpeningKey(PresetMove openingKey) {
        this.openingKey = openingKey; 
    }
}
