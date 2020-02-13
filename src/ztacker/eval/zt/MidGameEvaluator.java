package ztacker.eval.zt;

import java.util.LinkedList;
import ztacker.chooser.MoveChooser;
import ztacker.history.MoveHistory;
import ztacker.matrix.GridModifier;
import ztacker.matrix.Matrix;
import ztacker.move.FullSurfaceTops;
import ztacker.move.MoveGenerator;
import ztacker.move.zt.UnboundedMidMap;
import ztacker.tetromino.Tetromino;

public final class MidGameEvaluator extends ZTEvaluator {

    private static final int OPENNESS_DEPTH = 1;

    private static final int RIGHT_MID_HEIGHT = 9;

    private static final int LEFT_SURFACE_DIFFERENTIAL_WEIGHT = -150;
    private static final int LEFT_MID_DIFFERENTIAL_WEIGHT = -400;

    private static final int RESERVE_LEFT_I_WEIGHT = -5_500;
    private static final int RESERVE_RIGHT_I_WEIGHT = 5_500;

    private static final int LS_IMBALANCE_WEIGHT = -7_500;

    private static final int EXCESS_HEIGHT_WEIGHT = -50_000;

    private static final int RIGHT_I_MIN_HEIGHT = 9;

    private static final int MAX_LEFT_RISKY_HEIGHT = 18;
    private static final int MAX_MID_RISKY_HEIGHT = 17;

    private static final int FLAT_MID_WEIGHT = 10_000;
    private static final double FLAT_MID_PROBABILITY = 1.0 / 3.0;

    private static final int UNBALANCED_LEFT_SURFACE_WEIGHT = -6_000;

    private static final int LEFT_EXCESS_I_WEIGHT = -30_000;
    private static final int LEFT_EXCESS_HEIGHT = 3;

    @Override
    public int evaluate(
            Matrix matrix, MoveHistory history,
            LinkedList<Tetromino> remainder, MoveGenerator generator,
            FullSurfaceTops fullTops, MoveChooser chooser) {
        int height = fullTops.getMax() - 1;
        int val = 0;

        int ild = getInternalLeftDiffernetial(fullTops);
        val += ild * LEFT_SURFACE_DIFFERENTIAL_WEIGHT;

        int eld = getExternalLeftMidDifferential(fullTops);
        val += eld * LEFT_MID_DIFFERENTIAL_WEIGHT;

        if (hasReserveLeftI(matrix.getGrid())) {
            val += RESERVE_LEFT_I_WEIGHT;
        }

        boolean resRightI
                = hasReserveRightI(matrix.getGrid(), height);
        boolean resRightZ
                = fullTops.getRightTops()[1] < fullTops.getRightTops()[2];
        if (height > RIGHT_I_MIN_HEIGHT && resRightI) {
            val += RESERVE_RIGHT_I_WEIGHT;
        }
        if (isLSImbalance(matrix, fullTops, history, remainder, generator)) {
            val += LS_IMBALANCE_WEIGHT;
        }
        if (isExcessHeight(fullTops, matrix.getHeld(), resRightI, resRightZ)) {
            val += EXCESS_HEIGHT_WEIGHT;
        }
        if (isFlatMid(fullTops.getLeftTops(), fullTops.getMidTops(),
                matrix, remainder)) {
            val += FLAT_MID_WEIGHT;
        }
        if (isUnbalancedLeftSurface(fullTops.getLeftTops())) {
            val += UNBALANCED_LEFT_SURFACE_WEIGHT;
        }
        int lei = getNumLeftExcessI(fullTops.getLeftTops());
        val += LEFT_EXCESS_I_WEIGHT * lei;

        return val;
    }

    private int getNumLeftExcessI(int[] ltops) {
        int numLeftExcessI = 0;
        if ((ltops[1] - ltops[0]) >= LEFT_EXCESS_HEIGHT) {
            numLeftExcessI++;
        }
        if ((ltops[2] - ltops[3]) >= LEFT_EXCESS_HEIGHT) {
            numLeftExcessI++;
        }

        return numLeftExcessI;
    }

    private boolean hasReserveRightI(long[][] grid, int height) {
        return height >= RIGHT_MID_HEIGHT
                && GridModifier.isRowPrint(grid, 0b0000000001L, 3)
                && GridModifier.isRowPrint(grid, 0b0000000001L, 2)
                && GridModifier.isRowPrint(grid, 0b0000000001L, 1)
                && GridModifier.isRowPrint(grid, 0b0000000001L, 0);
    }

    private boolean hasReserveLeftI(long[][] grid) {
        return GridModifier.isRowPrint(grid, 0b0000000100L, 3)
                && GridModifier.isRowPrint(grid, 0b0000000100L, 2)
                && GridModifier.isRowPrint(grid, 0b0000000100L, 1)
                && GridModifier.isRowPrint(grid, 0b0000000100L, 0);
    }

    private boolean isFlatMid(int[] ltops, int[] mtops,
            Matrix matrix, LinkedList<Tetromino> remainder) {
        return (mtops[0] == mtops[1] && mtops[1] == mtops[2])
                || specificPieceProbability(matrix,
                        UnboundedMidMap.getCappingTetromino(ltops, mtops),
                        remainder) >= FLAT_MID_PROBABILITY;
    }

    private double specificPieceProbability(Matrix matrix, Tetromino type,
            LinkedList<Tetromino> remainder) {
        double pr = matrix.getHeld() == type ? 1.0 : 0.0;
        return remainder.contains(type) ? pr + 1.0 / remainder.size() : pr;
    }

    private boolean isExcessHeight(FullSurfaceTops fullTops, Tetromino held,
            boolean resRightI, boolean resRightZ) {
        return (fullTops.getLeftMax() > MAX_LEFT_RISKY_HEIGHT
                || fullTops.getMidMax() > MAX_MID_RISKY_HEIGHT)
                && !(resRightI && held == Tetromino.I_TYPE)
                && !(resRightZ && held == Tetromino.T_TYPE);
    }

    private boolean isUnbalancedLeftSurface(int[] leftTops) {
        return (leftTops[0] != leftTops[1])
                && (leftTops[1] != leftTops[2])
                && (leftTops[2] != leftTops[3]);
    }

    @Override
    public int calculateOpenness(Matrix matrix, MoveHistory history,
            LinkedList<Tetromino> remainder, MoveGenerator generator,
            FullSurfaceTops fullTops, MoveChooser chooser) {
        return calculateOpenness(matrix, history, remainder,
                generator, fullTops, chooser, OPENNESS_DEPTH);
    }
}
