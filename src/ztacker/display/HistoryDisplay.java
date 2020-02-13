package ztacker.display;

import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import ztacker.move.Move;
import ztacker.test.DisplayBranchedNode;
import ztacker.test.GameDisplay;
import ztacker.test.TestNode;

public class HistoryDisplay extends KeyAdapter implements Displayable {

    private GameDisplay matrix;
    private final ArrayList<GameDisplay> matrices;
    
    private DisplayBranchedNode root;
    private final ArrayList<DisplayBranchedNode> roots;
    
    private int index;
    
    public HistoryDisplay() {
        matrix = new GameDisplay();
        matrices = new ArrayList<>();
        
        root = null;
        roots = new ArrayList<>();
        
        matrices.add(matrix);
        
        index = -1;
    }
    
    @Override
    public void render(Graphics2D g2d) {
        if (root != null) {
            root.render(g2d);
        }
        
        if (matrix != null) {
            matrix.display(g2d);
        }
    }    

    @Override
    public void keyTyped(KeyEvent ke) {
        char key = ke.getKeyChar();
        
        if ('0' <= key && key <= '9') {
            shiftChild(Integer.parseInt(Character.toString(key)));
        } else if (key == 'r') {
            upToRoot();
        } else if (key == 'm') {
            shiftToMax();
        } else if (key == 'a') {
            if (index-1 >= 0) {
                index--;
            }
            
            root = roots.get(index);
            matrix = matrices.get(index);
        } else if (key == 'd') {
            if (index+1 < roots.size()) {
                index++;
            }
            
            root = roots.get(index);
            matrix = matrices.get(index);
        }
    }
    
    public void makeMove(Move move) {
        GameDisplay newm = new GameDisplay(matrices.get(matrices.size()-1));
        newm.makeMove(move);
        matrices.add(newm);
        
        matrix = newm;
    }

    public void addRoot(TestNode tNode) {
        setRoot(tNode);
        roots.add(root);
        index = roots.size()-1;
    }
    
    private void setRoot(TestNode tNode) {
        root = new DisplayBranchedNode(tNode);
    }
    
    private void shiftChild(int childNo) {
        if (childNo < root.getMainNode().getChildren().size()) {
            setRoot(root.getMainNode().getChildren().get(childNo));
        }
    }
    
    private void shiftToMax() {
        if (!root.getMainNode().getChildren().isEmpty()) {
            TestNode max = null;
            for (TestNode tNode : root.getMainNode().getChildren()) {
                if (max == null || tNode.getValue() > max.getValue()) {
                    max = tNode;
                }
            }
            
            setRoot(max);
        }
    }
    
    private void upToRoot() {
        if (root.getMainNode().getParent() != null) {
            setRoot(root.getMainNode().getParent());
        }
    }
    
    public GameDisplay getMatrix() {
        return matrix;
    }
    
    public DisplayBranchedNode getRoot() {
        return root;
    }
}
