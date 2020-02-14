package ztacker.robot.in;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.LinkedList;
import ztacker.tetromino.Tetromino;

public final class QueueCapturer {

    private static final int THRESHOLD_DIST = 2500;

    private final HashMap<Integer, Tetromino> colorMap;
    private final int[] queueX;
    private final int[] queueY;

    public QueueCapturer(HashMap<Integer, Tetromino> colorMap,
            int[] queueX, int[] queueY) {
        this.colorMap = colorMap;
        this.queueX = queueX;
        this.queueY = queueY;
    }

    public LinkedList<Tetromino> readQueue(
            BufferedImage gmsrc) {
        if (gmsrc == null) {
            return null;
        }
        
        LinkedList<Tetromino> queue = new LinkedList<>();
        for (int i = 0; i < queueX.length; i++) {
            Tetromino t = null;
            int pc = gmsrc.getRGB(queueX[i], queueY[i]);
            
            for (int color : colorMap.keySet()) {
                if (colorDist(pc, color) < THRESHOLD_DIST) {
                    t = colorMap.get(color);
                    break;
                }
            }
            
            if (t == null) {
                return null;
            }

            queue.add(t);
        }

        return queue;
    }

    public int colorDist(int c0, int c1) {
        int r0 = (c0 & 0xFF0000) >> 16;
        int g0 = (c0 & 0xFF00) >> 8;
        int b0 = (c0 & 0xFF);

        int r1 = (c1 & 0xFF0000) >> 16;
        int g1 = (c1 & 0xFF00) >> 8;
        int b1 = (c1 & 0xFF);

        int dr = r1 - r0;
        int dg = g1 - g0;
        int db = b1 - b0;
        return dr * dr + dg * dg + db * db;
    }

    public int[] getQueueX() {
        return queueX;
    }

    public int[] getQueueY() {
        return queueY;
    }
}
