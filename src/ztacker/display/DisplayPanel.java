package ztacker.display;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import javax.swing.JPanel;

public final class DisplayPanel extends JPanel {

    private final ArrayList<Displayable> displayables;
    
    public DisplayPanel() {
        displayables = new ArrayList<>();
    }

    @Override
    public void paintComponent(Graphics g) {
        setBackground(Color.BLACK);
        super.paintComponent(g);

        try {
            render((Graphics2D) g);
        } catch (ConcurrentModificationException expected) {
        }
    }

    private void render(Graphics2D g2d) {
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        for (Displayable displayable : displayables) {
            displayable.render(g2d);
        }
    }

    public void addDisplayable(Displayable displayable) {
        displayables.add(displayable);
    }

    public void removeDisplayable(Displayable displayable) {
        displayables.remove(displayable);
    }
}
