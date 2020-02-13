package ztacker.move;

import ztacker.tetromino.Tetromino;

public class Move {
    
    private static final int UNSET_CLEAR = -1;
    
    private final long[][] grid;
    private final Tetromino type;
    
    private final boolean hold;
    private final SoftDropCommands softDrop;
    
    private final int clearFrom;
    private final int clearTo;
        
    public Move(long[][] grid, Tetromino type) {
        this(grid, type, false);
    }
    
    public Move(long[][] grid, Tetromino type, boolean hold) {
        this(grid, type, hold, null);
    }
    
    public Move(long[][] grid, Tetromino type, boolean hold, 
            SoftDropCommands softDrop) {
        this(grid, type, hold, softDrop, 
                UNSET_CLEAR, UNSET_CLEAR);
    }
    
    public Move(long[][] grid, Tetromino type, boolean hold, 
            SoftDropCommands softDrop, int clearFrom, int clearTo) {
        this.grid = grid;
        this.type = type;
        this.hold = hold;
        this.softDrop = softDrop;
        this.clearFrom = clearFrom;
        this.clearTo = clearTo;
    }
   
    public Move(Move move, boolean hold) {
        this(move.grid, move.type, hold, 
                move.softDrop, move.clearFrom, move.clearTo);
    }
    
    public long[][] getGrid() {
        return grid;
    }
    
    public Tetromino getType() {
        return type;
    }   
    
    public boolean isHold() {
        return hold;
    }
    
    public boolean isSoftDrop() {
        return softDrop != null;
    }
    
    public int getClearTo() {
        return clearTo;
    }
    
    public int getClearFrom() {
        return clearFrom;
    }
    
    public boolean isClear() {
        return clearFrom != UNSET_CLEAR && clearTo != UNSET_CLEAR;
    }
    
    public SoftDropCommands getSoftDrop() {
        return softDrop;
    }
}
