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
    private boolean brokenNorm;
    
    @Override
    public void render(Graphics2D g2d) {
        Color nonEmpty = brokenNorm ? Color.RED : Color.GREEN;
        if (grid != null) {
            for (int y = 0; y < grid.length; y++) {
                for (int x = 0; x < grid[y].length; x++) {
                    g2d.setColor(grid[y][x] ? nonEmpty : Color.BLACK);
                    g2d.fillRect(x * X_WIDTH, y * Y_WIDTH, 
                            X_WIDTH, Y_WIDTH);
                }
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
