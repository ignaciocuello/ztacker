package ztacker.test;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import ztacker.matrix.Matrix;
import ztacker.matrix.GridStringConverter;
import ztacker.move.Move;

public final class GameDisplay {
    
    public static final Color DEFAULT_COLOR = 
            Color.DARK_GRAY.darker().darker();
    private static final int MAIN_X = 
            DisplayBranchedNode.MAIN_X - DisplayBranchedNode.MAIN_WIDTH -10;
    
    private final Color[][] matrix = 
            new Color[Matrix.MAX_PROCESS_HEIGHT][Matrix.MAX_WIDTH];
    private final int x;
    private final int y;
    private final int width;
    private final int height;
    
    private final int elemWidth;
    private final int elemHeight;
    
    public GameDisplay() {
        this(MAIN_X, DisplayBranchedNode.MAIN_Y, DisplayBranchedNode.MAIN_WIDTH);
    }
    
    public GameDisplay(int x, int y, int width) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = (int) (2.4 * width);
        
        for (int i = 0; i < matrix.length; i++) {
            matrix[i] = makeEmptyRow();
        }
        
        elemWidth = generateElemWidth();
        elemHeight = generateElemHeight();
    }
    
    public GameDisplay(GameDisplay disp) {
        this(disp.x, disp.y, disp.width);
        
        for (int y1 = 0; y1 < matrix.length; y1++) {
            System.arraycopy(disp.matrix[y1], 0, matrix[y1], 0, matrix[y1].length);
        }
    }
    
    private int generateElemWidth() {
        return (int) ((double) width / Matrix.MAX_WIDTH + 0.5);
    }
    
    private int generateElemHeight() {
        return (int) ((double) height / Matrix.MAX_PROCESS_HEIGHT + 0.5);
    }
    
    private Color[] makeEmptyRow() {
        Color[] row = new Color[Matrix.MAX_WIDTH];
        for (int x = 0; x < row.length; x++) {
            row[x] = DEFAULT_COLOR;
        }
        
        return row;
    }
    
    public void makeMove(Move move) {
        setMove(move);
        if (move.isClear()) {
            clearMove(move);
        }
    }
    
    private void setMove(Move move) {
        String matrixString = GridStringConverter.convertGrid(move.getGrid());
        String[] lines = matrixString.split("\n");
        for (int y = 0; y < lines.length; y++) {
            for (int x = 0; x < lines[y].length(); x++) {
                if (lines[y].charAt(x) != '0') {
                    matrix[y][x] = move.getType().color();
                } 
            }
        }
    }
    
    private void clearMove(Move move) {
        clear(move.getClearFrom(), move.getClearTo());
    }
    
    private void clear(int from, int to) {
        int sfrom = Matrix.MAX_PROCESS_HEIGHT-from-1;
        int sto = Matrix.MAX_PROCESS_HEIGHT-to-1;
        
        int dif = -(sto-sfrom) + 1;
        for (int y = sfrom; y >= 0; y--) {
            matrix[y] = 
                    y - dif >= 0 ? matrix[y - dif] : makeEmptyRow();
        }
    }
    
    public void display(Graphics2D g2d) {
        for (int y0 = y, y1 = 0; y1 < Matrix.MAX_PROCESS_HEIGHT; 
                y0 += elemHeight, y1++) {
            for (int x0 = x, x1 = 0; x1 < Matrix.MAX_WIDTH; 
                    x0 += elemWidth, x1++) {
                Color c = matrix[y1][x1];
                if (y1 < Matrix.MAX_UPPER_HEIGHT) {
                    c = c.darker().darker();
                }
                
                g2d.setColor(c);
                g2d.fillRect(x0, y0, elemWidth, elemHeight);
                g2d.setColor(c.darker().darker());
                g2d.setStroke(new BasicStroke(2.0f));
                g2d.drawRect(x0, y0, elemWidth, elemHeight);
            }
        }
    }
    
    public Color[][] getMatrix() {
        return matrix;
    }
}
