package ztacker.move.pf;

import java.util.ArrayList;
import ztacker.matrix.GridModifier;
import ztacker.matrix.Matrix;
import ztacker.move.FullSurfaceTops;
import ztacker.move.Move;
import ztacker.move.PresetMove;
import ztacker.tetromino.Tetromino;

public final class IICycle extends PFCycle {

    private static final int HEIGHT = 4;

    private static final long[] DEFAULT_LEFT_I
            = new long[]{0b1000_1000_1000_1000L, 0L};

    private final PresetMove bottomI;
    private boolean bottomIGrid;

    private final PresetMove topI;
    private boolean topIGrid;

    private boolean alternate;

    public IICycle(PFCycle iLoop) {
        super(iLoop);
        bottomI = new PresetMove(new long[Matrix.NUM_PARTIAL_GRIDS],
                Tetromino.I_TYPE);
        topI = new PresetMove(new long[Matrix.NUM_PARTIAL_GRIDS],
                Tetromino.I_TYPE);
    }

    @Override
    public ArrayList<Move> getValidMoves(Matrix matrix,
            FullSurfaceTops fullTops, int surfaceIndex, int downShift) {
        if (isEmpty()) {
            alternate
                    = fullTops.getMidTops()[1] - fullTops.getMidTops()[0] >= 4;
            clear();
        }

        if (alternate) {
            ArrayList<Move> moves = new ArrayList<>();

            Tetromino active = matrix.getActive();
            if (active == Tetromino.I_TYPE) {
                long[] pgrid
                        = GridModifier.shiftUp(DEFAULT_LEFT_I,
                                fullTops.getMidTops()[0]);
                if (!bottomIGrid) {
                    addMove(pgrid, surfaceIndex, matrix, bottomI, moves);
                } else if (!topIGrid) {
                    addMove(pgrid, surfaceIndex, matrix, topI, moves);
                }
            }
            return moves;
        } else {
            return super.getValidMoves(matrix, fullTops, surfaceIndex, downShift);
        }
    }

    private void addMove(long[] pgrid, int surfaceIndex, Matrix matrix,
            PresetMove key, ArrayList<Move> moves) {
        PlayingForeverMove move
                = generateClearedMove(convertToGrid(pgrid), surfaceIndex,
                        key, false, matrix);
        moves.add(move);
    }

    private long[][] convertToGrid(long[] pgrid) {
        return new long[][]{
            new long[2],
            pgrid,
            new long[2]
        };
    }

    @Override
    public void updateMove(Matrix matrix, Move move) {
        if (alternate) {
            PlayingForeverMove pfMove = (PlayingForeverMove) move;

            if (pfMove.getCycleKey() == topI) {
                topIGrid = true;
            } else if (pfMove.getCycleKey() == bottomI) {
                bottomIGrid = true;
            }
            made++;
        } else {
            super.updateMove(matrix, move);
        }
    }

    @Override
    public void revertMove(Matrix matrix, Move move) {
        if (alternate) {
            PlayingForeverMove pfMove = (PlayingForeverMove) move;

            if (pfMove.getCycleKey() == topI) {
                topIGrid = false;
            } else if (pfMove.getCycleKey() == bottomI) {
                bottomIGrid = false;
            }
            made--;
        } else {
            super.revertMove(matrix, move);
        }
    }

    @Override
    public boolean isEmpty() {
        return alternate ? !bottomIGrid && !topIGrid : super.isEmpty();
    }

    @Override
    public boolean isTarget() {
        return alternate ? bottomIGrid && topIGrid : super.isTarget();
    }

    @Override
    public void fill() {
        if (alternate) {
            bottomIGrid = topIGrid = true;
            made = getTotal();
        } else {
            super.fill();
        }
    }

    @Override
    public void clear() {
        if (alternate) {
            bottomIGrid = topIGrid = false;
            made = 0;
        } else {
            super.clear();
        }
    }

    @Override
    public int getHeight() {
        return HEIGHT;
    }

    public void setAlternate(boolean alternate) {
        this.alternate = alternate;
    }
}
