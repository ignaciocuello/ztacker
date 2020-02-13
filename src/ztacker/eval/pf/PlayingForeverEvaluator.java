package ztacker.eval.pf;

import java.util.LinkedList;
import ztacker.chooser.MoveChooser;
import ztacker.eval.Evaluator;
import ztacker.history.MoveHistory;
import ztacker.history.pf.PlayingForeverMoveHistory;
import ztacker.matrix.Matrix;
import ztacker.move.FullSurfaceTops;
import ztacker.move.MoveGenerator;
import ztacker.move.pf.JLOSurfaceMoveGenerator;
import ztacker.move.pf.PlayingForeverMoveGenerator;
import ztacker.move.pf.STZSurfaceMoveGenerator;
import ztacker.tetromino.Tetromino;

public final class PlayingForeverEvaluator extends Evaluator {

    private static final int OPENNESS_DEPTH = 2;

    private static final int FLOATING_WEIGHT = -100_000;

    private static final int FULL_CYCLE_WEIGHT = 10_000;
    private static final int INDEX_WEIGHT = 2_000;
    private static final int MADE_WEIGHT = 100;

    private static final int EXTERNAL_BORDER_WEIGHT = -1_000;

    private static final int TETRIS_WEIGHT = 50;

    @Override
    public int evaluate(
            Matrix matrix, MoveHistory history,
            LinkedList<Tetromino> remainder, MoveGenerator generator,
            FullSurfaceTops fullTops, MoveChooser chooser) {
        PlayingForeverMoveGenerator pfGenerator
                = (PlayingForeverMoveGenerator) generator;
        PlayingForeverMoveHistory pfHistory
                = (PlayingForeverMoveHistory) history;

        int value = 0;
        value += getNumFloating(fullTops.getMidTops(),
                matrix.getGrid()[Matrix.MID_SURFACE_INDEX][0])
                * FLOATING_WEIGHT;

        STZSurfaceMoveGenerator stzGen = pfGenerator.getSTZGenerator();
        JLOSurfaceMoveGenerator jloGen = pfGenerator.getJLOGenerator();

        value += stzGen.getMade() * FULL_CYCLE_WEIGHT
                + stzGen.getIndex() * INDEX_WEIGHT
                + stzGen.getCurrentCycle().getMade() * MADE_WEIGHT;
        value += jloGen.getMade() * FULL_CYCLE_WEIGHT
                + jloGen.getIndex() * INDEX_WEIGHT
                + jloGen.getCurrentCycle().getMade() * MADE_WEIGHT;
        value += getExternalBorderDifferential(fullTops)
                * EXTERNAL_BORDER_WEIGHT;
        value += pfHistory.getNumTetrises() * TETRIS_WEIGHT;

        return value;
    }

    public int getExternalBorderDifferential(FullSurfaceTops fullTops) {
        return Math.max(
                Math.max(fullTops.getLeftMax(), fullTops.getRightMax()),
                fullTops.getMidTops()[2])
                - Math.min(Math.min(fullTops.getLeftMin(),
                        fullTops.getRightMin()),
                        fullTops.getMidTops()[2]);
    }

    public int getNumFloating(int[] mtops, long mbot) {
        int numf = 0;
        if (mtops[0] > 0 && (mbot & 0b1000L) == 0L) {
            numf += mtops[0];
        }
        if (mtops[1] > 0 && (mbot & 0b0100L) == 0L) {
            numf += mtops[1];
        }
        return numf;
    }

    @Override
    public int calculateOpenness(Matrix matrix, MoveHistory history,
            LinkedList<Tetromino> remainder, MoveGenerator generator,
            FullSurfaceTops fullTops, MoveChooser chooser) {
        return super.calculateOpenness(matrix, history, remainder, generator,
                fullTops, chooser, OPENNESS_DEPTH);
    }
}
