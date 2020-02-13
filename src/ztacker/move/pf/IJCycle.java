package ztacker.move.pf;

import java.util.ArrayList;
import java.util.HashMap;
import ztacker.move.PresetMove;
import ztacker.matrix.GridModifier;
import ztacker.matrix.Matrix;
import ztacker.move.FullSurfaceTops;
import ztacker.move.Move;
import ztacker.tetromino.Tetromino;

public final class IJCycle extends PFCycle {

    private static final int HEIGHT = 8;
    private static final int TOTAL = 4;
    
    private static final long[] DEFAULT_LEFT_I
            = new long[]{0b1000_1000_1000_1000L, 0L};
    private static final long[] DEFAULT_RIGHT_I
            = new long[]{0b0100_0100_0100_0100L, 0L};
    private static final long[] DEFAULT_UPRIGHT_J
            = new long[]{0b0100_0100_1100, 0L};
    private static final long[] DEFAULT_INVERTED_J
            = new long[]{0b1100_1000_1000, 0L};

    private final PresetMove leftI;
    private boolean leftIGrid;

    private final PresetMove rightI;
    private boolean rightIGrid;

    private final PresetMove uprightJ;
    private boolean uprightJGrid;

    private final PresetMove invertedJ;
    private boolean invertedJGrid;

    private final boolean closing;

    public IJCycle(boolean closing) {
        super(new HashMap<>(), TOTAL);
        this.closing = closing;

        leftI = new PresetMove(new long[Matrix.NUM_PARTIAL_GRIDS],
                Tetromino.I_TYPE);
        rightI = new PresetMove(new long[Matrix.NUM_PARTIAL_GRIDS],
                Tetromino.I_TYPE);

        uprightJ = new PresetMove(new long[Matrix.NUM_PARTIAL_GRIDS],
                Tetromino.J_TYPE);
        invertedJ = new PresetMove(new long[Matrix.NUM_PARTIAL_GRIDS],
                Tetromino.J_TYPE);
    }

    @Override
    public ArrayList<Move> getValidMoves(Matrix matrix,
            FullSurfaceTops fullTops, int surfaceIndex, int downShift) {
        ArrayList<Move> moves = new ArrayList<>();

        Tetromino active = matrix.getActive();
        switch (active) {
            case I_TYPE:
                if (!leftIGrid && uprightJGrid && rightIGrid) {
                    if (closing && invertedJGrid) {
                        long[] pgrid = GridModifier.shiftUp(DEFAULT_RIGHT_I,
                                fullTops.getMidTops()[1]);
                        addMove(pgrid, surfaceIndex, matrix, leftI, moves);
                    }
                    long[] pgrid = GridModifier.shiftUp(DEFAULT_LEFT_I,
                            fullTops.getMidTops()[0]);
                    addMove(pgrid, surfaceIndex, matrix, leftI, moves);
                }
                if (!rightIGrid) {
                    long[] pgrid = GridModifier.shiftUp(DEFAULT_RIGHT_I,
                            fullTops.getMidTops()[1]);
                    addMove(pgrid, surfaceIndex, matrix, rightI, moves);
                }
                break;
            case J_TYPE:
                if (!uprightJGrid) {
                    int base = Math.max(fullTops.getMidTops()[0],
                            fullTops.getMidTops()[1]);
                    long[] pgrid = GridModifier.shiftUp(DEFAULT_UPRIGHT_J, base);
                    addMove(pgrid, surfaceIndex, matrix, uprightJ, moves);
                }
                if (!invertedJGrid && uprightJGrid && rightIGrid) {
                    long[] pgrid = GridModifier.shiftUp(DEFAULT_INVERTED_J,
                            fullTops.getMidTops()[1]
                            - fullTops.getMidTops()[0] > 2
                                    ? fullTops.getMidTops()[1] - 2
                                    : fullTops.getMidTops()[0]);
                    addMove(pgrid, surfaceIndex, matrix, invertedJ, moves);
                }
                break;
            default:
                break;
        }

        return moves;
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
        PlayingForeverMove pfMove = (PlayingForeverMove) move;

        if (pfMove.getCycleKey() == leftI) {
            leftIGrid = true;
        } else if (pfMove.getCycleKey() == rightI) {
            rightIGrid = true;
        } else if (pfMove.getCycleKey() == uprightJ) {
            uprightJGrid = true;
        } else if (pfMove.getCycleKey() == invertedJ) {
            invertedJGrid = true;
        }
        
        made++;
    }

    @Override
    public void revertMove(Matrix matrix, Move move) {
        PlayingForeverMove pfMove = (PlayingForeverMove) move;

        if (pfMove.getCycleKey() == leftI) {
            leftIGrid = false;
        } else if (pfMove.getCycleKey() == rightI) {
            rightIGrid = false;
        } else if (pfMove.getCycleKey() == uprightJ) {
            uprightJGrid = false;
        } else if (pfMove.getCycleKey() == invertedJ) {
            invertedJGrid = false;
        }
        
        made--;
    }

    @Override
    public boolean isEmpty() {
        return !leftIGrid && !rightIGrid && !uprightJGrid && !invertedJGrid;
    }

    @Override
    public boolean isTarget() {
        return leftIGrid && rightIGrid && uprightJGrid && invertedJGrid;
    }

    @Override
    public void fill() {
        leftIGrid = rightIGrid = uprightJGrid = invertedJGrid = true;
        made = getTotal();
    }

    @Override
    public void clear() {
        leftIGrid = rightIGrid = uprightJGrid = invertedJGrid = false;
        made = 0;
    }

    @Override
    public int getHeight() {
        return HEIGHT;
    }
}
