package ztacker.chooser;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import ztacker.eval.Evaluator;
import ztacker.history.MoveHistory;
import ztacker.matrix.Matrix;
import ztacker.move.FullSurfaceTops;
import ztacker.move.Move;
import ztacker.move.MoveGenerator;

import ztacker.tetromino.Tetromino;

public abstract class MoveChooser {

    private static final int CLOSED_NODE_WEIGHT = -1_000_000;

    private final MoveGenerator generator;
    private final Evaluator evaluator;
    private final MoveHistory history;

    private boolean mirror;

    public MoveChooser(MoveGenerator generator,
            Evaluator evaluator, MoveHistory history) {
        this.generator = generator;
        this.evaluator = evaluator;
        this.history = history;
    }

    public abstract MoveChooser copy();

    public Node chooseFirstMove(Tetromino active,
            LinkedList<Tetromino> queue,
            LinkedList<Tetromino> remainder) {
        if (remainder.isEmpty()) {
            remainder = Tetromino.valuesAsLinkedList();
        }

        LinkedList<Tetromino> mirrorQueue = mirrorQueue(queue);
        LinkedList<Tetromino> mirrorRemainder = mirrorQueue(remainder);

        Node normMax = chooseMove(active, queue, remainder);
        Node mirrorMax = chooseMove(active.mirror(), mirrorQueue,
                mirrorRemainder);

        int normVal = normMax != null ? normMax.getValue() : Integer.MIN_VALUE;
        int mirrorVal = mirrorMax != null
                ? mirrorMax.getValue() : Integer.MIN_VALUE;

        mirror = normVal < mirrorVal;

        return mirror ? mirrorMax : normMax;
    }

    private Node chooseMove(Tetromino active,
            LinkedList<Tetromino> queue, LinkedList<Tetromino> remainder) {
        Matrix matrix = new Matrix();
        matrix.setActive(active);
        matrix.setQueue(queue);

        return chooseMove(matrix, remainder);
    }

    private Node chooseMove(Matrix matrix,
            LinkedList<Tetromino> remainder) {
        Node root = new Node();
        search(matrix, root, remainder);
        return root.getMaxChild();
    }

    public Node chooseNextMove(Matrix matrix,
            LinkedList<Tetromino> queue,
            LinkedList<Tetromino> remainder) {
        if (remainder.isEmpty()) {
            remainder = Tetromino.valuesAsLinkedList();
        }

        Matrix mat = new Matrix(matrix);
        mat.setQueue(queue);

        if (mirror) {
            Matrix mirrorMatrix = new Matrix();

            mirrorMatrix.set(matrix.getGrid());
            mirrorMatrix.setActive(matrix.getActive().mirror());
            mirrorMatrix.setQueue(mirrorQueue(queue));
            mirrorMatrix.setHeld(matrix.getHeld() != null
                    ? matrix.getHeld().mirror() : null);

            mat = mirrorMatrix;
            remainder = mirrorQueue(remainder);
        }

        return chooseMove(mat, remainder);
    }

    private void search(Matrix matrix, Node parent,
            LinkedList<Tetromino> remainder) {
        if (matrix.getActive() != null) {
            ArrayList<Move> moves = generator.generateMoves(matrix, history);
            for (Move move : moves) {
                createNodeAndBranch(matrix, move, parent, remainder);
            }
        }

        if (!matrix.isHoldUsed()) {
            hold(matrix, parent, remainder);
        }
    }

    private void createNodeAndBranch(Matrix matrix, Move move, Node parent,
            LinkedList<Tetromino> remainder) {
        makeMove(move, matrix);

        Node child = new Node(parent, move);
        if (matrix.getActive() == null) {
            eval(matrix, child, remainder);
        } else {
            search(matrix, child, remainder);
        }

        unmakeMove(move, matrix);
    }

    private void hold(Matrix matrix, Node parent,
            LinkedList<Tetromino> remainder) {
        matrix.setHoldUsed(true);

        Tetromino held = matrix.getHeld();
        Tetromino active = matrix.getActive();
        Tetromino noHeld = null;

        matrix.setHeld(active);
        if (held == null) {
            matrix.setActive(noHeld = matrix.getQueue().pollFirst());
        } else {
            matrix.setActive(held);
        }

        search(matrix, parent, remainder);

        matrix.setHoldUsed(false);
        matrix.setHeld(held);
        matrix.setActive(active);

        if (held == null && noHeld != null) {
            matrix.getQueue().addFirst(noHeld);
        }
    }

    private void makeMove(Move move, Matrix matrix) {
        matrix.set(move.getGrid());

        if (move.isClear()) {
            matrix.clearRange(move.getClearFrom(), move.getClearTo());
        }

        matrix.setActive(matrix.getQueue().pollFirst());
        matrix.setHoldUsed(false);

        updateAll(move, matrix);
    }

    private void unmakeMove(Move move, Matrix matrix) {
        revertAll(move, matrix);

        if (move.isClear()) {
            matrix.revertClearRange(move.getClearFrom(), move.getClearTo());
        }
        matrix.clear(move.getGrid());

        if (matrix.getActive() != null) {
            matrix.getQueue().addFirst(matrix.getActive());
        }

        matrix.setActive(move.getType());
        matrix.setHoldUsed(move.isHold());
    }

    public void updateAll(Move move, Matrix matrix) {
        history.updateMove(move, matrix);
        generator.update(matrix, move, history);
    }

    public void revertAll(Move move, Matrix matrix) {
        history.revertMove(move, matrix);
        generator.revert(matrix, move, history);
    }

    private void eval(Matrix matrix, Node node,
            LinkedList<Tetromino> remainder) {
        FullSurfaceTops fullTops
                = FullSurfaceTops.constructFullSurfaceTop(matrix);
        int value = evaluator.evaluate(matrix, history, remainder, generator,
                fullTops, this);
        int secondaryValue = evaluator.getSecondaryValue(history);

        if (node.overridesParent(value, secondaryValue)) {
            if (evaluator.calculateOpenness(matrix, history, remainder,
                    generator, fullTops, this) > 0) {
                node.setValue(value, secondaryValue);
            } else {
                node.setValue(value + CLOSED_NODE_WEIGHT, secondaryValue);
            }
        }
    }

    private LinkedList<Tetromino> mirrorQueue(List<Tetromino> queue) {
        LinkedList<Tetromino> mirrorQueue = new LinkedList<>();
        for (Tetromino tetromino : queue) {
            mirrorQueue.add(tetromino.mirror());
        }

        return mirrorQueue;
    }

    public boolean isMirror() {
        return mirror;
    }
}
