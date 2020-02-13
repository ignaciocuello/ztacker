package ztacker.queue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import ztacker.tetromino.Tetromino;

public final class PseudoRandomQueueGenerator extends QueueGenerator {
   
    public static final int QUEUE_SIZE = 4;
    public static final List<Tetromino> MAIN_SET;
       
    private LinkedList<Tetromino> buffer;
    
    private final LinkedList<Tetromino> savedQueue;
    
    static {
        ArrayList<Tetromino> mainSet = new ArrayList<>();
        for (Tetromino type : Tetromino.values()) {
            mainSet.add(type);
        }
        
        MAIN_SET = Collections.unmodifiableList(mainSet);
    }
    
    public PseudoRandomQueueGenerator(LinkedList<Tetromino> queue, 
            LinkedList<Tetromino> buffer) {
        super.getQueue().addAll(queue);
        this.buffer = buffer;
        
        savedQueue = new LinkedList<>(queue);
    }
    
    public PseudoRandomQueueGenerator() {
        this(new LinkedList<>(), new LinkedList<>());
    }
    
    @Override
    public boolean init() {
        updateQueue();
        return true;
    }
    
    private void fillBuffer() {
        buffer.addAll(MAIN_SET);
        
        Collections.shuffle(buffer);
    }
    
    private void updateQueue() {
        while (super.getQueue().size() < QUEUE_SIZE) {
            if (buffer.isEmpty()) {
                fillBuffer();
            }
            
            Tetromino newPiece = buffer.pop();
            super.getQueue().add(newPiece);
            savedQueue.add(newPiece);
        }
    }
    
    @Override
    public Tetromino getNext() {
        Tetromino next = super.getNext();
        updateQueue();
        
        return next;
    }
    
    @Override
    public LinkedList<Tetromino> getQueue() {
        return new LinkedList<>(super.getQueue().subList(0, QUEUE_SIZE));
    }
    
    public LinkedList<Tetromino> getBuffer() {
        return buffer;
    }
    
    public LinkedList<Tetromino> getSavedQueue() {
        return savedQueue;
    }
}
