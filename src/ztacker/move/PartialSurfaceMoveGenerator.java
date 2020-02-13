package ztacker.move;

import java.util.ArrayList;
import ztacker.history.MoveHistory;
import ztacker.matrix.Matrix;

public abstract class PartialSurfaceMoveGenerator {
    
    public abstract ArrayList<Move> getMoveList(Matrix matrix, 
            FullSurfaceTops fullTops, MoveHistory history, 
            MoveGenerator generator);
    
    public abstract void update(Matrix matrix, Move move, 
            MoveHistory history);
    
    public abstract void revert(Matrix matrix, Move move, 
            MoveHistory history);
}
