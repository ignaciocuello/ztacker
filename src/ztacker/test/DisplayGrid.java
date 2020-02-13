package ztacker.test;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import ztacker.display.Displayable;
import ztacker.matrix.GridStringConverter;
import ztacker.matrix.Matrix;

public final class DisplayGrid implements Displayable {

    private static final Color DEFAULT_COLOR = Color.WHITE;

    private final Rectangle bounds;
    private final Rectangle elemBounds;

    private final Color fill;
    private final boolean[][] elems;

    public DisplayGrid(Rectangle bounds, long[][] grid, Color fill) {
        this.bounds = bounds;
        this.fill = fill;

        this.elemBounds = initElemBounds();
        this.elems = initElems(grid);
    }

    private Rectangle initElemBounds() {
        return new Rectangle(
                (int) (bounds.getWidth() / Matrix.MAX_WIDTH + 0.5),
                (int) (bounds.getHeight() / Matrix.MAX_PROCESS_HEIGHT + 0.5));
    }

    private boolean[][] initElems(long[][] grid) {
        boolean[][] elems
                = new boolean[Matrix.MAX_PROCESS_HEIGHT][Matrix.MAX_WIDTH];
        String gridString = GridStringConverter.convertGrid(grid);
        String[] lines = gridString.split("\n");
        if (lines.length != Matrix.MAX_PROCESS_HEIGHT) {
            throw new IllegalArgumentException();
        }

        for (int y = 0; y < lines.length; y++) {
            elems[y] = parseGridLine(lines[y]);
        }

        return elems;
    }

    private boolean[] parseGridLine(String line) {
        char[] charArray = line.toCharArray();
        if (charArray.length != Matrix.MAX_WIDTH) {
            throw new IllegalArgumentException();
        }

        boolean[] row = new boolean[Matrix.MAX_WIDTH];
        for (int x = 0; x < charArray.length; x++) {
            row[x] = charArray[x] == '1';
        }

        return row;
    }

    @Override
    public void render(Graphics2D g2d) {
        int bx = (int) bounds.getX();
        int by = (int) bounds.getY();

        int ew = (int) elemBounds.getWidth();
        int eh = (int) elemBounds.getHeight();

        for (int y0 = by, y1 = 0; y1 < Matrix.MAX_PROCESS_HEIGHT;
                y0 += eh, y1++) {
            for (int x0 = bx, x1 = 0; x1 < Matrix.MAX_WIDTH;
                    x0 += ew, x1++) {
                if (elems[y1][x1]) {
                    g2d.setColor(fill);
                    g2d.fillRect(x0, y0, ew, eh);
                }

                g2d.setColor(DEFAULT_COLOR);
                if (y1 < Matrix.MAX_UPPER_HEIGHT) {
                    g2d.fillRect(x0, y0, ew, eh);
                } else {
                    g2d.drawRect(x0, y0, ew, eh);
                }
            }
        }
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public Rectangle getElemBounds() {
        return elemBounds;
    }
}
