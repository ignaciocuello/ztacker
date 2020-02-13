package ztacker.eval;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import ztacker.chooser.MoveChooser;
import ztacker.history.MoveHistory;
import ztacker.matrix.Matrix;
import ztacker.move.FullSurfaceTops;
import ztacker.move.Move;
import ztacker.move.MoveGenerator;
import ztacker.tetromino.Tetromino;

public abstract class Evaluator {

    private static final int HOLD_WEIGHT = -10;
    private static final int SOFT_DROP_WEIGHT = -1000;

    public abstract int evaluate(
            Matrix matrix, MoveHistory history,
            LinkedList<Tetromino> remainder, MoveGenerator generator,
            FullSurfaceTops fullTops, MoveChooser chooser);
    
    public abstract int calculateOpenness(Matrix matrix, MoveHistory history, 
            LinkedList<Tetromino> remainder, MoveGenerator generator, 
            FullSurfaceTops fullTops, MoveChooser chooser);
    
    public int calculateOpenness(
            Matrix matrix, MoveHistory history,
            LinkedList<Tetromino> remainder, MoveGenerator generator,
            FullSurfaceTops fullTops, MoveChooser chooser, int depth) {
        int openness = 0;
        for (Tetromino active : remainder) {
            LinkedList<Tetromino> subRemainder
                    = subtractFromRemainder(remainder, active);
            int local = calcLocalOpenness(active, matrix, history,
                    subRemainder, generator, fullTops, chooser, depth);

            matrix.setHoldUsed(true);

            Tetromino held = matrix.getHeld();
            matrix.setHeld(active);
            if (held == null) {
                int totalHoldLocal = 0;
                for (Tetromino heldType : subRemainder) {
                    LinkedList<Tetromino> subHeldRemainder
                            = subtractFromRemainder(subRemainder, heldType);

                    int holdLocal = calcLocalOpenness(heldType, matrix,
                            history, subHeldRemainder, generator, fullTops,
                            chooser, depth);
                    if (local == 0 && holdLocal == 0) {
                        matrix.setActive(null);
                        matrix.setHeld(held);
                        matrix.setHoldUsed(false);
                        return 0;
                    }
                    totalHoldLocal += holdLocal;
                }
                local += totalHoldLocal;
            } else {
                int holdLocal = calcLocalOpenness(held, matrix, history,
                        subRemainder, generator, fullTops,
                        chooser, depth);
                if (local == 0 && holdLocal == 0) {
                    matrix.setActive(null);
                    matrix.setHeld(held);
                    matrix.setHoldUsed(false);
                    return 0;
                }
                local += holdLocal;
            }
            matrix.setHeld(held);
            matrix.setHoldUsed(false);

            if (local == 0) {
                return 0;
            } else {
                openness += local;
            }
        }
 
        matrix.setActive(null);
        return openness;
    }

    private int calcLocalOpenness(
            Tetromino active, Matrix matrix, MoveHistory history,
            LinkedList<Tetromino> remainder, MoveGenerator generator,
            FullSurfaceTops fullTops, MoveChooser chooser, int depth) {
        int local = 0;

        matrix.setActive(active);
        ArrayList<Move> moves
                = generator.generateMoves(matrix, history, fullTops);
        for (Move move : moves) {
            matrix.set(move.getGrid());
            if (move.isClear()) {
                matrix.clearRange(move.getClearFrom(),
                        move.getClearTo());
            }
            matrix.setHoldUsed(false);
            chooser.updateAll(move, matrix);

            if (depth - 1 == 0) {
                local++;
            } else {
                local += calculateOpenness(matrix, history,
                            remainder, generator,
                            FullSurfaceTops.constructFullSurfaceTop(matrix),
                            chooser, depth - 1);
            }
            chooser.revertAll(move, matrix);
            if (move.isClear()) {
                matrix.revertClearRange(move.getClearFrom(),
                        move.getClearTo());
            }
            matrix.clear(move.getGrid());
            matrix.setHoldUsed(move.isHold());
        }

        return local;
    }

    private LinkedList<Tetromino> subtractFromRemainder(
            LinkedList<Tetromino> remainder, Tetromino active) {
        LinkedList<Tetromino> sub = new LinkedList<>(remainder);

        sub.remove(active);
        if (sub.isEmpty()) {
            sub.addAll(Arrays.asList(Tetromino.values()));
        }

        return sub;
    }

    public int getSecondaryValue(MoveHistory history) {
        return history.getNumHolds() * HOLD_WEIGHT
                + history.getNumSoftDrops() * SOFT_DROP_WEIGHT;
    }
}
