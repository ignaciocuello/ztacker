package ztacker.robot.in;

import java.awt.Color;
import java.awt.Graphics2D;
import ztacker.display.Displayable;
import ztacker.matrix.Matrix;

public final class GridCapturerDisplay implements Displayable {
    
    private static final int X_WIDTH = 25;
    private static final int Y_WIDTH = 25;
    
    public static final int WIDTH = X_WIDTH * Matrix.MAX_WIDTH;
    public static final int HEIGHT = Y_WIDTH * (Matrix.MAX_REAL_HEIGHT + 1)
            + 28;
    
    private boolean[][] grid;
    
    public java.util.ArrayList<boolean[][]> Grids = new java.util.ArrayList<>();
    
    private boolean brokenNorm;
    
    @Override
    public void render(Graphics2D g2d) {
        Color nonEmpty = brokenNorm ? Color.RED : Color.GREEN;
        if (grid != null) {
            drawGrid(g2d, 0, grid, nonEmpty);
        }
    }
    
    /**
     * Draws the given grid on the screen as a collection of black and x colour
     * squares, where x is the color given by nonEmpty. A grid element will be coloured
     * black if its empty and 'nonEmpty' colour otherwise.
     * 
     * @param g2d the graphics environment
     * @param xOffset how much to offset the drawing in the x direction
     * @param grid the grid to draw
     * @param nonEmpty the colour to paint non-empty grid elements
     */
    private void drawGrid(Graphics2D g2d, int xOffset, boolean[][] grid, Color nonEmpty) {
    	for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[y].length; x++) {
                g2d.setColor(grid[y][x] ? nonEmpty : Color.BLACK);
                g2d.fillRect(xOffset + x * X_WIDTH, y * Y_WIDTH, 
                        X_WIDTH, Y_WIDTH);
            }
        }
    }
    
    public void setGrid(boolean[][] grid) {
        this.grid = grid;
    }
    
    public void setBrokenNorm(boolean brokenNorm) {
           this.brokenNorm = brokenNorm;
    }
}
