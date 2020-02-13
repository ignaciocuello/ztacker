package ztacker.history;

import java.util.HashMap;
import java.util.LinkedList;
import ztacker.matrix.Matrix;
import ztacker.move.Move;
import ztacker.tetromino.Tetromino;

public class MoveHistory {

    private final HashMap<Tetromino, LinkedList<Move>> typedHistory;

    private final LinkedList<Move> history;

    private int numMoves;
    private int numSoftDrops;
    private int numHolds;

    public MoveHistory() {
        typedHistory = new HashMap<>();
        for (Tetromino type : Tetromino.values()) {
            typedHistory.put(type, new LinkedList<>());
        }

        history = new LinkedList<>();
    }

    public void updateMove(Move move, Matrix matrix) {
        numMoves++;
        if (move.isHold()) {
            numHolds++;
        }
        if (move.isSoftDrop()) {
            numSoftDrops++;
        }

        typedHistory.get(move.getType()).add(move);
        history.add(move);
    }

    public void revertMove(Move move, Matrix matrix) {
        numMoves--;
        if (move.isHold()) {
            numHolds--;
        }
        if (move.isSoftDrop()) {
            numSoftDrops--;
        }

        typedHistory.get(move.getType()).removeLastOccurrence(move);
        history.removeLastOccurrence(move);
    }

    public HashMap<Tetromino, LinkedList<Move>> getTypedHistory() {
        return typedHistory;
    }

    public LinkedList<Move> getHistory() {
        return history;
    }

    public int getNumMoves() {
        return numMoves;
    }

    public int getNumHolds() {
        return numHolds;
    }

    public int getNumSoftDrops() {
        return numSoftDrops;
    }
}
