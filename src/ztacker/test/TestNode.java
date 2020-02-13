package ztacker.test;

import java.util.ArrayList;
import java.util.LinkedList;
import ztacker.chooser.Node;
import ztacker.matrix.GridModifier;
import ztacker.move.Move;
import ztacker.tetromino.Tetromino;

public final class TestNode {

    private final Move move;

    private final long[][] grid;

    private int value;

    private final TestNode parent;
    private final ArrayList<TestNode> children;

    private final Tetromino active;
    private final Tetromino held;
    
    private final LinkedList<Tetromino> queue;
    private final LinkedList<Tetromino> remainder;

    private String string;
    
    public TestNode(long[][] grid, Tetromino active, Tetromino held, 
            LinkedList<Tetromino> queue, LinkedList<Tetromino> remainder) {
        this(null, null, grid, active, held, queue, remainder);
    }

    public TestNode(TestNode parent, Node node, long[][] grid, 
            Tetromino active, Tetromino held,
            LinkedList<Tetromino> queue, LinkedList<Tetromino> remainder) {
        this.grid = GridModifier.copy(grid);
        this.parent = parent;
        this.active = active;
        this.held = held;
        this.queue = new LinkedList<>(queue);
        this.remainder = new LinkedList<>(remainder);
        this.string = string;

        move = node != null ? node.getMove() : null;
        value = Integer.MIN_VALUE;
        children = new ArrayList<>();
    }

    public Move getMove() {
        return move;
    }

    public long[][] getGrid() {
        return grid;
    }

    public void addChild(TestNode child) {
        children.add(child);
    }

    public ArrayList<TestNode> getChildren() {
        return children;
    }

    public TestNode getParent() {
        return parent;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;

        if (parent != null) {
            if (value > parent.value) {
                parent.setValue(value);
            }
        }
    }
    
    public Tetromino getActive() {
        return active;
    }
    
    public Tetromino getHeld() {
        return held;
    }

    public LinkedList<Tetromino> getQueue() {
        return queue;
    }

    public LinkedList<Tetromino> getRemainder() {
        return remainder;
    }
    
    public String getString() {
        return string;
    }
    
    public void setString(String string) {
        this.string = string;
    }
}
