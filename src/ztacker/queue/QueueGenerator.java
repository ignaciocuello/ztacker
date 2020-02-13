package ztacker.queue;

import java.util.LinkedList;
import ztacker.tetromino.Tetromino;

public abstract class QueueGenerator {

    private final LinkedList<Tetromino> queue;

    public QueueGenerator() {
        queue = new LinkedList<>();
    }
    
    public abstract boolean init();
    
    public Tetromino getNext() {
        return queue.poll();
    }
    
    public LinkedList<Tetromino> getQueue() {
        return queue;
    }
}
