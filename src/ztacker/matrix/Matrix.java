package ztacker.matrix;

import java.util.LinkedList;
import ztacker.tetromino.Tetromino;

public final class Matrix {

    public static final int MAX_UPPER_HEIGHT = 4;

    public static final int MAX_PROCESS_HEIGHT = 24;
    public static final int MAX_REAL_HEIGHT = 20;

    public static final int MAX_WIDTH = 10;

    public static final int HALF_BORDER = 12;
    public static final int NUM_HALVES = 2;

    public static final int PARTIAL_GRID_WIDTH = 4;
    public static final int NUM_PARTIAL_GRIDS = 3;

    public static final int LEFT_SURFACE_INDEX = 0;
    public static final int MID_SURFACE_INDEX = 1;
    public static final int RIGHT_SURFACE_INDEX = 2;

    private Tetromino active;
    private Tetromino held;

    private final long[][] grid;

    private LinkedList<Tetromino> queue;

    private boolean holdUsed;

    public Matrix() {
        this.grid = Matrix.createEmptyGrid();
    }

    public Matrix(Matrix matrix) {
        this();
        setActive(matrix.getActive());
        setHeld(matrix.getHeld());
        setQueue(matrix.getQueue());
        setHoldUsed(matrix.isHoldUsed());

        set(matrix.getGrid());
    }

    public final void set(long[][] g) {
        GridModifier.set(grid, g);
    }

    public void clear(long[][] g) {
        GridModifier.clear(grid, g);
    }

    public void clearRange(int cFrom, int cTo) {
        GridModifier.clearGridPortion(grid, cFrom, cTo);
    }

    public void revertClearRange(int cFrom, int cTo) {
        GridModifier.revertClearGridPortion(grid, cFrom, cTo);
    }

    public boolean isFilled(long[] gridHalf) {
        return GridModifier.isFilled(grid, gridHalf);
    }

    public boolean isFree(long[] gridHalf) {
        return GridModifier.isFree(grid, gridHalf);
    }

    public final long[][] getGrid() {
        return grid;
    }

    public final LinkedList<Tetromino> getQueue() {
        return queue;
    }

    public final void setQueue(LinkedList<Tetromino> queue) {
        this.queue = queue;
    }

    public final boolean isHoldUsed() {
        return holdUsed;
    }

    public final void setHoldUsed(boolean holdUsed) {
        this.holdUsed = holdUsed;
    }

    public final Tetromino getActive() {
        return active;
    }

    public final void setActive(Tetromino active) {
        this.active = active;
    }

    public final Tetromino getHeld() {
        return held;
    }

    public final void setHeld(Tetromino held) {
        this.held = held;
    }

    public static long[][] createEmptyGrid() {
        return new long[NUM_PARTIAL_GRIDS][NUM_HALVES];
    }
}
