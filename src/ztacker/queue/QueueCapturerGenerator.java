package ztacker.queue;

import java.awt.image.BufferedImage;
import java.util.LinkedList;
import ztacker.robot.in.QueueCapturer;
import ztacker.tetromino.Tetromino;

public final class QueueCapturerGenerator extends QueueGenerator {
    
    private final QueueCapturer capturer;
    
    private BufferedImage gameSource;
    
    public QueueCapturerGenerator(QueueCapturer capturer) {
        this.capturer = capturer;
    }
    
    @Override
    public boolean init() {
        LinkedList<Tetromino> cp = capturer.readQueue(gameSource);
        if (cp == null) {
            return false;
        }
        
        super.getQueue().addAll(cp);
        return true;
    }
    
    public void setGameSource(BufferedImage gmsrc) {
        this.gameSource = gmsrc;
    }
    
    @Override
    public Tetromino getNext() {
        LinkedList<Tetromino> cp = capturer.readQueue(gameSource);
        if (cp == null || cp.equals(getQueue())) {
            return null;
        }
        
        Tetromino next = super.getNext();
        getQueue().add(cp.removeLast());
        
        return next;
    }
    
    public QueueCapturer getCapturer() {
        return capturer;
    }
}
