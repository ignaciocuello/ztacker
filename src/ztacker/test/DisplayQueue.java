package ztacker.test;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.LinkedList;
import ztacker.display.Displayable;
import ztacker.tetromino.Tetromino;

public final class DisplayQueue implements Displayable {

    private static final Color DEFAULT_COLOR = Color.WHITE;
    
    private final Rectangle elemBounds;
    private final LinkedList<Color> elems;
    
    public DisplayQueue(Rectangle elemBounds, 
            LinkedList<Tetromino> queue) {
        this.elemBounds = elemBounds;
        
        elems = new LinkedList<>();
        for (Tetromino t : queue) {
            elems.add(t.color());
        }
    }
    
    @Override
    public void render(Graphics2D g2d) {
        int x = (int) elemBounds.getX();
        int y = (int) elemBounds.getY();
        int ew = (int) elemBounds.getWidth();
        int eh = (int) elemBounds.getHeight();
        
        for (Color color : elems) {
            g2d.setColor(color);
            g2d.fillRect(x, y, ew, eh);
            
            g2d.setColor(DEFAULT_COLOR);
            g2d.drawRect(x, y, ew, eh);
            
            x += ew;
        }
    }
    
    public Rectangle getElemBounds() {
        return elemBounds;
    }
}
