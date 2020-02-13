package ztacker.history.pf;

import java.util.LinkedList;
import ztacker.history.MoveHistory;
import ztacker.matrix.Matrix;
import ztacker.move.Move;
import ztacker.move.pf.PlayingForeverMove;

public final class PlayingForeverMoveHistory extends MoveHistory {

    private final LinkedList<Move>[] surfacedHistory;
    private int numTetrises;

    public PlayingForeverMoveHistory() {
        surfacedHistory = new LinkedList[Matrix.NUM_PARTIAL_GRIDS];
        for (int i = 0; i < surfacedHistory.length; i++) {
            surfacedHistory[i] = new LinkedList<>();
        }
    }

    @Override
    public void updateMove(Move move, Matrix matrix) {
        super.updateMove(move, matrix);
        PlayingForeverMove pfMove = (PlayingForeverMove) move;
        if (move.isClear() && (move.getClearTo()-move.getClearFrom()+1) == 4) {
            numTetrises++;
        }
        surfacedHistory[pfMove.getSurfaceIndex()].add(move);
    }

    @Override
    public void revertMove(Move move, Matrix matrix) {
        super.revertMove(move, matrix);
        PlayingForeverMove pfMove = (PlayingForeverMove) move;
        if (move.isClear() && (move.getClearTo()-move.getClearFrom()+1) == 4) {
            numTetrises--;
        }

        surfacedHistory[pfMove.getSurfaceIndex()].removeLastOccurrence(move);
    }

    public LinkedList<Move>[] getSurfacedHistory() {
        return surfacedHistory;
    }
    
    public int getNumTetrises() {
        return numTetrises;
    }
}
