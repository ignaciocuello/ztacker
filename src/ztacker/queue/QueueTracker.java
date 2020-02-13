package ztacker.queue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import ztacker.tetromino.Tetromino;

public final class QueueTracker {
    
    private ArrayList<Tetromino> remainder;
    
    public QueueTracker() {
        remainder = initRemainder();
    }
    
    public void updateQueue(Tetromino next) {
        if (remainder.isEmpty()) {
            remainder = initRemainder();
        }
        
        remainder.remove(next);
    }
    
    public void initQueue(LinkedList<Tetromino> queue) {
        remainder.removeAll(queue);
    }
    
    public ArrayList<Tetromino> getRemainder() {
        return remainder;
    }
    
    private static ArrayList<Tetromino> initRemainder() {
        return new ArrayList<>(Arrays.asList(Tetromino.values()));
    }
}
