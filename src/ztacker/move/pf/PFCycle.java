package ztacker.move.pf;

import ztacker.move.PresetMove;
import java.util.ArrayList;
import java.util.HashMap;
import ztacker.matrix.GridModifier;
import ztacker.matrix.Matrix;
import ztacker.move.FullSurfaceTops;
import ztacker.move.Move;
import ztacker.tetromino.Tetromino;

public class PFCycle {

    private final long[] localGrid;
    private final long[] targetGrid;
    private final int height;

    private int base;
    private final HashMap<Tetromino, ArrayList<PresetMove>> cycleKeys;

    private final int total;
    int made;

    public PFCycle(HashMap<Tetromino, ArrayList<PresetMove>> cycleKeys,
            int total) {
        this.cycleKeys = cycleKeys;
        this.targetGrid = generateTargetGrid();
        this.height = deriveHeight();
        this.total = total;

        localGrid = new long[Matrix.NUM_PARTIAL_GRIDS];
    }

    public PFCycle(PFCycle cycle) {
        this.cycleKeys = cycle.cycleKeys;
        this.targetGrid = cycle.targetGrid;
        this.height = cycle.height;
        this.total = cycle.total;

        localGrid = new long[Matrix.NUM_PARTIAL_GRIDS];
    }

    private long[] generateTargetGrid() {
        long[] target = new long[Matrix.NUM_PARTIAL_GRIDS];
        for (Tetromino t : cycleKeys.keySet()) {
            for (PresetMove key : cycleKeys.get(t)) {
                GridModifier.setOrGridHalf(target, key.getGrid());
            }
        }

        return target;
    }

    private int deriveHeight() {
        int height = 0;
        while (height < Matrix.HALF_BORDER
                && ((targetGrid[Matrix.LEFT_SURFACE_INDEX]
                | targetGrid[Matrix.MID_SURFACE_INDEX]
                | targetGrid[Matrix.RIGHT_SURFACE_INDEX])
                & (0b1111L << (height * Matrix.PARTIAL_GRID_WIDTH))) != 0L) {
            height++;
        }

        return height;
    }

    public ArrayList<Move> getValidMoves(Matrix matrix,
            FullSurfaceTops fullTops, int surfaceIndex, int downShift) {
        ArrayList<Move> moves = new ArrayList<>();

        Tetromino active = matrix.getActive();
        ArrayList<PresetMove> typeKeys = cycleKeys.get(active);
        if (typeKeys != null) {
            for (PresetMove typeKey : typeKeys) {
                attemptAppend(typeKey, matrix, surfaceIndex, downShift, moves);
            }
        }

        return moves;
    }

    private void attemptAppend(PresetMove key, Matrix matrix,
            int surfaceIndex, int downShift, ArrayList<Move> moves) {
        if (GridModifier.isFree(localGrid, key.getGrid())
                && GridModifier.isFilled(localGrid, key.getFill())) {
            boolean softDrop;
            if (GridModifier.isFree(localGrid, key.getFreeHard())) {
                softDrop = false;
            } else if (key.getFreeSoft() != null) {
                long[][] sfgrid = new long[][]{
                    {key.getFreeSoft()[Matrix.LEFT_SURFACE_INDEX], 0L},
                    {key.getFreeSoft()[Matrix.MID_SURFACE_INDEX], 0L},
                    {key.getFreeSoft()[Matrix.RIGHT_SURFACE_INDEX], 0L}};
                int shift = base - downShift;
                sfgrid = shift < 0 ? GridModifier.shiftDown(sfgrid, -shift)
                        : GridModifier.shiftUp(sfgrid, shift);
                if (GridModifier.isFree(matrix.getGrid(), sfgrid)) {
                    softDrop = true;
                } else {
                    return;
                }
            } else {
                return;
            }

            long[][] grid = new long[][]{
                {key.getGrid()[Matrix.LEFT_SURFACE_INDEX], 0L},
                {key.getGrid()[Matrix.MID_SURFACE_INDEX], 0L},
                {key.getGrid()[Matrix.RIGHT_SURFACE_INDEX], 0L}};
            int shift = base - downShift;
            grid = shift < 0 ? GridModifier.shiftDown(grid, -shift)
                    : GridModifier.shiftUp(grid, shift);

            PlayingForeverMove move
                    = generateClearedMove(grid, surfaceIndex, key, softDrop,
                            matrix);
            moves.add(move);
        }
    }

    public PlayingForeverMove generateClearedMove(long[][] grid,
            int surfaceIndex, PresetMove key, boolean softDrop, Matrix matrix) {
        GridModifier.set(matrix.getGrid(), grid);

        int clearFrom = 0;
        while (clearFrom < Matrix.MAX_PROCESS_HEIGHT
                && !GridModifier.isRowPrint(matrix.getGrid(), 0b1111111111L,
                        clearFrom)) {
            clearFrom++;
        }

        int clearTo = -1;
        if (clearFrom < Matrix.MAX_PROCESS_HEIGHT) {
            clearTo = clearFrom - 1;
            while (GridModifier.isRowPrint(matrix.getGrid(), 0b1111111111L,
                    clearTo + 1)) {
                clearTo++;
            }
        }

        GridModifier.clear(matrix.getGrid(), grid);
        return clearTo == -1
                ? new PlayingForeverMove(grid, surfaceIndex, matrix.getActive(),
                        key, matrix.isHoldUsed(), softDrop ? key.getSoftDrop() : null)
                : new PlayingForeverMove(grid, surfaceIndex, matrix.getActive(),
                        key, matrix.isHoldUsed(), softDrop ? key.getSoftDrop() : null,
                        clearFrom, clearTo);
    }

    public void updateMove(Matrix matrix, Move move) {
        PlayingForeverMove pfMove = (PlayingForeverMove) move;
        GridModifier.setOrGridHalf(localGrid, pfMove.getCycleKey().getGrid());
        made++;
    }

    public void revertMove(Matrix matrix, Move move) {
        PlayingForeverMove pfMove = (PlayingForeverMove) move;
        GridModifier.clearOrGridHalf(localGrid, pfMove.getCycleKey().getGrid());
        made--;
    }

    public int getBase() {
        return base;
    }

    public void setBase(int base) {
        this.base = base;
    }

    public int getHeight() {
        return height;
    }

    public long[] getLocalGrid() {
        return localGrid;
    }

    public boolean isEmpty() {
        for (long pgrid : localGrid) {
            if (pgrid != 0L) {
                return false;
            }
        }

        return true;
    }

    public boolean isTarget() {
        return GridModifier.isFilled(localGrid, targetGrid);
    }

    public void fill() {
        System.arraycopy(targetGrid, 0, localGrid, 0, localGrid.length);
        made = total;
    }

    public void clear() {
        for (int i = 0; i < localGrid.length; i++) {
            localGrid[i] = 0L;
        }
        made = 0;
    }

    @Override
    public String toString() {
        String str = "[";
        for (Tetromino t : cycleKeys.keySet()) {
            str += cycleKeys.get(t).toString();
        }
        return str + "]";
    }

    public int getTotal() {
        return total;
    }

    public int getMade() {
        return made;
    }
}
