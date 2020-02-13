package ztacker.eval.zt;

import java.util.LinkedList;
import ztacker.chooser.MoveChooser;
import ztacker.history.MoveHistory;
import ztacker.history.zt.ZTStackingMoveHistory;
import ztacker.matrix.Matrix;
import ztacker.move.zt.MidSurfaceMoveGenerator;
import ztacker.move.zt.ZTStackingMoveGenerator;
import ztacker.move.FullSurfaceTops;
import ztacker.move.MoveGenerator;
import ztacker.move.zt.ZTStackingMove;
import ztacker.tetromino.Tetromino;

public final class OpeningEvaluator extends ZTEvaluator {

    private static final int OPENNESS_DEPTH = 1;

    private static final int LEFT_SURFACE_DIFFERENTIAL_WEIGHT = -150;
    private static final int LEFT_MID_DIFFERENTIAL_WEIGHT = -400;

    private static final int FULL_OPENING_WEIGHT = 5_000;

    private static final int MID_FILLED_WEIGHT = 2_000;
    private static final long MID_FILL_PRINT = 0b1110_1110L;

    private static final int LS_IMBALANCE_WEIGHT = -3_500;

    private static final int FIRST_Z_QCT_WEIGHT = 450;

    @Override
    public int evaluate(
            Matrix matrix, MoveHistory history,
            LinkedList<Tetromino> remainder, MoveGenerator generator,
            FullSurfaceTops fullTops, MoveChooser chooser) {
        ZTStackingMoveHistory ztStackingHistory
                = (ZTStackingMoveHistory) history;
        ZTStackingMoveGenerator ztStackingGenerator
                = (ZTStackingMoveGenerator) generator;

        int val = (int) (ztStackingGenerator.getOpeningGenerator().
                getHighestPercentageOfCompletion() * FULL_OPENING_WEIGHT);
        val += getInternalLeftDiffernetial(fullTops)
                * LEFT_SURFACE_DIFFERENTIAL_WEIGHT;
        val += getExternalLeftMidDifferential(fullTops)
                * LEFT_MID_DIFFERENTIAL_WEIGHT;
        if (isFirstZQCT(ztStackingHistory)) {
            val += FIRST_Z_QCT_WEIGHT;
        }
        if (isLSImbalance(matrix, fullTops, history, remainder, generator)) {
            val += LS_IMBALANCE_WEIGHT;
        }
        if (isMidFill(matrix.getGrid())) {
            val += MID_FILLED_WEIGHT;
        }

        return val;
    }

    public boolean isJZImbalance(MidSurfaceMoveGenerator centerGenerator,
            ZTStackingMoveHistory history) {
        int numj = history.getTypedHistory().get(Tetromino.J_TYPE).size();
        int numz = history.getTypedHistory().get(Tetromino.Z_TYPE).size();

        return numj != numz;
    }

    public boolean isFirstZQCT(ZTStackingMoveHistory history) {
        return !history.getTypedHistory().get(Tetromino.Z_TYPE).isEmpty()
                && ((ZTStackingMove) history.getTypedHistory().get(
                        Tetromino.Z_TYPE).get(0)).getSurfaceIndex()
                != Matrix.RIGHT_SURFACE_INDEX;
    }

    public boolean isMidFill(long[][] grid) {
        return (grid[Matrix.MID_SURFACE_INDEX][0] & MID_FILL_PRINT)
                == MID_FILL_PRINT;
    }
    
    @Override
    public int calculateOpenness(Matrix matrix, MoveHistory history,
            LinkedList<Tetromino> remainder, MoveGenerator generator,
            FullSurfaceTops fullTops, MoveChooser chooser) {
        return calculateOpenness(matrix, history, remainder,
                generator, fullTops, chooser, OPENNESS_DEPTH);
    }
}
