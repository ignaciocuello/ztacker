package ztacker.eval.zt;

import java.util.ArrayList;
import java.util.LinkedList;
import ztacker.eval.Evaluator;
import ztacker.history.MoveHistory;
import ztacker.matrix.Matrix;
import ztacker.move.FullSurfaceTops;
import ztacker.move.Move;
import ztacker.move.MoveGenerator;
import ztacker.move.zt.UnboundedMidMap;
import ztacker.tetromino.Tetromino;
import ztacker.move.zt.MidSurfaceDiscriminator;
import ztacker.move.zt.ZTStackingMove;

public abstract class ZTEvaluator extends Evaluator {

    public boolean isFlatCenter(FullSurfaceTops fullTops) {
        int[] ctops = fullTops.getMidTops();
        return ctops[0] == ctops[1] && ctops[1] == ctops[2];
    }

    public int getInternalLeftDiffernetial(FullSurfaceTops fullTops) {
        int leftMax = fullTops.getLeftMax();
        int[] leftTops = fullTops.getLeftTops();

        int differential = 0;
        for (int leftTop : leftTops) {
            int dif = leftMax - leftTop;
            differential += dif;
        }

        return differential;
    }

    public int getExternalLeftMidDifferential(FullSurfaceTops fullTops) {
        return Math.max(fullTops.getLeftMax(), fullTops.getMidMax())
                - Math.min(fullTops.getLeftMin(), fullTops.getMidMin());
    }

    public boolean isLSImbalance(Matrix matrix, FullSurfaceTops fullTops,
            MoveHistory history, LinkedList<Tetromino> remainder,
            MoveGenerator generator) {
        int[] qtops = fullTops.getLeftTops();
        int[] ctops = fullTops.getMidTops();

        int numl = history.getTypedHistory().get(Tetromino.L_TYPE).size();
        int nums = history.getTypedHistory().get(Tetromino.S_TYPE).size();

        Tetromino active = matrix.getActive();
        if (UnboundedMidMap.getSTop().getBaseIndex(qtops, ctops)
                != MidSurfaceDiscriminator.INVALID) {
            numl++;
        }

        int nexts = 0;
        int nextl = 0;
        if (remainder.contains(Tetromino.S_TYPE)) {
            nexts++;
        }
        if (remainder.contains(Tetromino.L_TYPE)) {
            nextl++;
        }
        if (matrix.getHeld() == Tetromino.S_TYPE) {
            nexts++;
        } else if (matrix.getHeld() == Tetromino.L_TYPE) {
            nextl++;
        }

        if (numl < nums && (nextl - nexts) == 1) {
            matrix.setActive(Tetromino.L_TYPE);
            if (!generator.generateMoves(matrix, history, fullTops).isEmpty()) {
                numl++;
            }
            matrix.setActive(active);
        } else if (nums < numl && (nexts - nextl) == 1) {
            matrix.setActive(Tetromino.S_TYPE);
            ArrayList<Move> moves
                    = generator.generateMoves(matrix, history, fullTops);
            if (!moves.isEmpty()) {
                if (moves.size() != 1
                        || ((ZTStackingMove) moves.get(0)).getSurfaceIndex()
                        != Matrix.MID_SURFACE_INDEX
                        || UnboundedMidMap.getFlatTop().getBaseIndex(qtops, ctops)
                        == MidSurfaceDiscriminator.INVALID) {
                    nums++;
                }
            }
            matrix.setActive(active);
        }

        return numl != nums;
    }
}
