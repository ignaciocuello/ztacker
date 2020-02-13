package ztacker.move.zt;

import java.util.ArrayList;
import ztacker.move.PresetMove;

public final class Opening {
    
    private int made;
    
    private final ArrayList<PresetMove> moves;
    
    public Opening(ArrayList<PresetMove> moves) {
        this.moves = moves;
    }
    
    public void setMade(int made) {
        this.made = made;
    }
    
    public void update(PresetMove move) {
        if (moves.contains(move)) {
            made++;
        }
    }
    
    public void revert(PresetMove move) {
        if (moves.contains(move)) {
            made--;
        }
    }
    
    public boolean isDone() {
        return made == moves.size();
    }
    
    public double getPercentageOfCompletion() {
        return (double) made / moves.size();
    }
}
