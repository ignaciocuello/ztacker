package ztacker.test;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import ztacker.display.Displayable;

public final class DisplayNode implements Displayable {

    private static final Color DEFAULT_COLOR = Color.WHITE;

    private final DisplayGrid grid;
    private final DisplayGrid moveGrid;
    private final DisplayQueue queue;
    private final DisplayQueue held;
    private final DisplayQueue remainder;

    private final Font font;
    private final String[] strings;

    public DisplayNode(DisplayGrid grid, DisplayGrid moveGrid, 
            DisplayQueue queue, DisplayQueue held, DisplayQueue remainder, 
            Font font, String... strings) {
        this.grid = grid;
        this.moveGrid = moveGrid;
        this.queue = queue;
        this.held = held;
        this.remainder = remainder;
        this.font = font;
        this.strings = strings;
    }

    @Override
    public void render(Graphics2D g2d) {
        grid.render(g2d);
        moveGrid.render(g2d);
        queue.render(g2d);
        held.render(g2d);
        remainder.render(g2d);

        g2d.setFont(font);
        g2d.setColor(DEFAULT_COLOR);
        FontMetrics fm = g2d.getFontMetrics();
        int x = (int) grid.getBounds().getX();
        int y = (int) (remainder.getElemBounds().getY()
                + remainder.getElemBounds().getHeight()) + fm.getHeight();
        for (String string : strings) {
            g2d.drawString(string, x, y);
            y += fm.getHeight();
        }
    }
}
