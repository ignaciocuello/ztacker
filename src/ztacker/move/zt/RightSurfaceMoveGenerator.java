package ztacker.move.zt;

import ztacker.move.FullSurfaceTops;
import ztacker.move.PartialSurfaceMoveGenerator;
import java.util.ArrayList;
import ztacker.history.MoveHistory;
import ztacker.matrix.GridModifier;
import ztacker.matrix.Matrix;
import ztacker.move.Move;
import ztacker.move.MoveGenerator;
import ztacker.move.SoftDropCommands;
import ztacker.robot.out.Command;
import ztacker.tetromino.Tetromino;

public final class RightSurfaceMoveGenerator extends PartialSurfaceMoveGenerator {

    public static final int LMZ_DOUBLE_LOCK_THRESHOLD = 50;
    public static final int RESET_LOCK_THRESHOLD = 180;

    private int lmt;
    private int lmz;

    private boolean lmtDoubleLock;
    private boolean lmzSingleLock;
    private boolean lmzDoubleLock;

    private int prevMovesAfterReset;
    private int movesAfterReset;

    public RightSurfaceMoveGenerator() {
        resetLocks();
    }

    @Override
    public ArrayList<Move> getMoveList(Matrix matrix,
            FullSurfaceTops fullTops, MoveHistory history,
            MoveGenerator generator) {
        ArrayList<Move> moves = new ArrayList<>();

        int[] ltops = fullTops.getLeftTops();
        int[] mtops = fullTops.getMidTops();
        int[] rtops = fullTops.getRightTops();

        switch (matrix.getActive()) {
            case Z_TYPE:
                processZMoves(ltops, mtops, rtops, matrix.isHoldUsed(),
                        moves);
                break;
            case T_TYPE:
                processTMoves(ltops, mtops, rtops,
                        matrix.getGrid(), matrix.isHoldUsed(), moves, 
                        history);
                break;
            case I_TYPE:
                processIMoves(rtops, matrix.getGrid(), matrix.isHoldUsed(),
                        moves);
                break;
            default:
                break;
        }

        return moves;
    }

    private void processZMoves(int[] ltops, int[] mtops, int[] rtops,
            boolean hold, ArrayList<Move> moves) {
        if (isZTypeValid(rtops)) {
            int height = rtops[1];
            if (height < Matrix.MAX_REAL_HEIGHT) {
                ZTStackingMove move = new ZTStackingMove(
                        new long[][]{
                            {0L, 0L},
                            {0L, 0L},
                            GridModifier.shiftUp(
                                    new long[]{0b0010_0110_0100L, 0L},
                                    height)
                        }, Matrix.RIGHT_SURFACE_INDEX,
                        Tetromino.Z_TYPE,
                        hold);
                moves.add(move);
            }
        }
    }

    private boolean isZTypeValid(int[] rtops) {
        return rtops[0] <= rtops[1] && rtops[2] - rtops[1] <= 0;
    }

    private void processTMoves(int[] ltops, int[] mtops, int[] rtops,
            long[][] matrix, boolean hold, ArrayList<Move> moves, 
            MoveHistory history) {
        if (isTSpinValid(mtops, rtops, history)) {
            int height = rtops[1] - 1;
            if (height < Matrix.MAX_REAL_HEIGHT) {
                ZTStackingMove move = new ZTStackingMove(
                        new long[][]{
                            {0L, 0L},
                            GridModifier.shiftUp(new long[]{0b0010_0000L, 0L},
                            height),
                            GridModifier.shiftUp(new long[]{0b1100_1000L, 0L},
                            height)
                        }, Matrix.RIGHT_SURFACE_INDEX,
                        Tetromino.T_TYPE,
                        hold,
                        new SoftDropCommands(new long[]{0L,0L,0b1000_1100_1000L}, 
                                Command.ROTATE_CW),
                        height,
                        height + 1);
                if (createsValidTPrint(matrix, move, height)) {
                    moves.add(move);
                }
            }
        }
    }

    private boolean isTSpinValid(int[] mtops, int[] rtops,
            MoveHistory history) {
        return history.getTypedHistory().get(Tetromino.T_TYPE).size() > 1
                && rtops[2] - rtops[1] == 1 && rtops[1] - rtops[0] >= 1
                && mtops[2] >= rtops[1];
    }

    private boolean createsValidTPrint(long[][] matrix, ZTStackingMove move, 
            int height) {
        GridModifier.set(matrix, move.getGrid());
        boolean valid = GridModifier.isRowPrint(matrix, 0b1111111111L, height + 1)
                && GridModifier.isRowPrint(matrix, 0b1111111111L, height);
        GridModifier.clear(matrix, move.getGrid());
        return valid;
    }

    private void processIMoves(int[] rtops, long[][] grid,
            boolean hold, ArrayList<Move> moves) {
        if (isIRightValid(rtops)) {
            int height = rtops[2];
            if (height < Matrix.MAX_REAL_HEIGHT) {
                long[][] mgrid = new long[][]{
                    {0L, 0L},
                    {0L, 0L},
                    GridModifier.shiftUp(
                    new long[]{0b0010_0010_0010_0010L, 0L},
                    height)
                };
                ZTStackingMove move = createsValidIClear(grid, mgrid, height)
                        ? new ZTStackingMove(mgrid, 
                                Matrix.RIGHT_SURFACE_INDEX,
                                Tetromino.I_TYPE, 
                                hold, 
                                null,
                                height, 
                                height+3)
                        : new ZTStackingMove(mgrid, 
                                Matrix.RIGHT_SURFACE_INDEX,
                                Tetromino.I_TYPE, 
                                hold);
                moves.add(move);
            }
        }

        if (isILeftValid(rtops)) {
            int height = rtops[0];
            if (height < Matrix.MAX_REAL_HEIGHT) {
                long[][] mgrid = new long[][]{
                    {0L, 0L},
                    {0L, 0L},
                    GridModifier.shiftUp(
                    new long[]{0b1000_1000_1000_1000L, 0L},
                    height)
                };
                ZTStackingMove move = createsValidIClear(grid, 
                        mgrid,
                        height)
                        ? new ZTStackingMove(mgrid, 
                                Matrix.RIGHT_SURFACE_INDEX,
                                Tetromino.I_TYPE, 
                                hold, 
                                null,
                                height, 
                                height+3)
                        : new ZTStackingMove(mgrid, 
                                Matrix.RIGHT_SURFACE_INDEX,
                                Tetromino.I_TYPE, 
                                hold);
                moves.add(move);
            }
        }
    }

    private boolean isIRightValid(int[] rtops) {
        return rtops[1] - rtops[2] >= 4;
    }

    private boolean isILeftValid(int[] rtops) {
        return rtops[1] - rtops[0] >= 5;
    }

    private boolean createsValidIClear(long[][] grid, 
            long[][] move, int height) {
        GridModifier.set(grid, move);
        boolean valid
                = GridModifier.isRowPrint(grid,
                        0b1111111111L, height + 3)
                && GridModifier.isRowPrint(grid,
                        0b1111111111L, height + 2)
                && GridModifier.isRowPrint(grid,
                        0b1111111111L, height + 1)
                && GridModifier.isRowPrint(grid,
                        0b1111111111L, height);
        GridModifier.clear(grid, move);
        return valid;
    }

    @Override
    public void update(Matrix matrix, Move move, MoveHistory history) {
        ZTStackingMove ztStackingMove = (ZTStackingMove) move;
        movesAfterReset++;

        if (ztStackingMove.getSurfaceIndex() != Matrix.RIGHT_SURFACE_INDEX) {
            if (move.getType() == Tetromino.T_TYPE) {
                lmt++;
            } else if (move.getType() == Tetromino.Z_TYPE) {
                lmz++;
            }
        }

        lmtDoubleLock = lmtDoubleLock || lmt == 2;

        lmzSingleLock = lmzSingleLock || lmz >= 1;

        lmzDoubleLock = movesAfterReset < LMZ_DOUBLE_LOCK_THRESHOLD || lmz == 2;

        if (movesAfterReset >= RESET_LOCK_THRESHOLD
                && lmtDoubleLock && lmzDoubleLock) {
            resetLocks();
        }
    }

    @Override
    public void revert(Matrix matrix, Move move, MoveHistory history) {
        ZTStackingMove ztStackingMove = (ZTStackingMove) move;
        if (movesAfterReset == 0
                && prevMovesAfterReset >= RESET_LOCK_THRESHOLD) {
            unresetLocks();
        }

        if (ztStackingMove.getSurfaceIndex() != Matrix.RIGHT_SURFACE_INDEX) {
            if (move.getType() == Tetromino.T_TYPE) {
                lmt--;
            } else if (move.getType() == Tetromino.Z_TYPE) {
                lmz--;
            }
        }

        movesAfterReset--;

        lmtDoubleLock = lmtDoubleLock && lmt == 2;

        lmzSingleLock = lmzSingleLock && lmz >= 1;
        lmzDoubleLock = movesAfterReset < LMZ_DOUBLE_LOCK_THRESHOLD || lmz == 2;
    }

    private void resetLocks() {
        lmtDoubleLock = false;
        lmzSingleLock = false;
        lmzDoubleLock = true;

        lmt = 0;
        lmz = 0;

        prevMovesAfterReset = movesAfterReset;
        movesAfterReset = 0;
    }

    private void unresetLocks() {
        lmtDoubleLock = true;
        lmzSingleLock = true;
        lmzDoubleLock = true;

        lmt = 2;
        lmz = 2;

        movesAfterReset = prevMovesAfterReset;
    }

    public boolean allowLMT() {
        return !lmtDoubleLock;
    }

    public boolean allowLMZ() {
        return !lmzSingleLock && lmz == 0 || !lmzDoubleLock && lmz == 1;
    }
}
