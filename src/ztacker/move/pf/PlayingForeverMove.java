package ztacker.move.pf;

import ztacker.move.PresetMove;
import ztacker.move.Move;
import ztacker.move.SoftDropCommands;
import ztacker.tetromino.Tetromino;

public final class PlayingForeverMove extends Move {

    private final int surfaceIndex;
    private final PresetMove cycleKey;

    public PlayingForeverMove(long[][] grid, int surfaceIndex, Tetromino type,
            PresetMove cycleKey) {
        this(grid, surfaceIndex, type, cycleKey, false);
    }
    
    public PlayingForeverMove(long[][] grid, int surfaceIndex, Tetromino type, 
            PresetMove cycleKey, boolean hold) {
        this(grid, surfaceIndex, type, cycleKey, hold, null);
    }

    public PlayingForeverMove(long[][] grid, int surfaceIndex, Tetromino type, 
            PresetMove cycleKey, boolean hold, SoftDropCommands softDrop) {
        super(grid, type, hold, softDrop);
        this.surfaceIndex = surfaceIndex;
        this.cycleKey = cycleKey;
    }

    public PlayingForeverMove(long[][] grid, int surfaceIndex, Tetromino type,
            PresetMove cycleKey, boolean hold, SoftDropCommands softDrop,
            int clearFrom, int clearTo) {
        super(grid, type, hold, softDrop, clearFrom, clearTo);
        this.surfaceIndex = surfaceIndex;
        this.cycleKey = cycleKey;
    }
    
    public int getSurfaceIndex() {
        return surfaceIndex;
    }
    
    public PresetMove getCycleKey() {
        return cycleKey;
    }
}
