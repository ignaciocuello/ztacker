package ztacker.chooser;

import ztacker.move.Move;

public final class Node {

    private final Move move;

    private final Node parent;
    private Node maxChild;

    private int value;
    private int secondaryValue;

    public Node() {
        this(null, null);
    }

    public Node(Node parent, Move move) {
        this.parent = parent;
        this.move = move;

        value = Integer.MIN_VALUE;
        secondaryValue = Integer.MIN_VALUE;
    }

    public Move getMove() {
        return move;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value, int secondaryValue) {
        this.value = value;
        this.secondaryValue = secondaryValue;

        if (overridesParent(value, secondaryValue)) {
            parent.setValue(value, secondaryValue);
            parent.maxChild = this;
        }
    }

    public boolean overridesParent(int value, int secondaryValue) {
        return parent != null
                && (value > parent.value
                || (value == parent.value
                && secondaryValue > parent.secondaryValue));
    }

    public Node getMaxChild() {
        return maxChild;
    }
}
