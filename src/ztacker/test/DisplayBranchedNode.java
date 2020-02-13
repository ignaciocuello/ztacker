package ztacker.test;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.LinkedList;
import ztacker.display.Displayable;
import ztacker.tetromino.Tetromino;

public final class DisplayBranchedNode implements Displayable {

    private static final Color DEFAULT_COLOR = Color.LIGHT_GRAY;

    public static final int MAIN_X = 650;
    public static final int MAIN_Y = 50;

    public static final int MAIN_WIDTH = 100;

    public static final int MAX_X = MAIN_X + MAIN_WIDTH;

    private static final int MAIN_QUEUE_WIDTH = 10;
    private static final int MAIN_FONT_SIZE = 12;

    private static final int CHILD_Y = 350;
    private static final int CHILD_BOUNDS_WIDTH = 1300;
    private static final int CHILD_WIDTH = 50;

    private static final int CHILD_QUEUE_WIDTH = 5;
    private static final int CHILD_FONT_SIZE = 8;

    private final TestNode mainNode;

    private final DisplayNode main;
    private final DisplayNode max;
    private final ArrayList<DisplayNode> children;

    public DisplayBranchedNode(TestNode mainNode) {
        this.mainNode = mainNode;
        this.children = new ArrayList<>();
        this.main = initDisplayNodes();
        this.max = initMaxNode();
    }

    private DisplayNode initDisplayNodes() {
        DisplayNode dmain = generateDisplayNode(mainNode, MAIN_X, MAIN_Y, MAIN_WIDTH,
                MAIN_QUEUE_WIDTH, MAIN_FONT_SIZE);
        if (!mainNode.getChildren().isEmpty()) {
            int numChildren = mainNode.getChildren().size();
            int deltaX = CHILD_BOUNDS_WIDTH / (numChildren + 1);
            int x = deltaX - CHILD_WIDTH / 2;

            for (int i = 0; i < numChildren; i++) {
                TestNode child = mainNode.getChildren().get(i);
                children.add(
                        generateDisplayNode(child, x, CHILD_Y, CHILD_WIDTH,
                                CHILD_QUEUE_WIDTH, CHILD_FONT_SIZE));
                x += deltaX;
            }
        }

        return dmain;
    }

    private DisplayNode initMaxNode() {
        TestNode max = getMaxChild(mainNode);
        return generateDisplayNode(max, MAX_X + 150, MAIN_Y, MAIN_WIDTH,
                MAIN_QUEUE_WIDTH, MAIN_FONT_SIZE);
    }

    private TestNode getMaxChild(TestNode node) {
        if (node.getChildren().isEmpty()) {
            return node;
        }

        TestNode max = null;
        for (TestNode child : node.getChildren()) {
            if (max == null || child.getValue() > max.getValue()) {
                max = child;
            }
        }

        return getMaxChild(max);
    }

    private DisplayNode generateDisplayNode(TestNode node,
            int mx, int my, int mw, int qw, int fsize) {
        Rectangle bounds = new Rectangle(mx, my, mw, (int) (mw * 2.4));
        DisplayGrid grid = new DisplayGrid(bounds, node.getGrid(), DEFAULT_COLOR);
        DisplayGrid moveGrid = node.getMove() != null
                ? new DisplayGrid(bounds, node.getMove().getGrid(),
                        node.getMove().getType().color())
                : grid;

        Rectangle qbounds = new Rectangle(mx,
                (int) (bounds.getY() + bounds.getHeight()), qw, qw);

        LinkedList<Tetromino> tqueue = new LinkedList<>(node.getQueue());
        if (node.getActive() != null) {
            tqueue.addFirst(node.getActive());
        }
        DisplayQueue queue = new DisplayQueue(qbounds, tqueue);

        Rectangle hbounds
                = new Rectangle(mx + mw - qw, (int) qbounds.getY(), qw, qw);
        LinkedList<Tetromino> hqueue = new LinkedList<>();
        if (node.getHeld() != null) {
            hqueue.add(node.getHeld());
        }
        DisplayQueue held = new DisplayQueue(hbounds, hqueue);

        Rectangle rbounds = new Rectangle(mx,
                (int) (qbounds.getY() + qbounds.getHeight()), qw, qw);
        DisplayQueue remainder = new DisplayQueue(rbounds, node.getRemainder());

        Font font = new Font("TimesRoman", Font.PLAIN, fsize);
        String value = node.getValue() != Integer.MIN_VALUE
                ? Integer.toString(node.getValue()) : "-∞";
        if (node.getMove() != null && node.getMove().isHold()) {
            value += " HELD";
        }

        return node.getString() != null
                ? new DisplayNode(grid, moveGrid, queue, held, remainder,
                        font, value, node.getString())
                : new DisplayNode(grid, moveGrid, queue, held, remainder,
                        font, value);
    }

    @Override
    public void render(Graphics2D g2d) {
        main.render(g2d);
        max.render(g2d);

        for (DisplayNode child : children) {
            child.render(g2d);
        }
    }

    public TestNode getMainNode() {
        return mainNode;
    }
}
