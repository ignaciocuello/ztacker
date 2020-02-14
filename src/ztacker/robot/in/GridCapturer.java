package ztacker.robot.in;

import java.awt.Color;
import java.awt.image.BufferedImage;

import ztacker.matrix.Matrix;
import ztacker.test.GameDisplay;

public final class GridCapturer {

	private static final int BLACK = 0xff000000;
	
    private final int gridX;

    private final int gridY;
    private final int gridUpperY;

    private final int gridXStep;
    private final int gridYStep;

    private final int[][] unsetRGBGrid;

    private boolean[][] grid;

    private BufferedImage gmsrc;
    
    private boolean brokenNorm;

    public GridCapturer(
            int gridX, int gridY, int gridUpperY,
            int gridXStep, int gridYStep) {
        this.gridX = gridX;

        this.gridY = gridY;
        this.gridUpperY = gridUpperY;

        this.gridXStep = gridXStep;
        this.gridYStep = gridYStep;

        unsetRGBGrid = new int[Matrix.MAX_REAL_HEIGHT + 1][Matrix.MAX_WIDTH];
    }

    /**
     * Initializes the unsetRGBGrid array with the hex values of the colour of each grid element
     * when it is unoccupied by a tetromino piece. This is so that when we detect that the colour
     * of the grid element is different than this value, we know that it is occupied.
     */
    public void initUnsetRGBGrid() {
        for (int y = 0; y < unsetRGBGrid.length; y++) {
            for (int x = 0; x < unsetRGBGrid[y].length; x++) {
            	//TODO add this as an option to be read from file
            	unsetRGBGrid[y][x] = BLACK;
            }
        }
    }

    public void updateGrid(GameDisplay disp, boolean mirror) {
        boolean[][] bgrid
                = new boolean[Matrix.MAX_REAL_HEIGHT + 1][Matrix.MAX_WIDTH];
        for (int y = 0; y < bgrid.length; y++) {
            for (int x = 0; x < bgrid[y].length; x++) {
                bgrid[y][x]
                        = gmsrc.getRGB(gridX + x * gridXStep,
                                (y == 0 ? gridUpperY : (gridY + (y - 1)
                                        * gridYStep))) != unsetRGBGrid[y][x];
            }
        }

        ignoreSetGrid(bgrid, disp, mirror);
        grid = bgrid;
    }

    private void ignoreSetGrid(boolean[][] bgrid, GameDisplay disp,
            boolean mirror) {
        Color[][] cgrid = disp.getMatrix();
        for (int y = 0; y < bgrid.length; y++) {
            for (int x = 0; x < bgrid[y].length; x++) {
                if (cgrid[y + Matrix.MAX_UPPER_HEIGHT - 1][!mirror
                        ? x : Matrix.MAX_WIDTH - 1 - x]
                        != GameDisplay.DEFAULT_COLOR) {
                    bgrid[y][x] = false;
                }
            }
        }

    }

    public boolean[][] getNorm() {
        return getNorm(grid);
    }

    private boolean[][] getNorm(boolean[][] bgrid) {
        boolean[][] norm = new boolean[bgrid.length][bgrid[0].length];
        int shift = getRealShift(bgrid);

        int occupied = 0;
        for (int i = 0; i <= shift; i++) {
            for (int x = 0; x < Matrix.MAX_WIDTH; x++) {
                norm[Matrix.MAX_REAL_HEIGHT - i][x] = bgrid[shift - i][x];
                if (norm[Matrix.MAX_REAL_HEIGHT-i][x]) {
                    occupied++;
                }
            }
        }
        brokenNorm = occupied != 4;

        return norm;
    }

    private int getRealShift(boolean[][] bgrid) {
        int shift;
        int adjacent = 0;
        for (shift = 0; shift < bgrid.length; shift++) {
            boolean occupied = false;
            for (int x = 0; x < Matrix.MAX_WIDTH; x++) {
                if (bgrid[shift][x]) {
                    occupied = true;
                    adjacent++;
                }
            }

            if (!occupied && adjacent > 0) {
                break;
            }
        }

        return shift - 1;
    }

    public int getHeight() {
        return Matrix.MAX_REAL_HEIGHT - getRealShift(grid);
    }

    public boolean isDasLeft() {
        boolean[][] norm = getNorm();
        for (int y = 0; y < norm.length; y++) {
            if (norm[y][0]) {
                return true;
            }
        }

        return false;
    }

    public boolean isDasRight() {
        boolean[][] norm = getNorm();
        for (int y = 0; y < norm.length; y++) {
            if (norm[y][Matrix.MAX_WIDTH - 1]) {
                return true;
            }
        }

        return false;
    }

    public boolean isNonVerticalChange(boolean[][] bgrid0, boolean[][] bgrid1) {
        boolean[][] norm0 = getNorm(bgrid0);
        boolean[][] norm1 = getNorm(bgrid1);
        
        for (int y = 0; y < norm0.length; y++) {
            for (int x = 0; x < norm0[y].length; x++) {
                if (norm0[y][x] != norm1[y][x]) {
                    return true;
                }
            }
        }

        return false;
    }

    public boolean[][] getGrid() {
        return grid;
    }

    public void setGameSource(BufferedImage gmsrc) {
        this.gmsrc = gmsrc;
    }

    public int getGridX() {
        return gridX;
    }

    public int getGridY() {
        return gridY;
    }

    public int getGridUpperY() {
        return gridUpperY;
    }

    public int getGridXStep() {
        return gridXStep;
    }

    public int getGridYStep() {
        return gridYStep;
    }
    
    public boolean isBrokenNorm() {
        return brokenNorm;
    }
}
