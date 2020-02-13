package ztacker.move.zt;

import java.util.ArrayList;
import ztacker.history.MoveHistory;
import ztacker.matrix.GridModifier;
import ztacker.matrix.Matrix;
import ztacker.move.MoveGenerator;
import ztacker.move.FullSurfaceTops;
import ztacker.move.Move;
import ztacker.move.PartialSurfaceMoveGenerator;
import ztacker.tetromino.Tetromino;

public final class MidSurfaceMoveGenerator
        extends PartialSurfaceMoveGenerator {

    private final OpeningMoveGenerator openingGenerator;

    private boolean midUnlock;

    public MidSurfaceMoveGenerator(OpeningMoveGenerator openingGenerator) {
        this.openingGenerator = openingGenerator;
    }

    @Override
    public ArrayList<Move> getMoveList(Matrix matrix,
            FullSurfaceTops fullTops, MoveHistory history,
            MoveGenerator generator) {
        ZTStackingMoveGenerator ztStackingGenerator
                = (ZTStackingMoveGenerator) generator;

        ArrayList<Move> moves = new ArrayList<>();
        Tetromino type = matrix.getActive();
        if (isInvalid(type, ztStackingGenerator)) {
            return moves;
        }

        int[] ltops = fullTops.getLeftTops();
        int[] mtops = fullTops.getMidTops();

        ArrayList<MidSurfacePair> surfacePairs
                = UnboundedMidMap.getUnboundedMidMapping(type);

        if (surfacePairs == null) {
            return moves;
        }

        for (MidSurfacePair pair : surfacePairs) {
            int height = pair.getDiscriminator().getBaseIndex(ltops, mtops);
            if (height != MidSurfaceDiscriminator.INVALID) {
                moves.add(generateMove(pair.getGrid(), height, type,
                        matrix.isHoldUsed()));
            }
        }

        return moves;
    }

    private boolean isInvalid(Tetromino type,
            ZTStackingMoveGenerator generator) {
        return !midUnlock
                || (type == Tetromino.T_TYPE
                && !generator.getRightGenerator().allowLMT())
                || (type == Tetromino.Z_TYPE
                && !generator.getRightGenerator().allowLMZ());
    }

    private ZTStackingMove generateMove(long[][] grid, int height,
            Tetromino type, boolean hold) {
        ZTStackingMove move = new ZTStackingMove(
                GridModifier.shiftUp(grid, height),
                Matrix.MID_SURFACE_INDEX, type, hold);
        return move;
    }

    @Override
    public void update(Matrix matrix, Move move, MoveHistory history) {
        midUnlock = midUnlock || openingGenerator.isOpeningLock();
    }

    @Override
    public void revert(Matrix matrix, Move move, MoveHistory history) {
        midUnlock = midUnlock && openingGenerator.isOpeningLock();
    }

    public boolean isMidUnlock() {
        return midUnlock;
    }
}
