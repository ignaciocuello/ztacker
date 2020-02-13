package ztacker.history.zt;

import java.util.HashMap;
import java.util.LinkedList;
import ztacker.history.MoveHistory;
import ztacker.matrix.Matrix;
import ztacker.move.Move;
import ztacker.move.zt.ZTStackingMove;
import ztacker.tetromino.Tetromino;

public final class ZTStackingMoveHistory extends MoveHistory {

    public static final int OPENING_INDEX = 3;

    private final HashMap<Tetromino, LinkedList<Move>>[] surfacedHistory;

    private int numTSpins;

    public ZTStackingMoveHistory() {
        /*+1 for opening index */
        surfacedHistory = new HashMap[Matrix.NUM_PARTIAL_GRIDS + 1];
        for (int i = 0; i < surfacedHistory.length; i++) {
            surfacedHistory[i] = new HashMap<>();
            for (Tetromino t : Tetromino.values()) {
                surfacedHistory[i].put(t, new LinkedList<>());
            }
        }
    }

    @Override
    public void updateMove(Move move, Matrix matrix) {
        super.updateMove(move, matrix);
        ZTStackingMove ztStackingMove = (ZTStackingMove) move;

        if (move.getType() == Tetromino.T_TYPE
                && ztStackingMove.getSurfaceIndex()
                == Matrix.RIGHT_SURFACE_INDEX) {
            numTSpins++;
        }

        surfacedHistory[ztStackingMove.getSurfaceIndex()]
                .get(move.getType()).add(move);
    }

    @Override
    public void revertMove(Move move, Matrix matrix) {
        super.revertMove(move, matrix);
        ZTStackingMove ztStackingMove = (ZTStackingMove) move;

        if (move.getType() == Tetromino.T_TYPE
                && ztStackingMove.getSurfaceIndex()
                == Matrix.RIGHT_SURFACE_INDEX) {
            numTSpins--;
        }

        surfacedHistory[ztStackingMove.getSurfaceIndex()].get(move.getType())
                .removeLastOccurrence(move);
    }

    public HashMap<Tetromino, LinkedList<Move>>[] getSurfacedHistory() {
        return surfacedHistory;
    }

    public int getNumTSpins() {
        return numTSpins;
    }
}
