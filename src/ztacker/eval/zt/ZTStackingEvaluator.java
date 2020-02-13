package ztacker.eval.zt;

import java.util.LinkedList;
import ztacker.chooser.MoveChooser;
import ztacker.eval.Evaluator;
import ztacker.history.MoveHistory;
import ztacker.history.zt.ZTStackingMoveHistory;
import ztacker.matrix.Matrix;
import ztacker.move.FullSurfaceTops;
import ztacker.move.MoveGenerator;
import ztacker.move.zt.ZTStackingMoveGenerator;
import ztacker.tetromino.Tetromino;

public final class ZTStackingEvaluator extends Evaluator {

    private final OpeningEvaluator openingEvaluator;
    private final MidGameEvaluator midGameEvaluator;

    public ZTStackingEvaluator() {
        openingEvaluator = new OpeningEvaluator();
        midGameEvaluator = new MidGameEvaluator();
    }

    @Override
    public int evaluate(
            Matrix matrix, MoveHistory history,
            LinkedList<Tetromino> remainder, MoveGenerator generator,
            FullSurfaceTops fullTops, MoveChooser chooser) {
        return isOpening(history, generator)
                ? openingEvaluator.evaluate(matrix, history,
                        remainder, generator, fullTops, chooser)
                : midGameEvaluator.evaluate(matrix, history,
                        remainder, generator, fullTops, chooser);
    }

    @Override
    public int calculateOpenness(Matrix matrix, MoveHistory history,
            LinkedList<Tetromino> remainder, MoveGenerator generator,
            FullSurfaceTops fullTops, MoveChooser chooser) {
        return isOpening(history, generator)
                ? openingEvaluator.calculateOpenness(
                        matrix, history, remainder, generator, fullTops, chooser)
                : midGameEvaluator.calculateOpenness(
                        matrix, history, remainder, generator, fullTops, chooser);
    }

    private boolean isOpening(MoveHistory history, MoveGenerator generator) {
        return ((ZTStackingMoveHistory) history).getNumTSpins() == 0
                || !((ZTStackingMoveGenerator) generator)
                .getOpeningGenerator().isOpeningLock();
    }
}
