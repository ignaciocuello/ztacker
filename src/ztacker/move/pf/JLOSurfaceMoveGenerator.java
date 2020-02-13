package ztacker.move.pf;

import java.util.ArrayList;
import ztacker.history.MoveHistory;
import ztacker.matrix.GridModifier;
import ztacker.matrix.Matrix;
import ztacker.move.FullSurfaceTops;
import ztacker.move.Move;
import ztacker.move.MoveGenerator;
import ztacker.tetromino.Tetromino;

public final class JLOSurfaceMoveGenerator extends LoopSurfaceMoveGenerator {
    
    private static final int NUM_JLO_LOOPS = 12;
    private static final int NUM_LO_LOOPS = 2;
    private static final int NUM_STZ_LOOPS = 1;
    private static final int NUM_LOOPS = NUM_JLO_LOOPS + NUM_LO_LOOPS 
            + NUM_STZ_LOOPS;
    
    public JLOSurfaceMoveGenerator(PFCycle jloLoop, PFCycle loLoop, 
            PFCycle stzLoop) {
        super(generateLoops(jloLoop, loLoop, stzLoop), 
                PlayingForeverMoveGenerator.JLO_SURFACE_INDEX); 
    }
    
    @Override
    public ArrayList<Move> getMoveList(Matrix matrix, FullSurfaceTops fulltops, 
            MoveHistory history, MoveGenerator generator) {
        ArrayList<Move> moves = 
                super.getMoveList(matrix, fulltops, history, generator);
        ArrayList<Move> validMoves = new ArrayList<>(moves);
        
        if (matrix.getActive() == Tetromino.J_TYPE 
                || matrix.getActive() == Tetromino.L_TYPE) {
            long[] localGrid = getCurrentCycle().getLocalGrid();
            for (Move move : moves) {
                PlayingForeverMove pfMove = (PlayingForeverMove) move;
                GridModifier.setOrGridHalf(localGrid, 
                        pfMove.getCycleKey().getGrid());
                if (isImpossibleJLOPlacement(localGrid)) {
                    validMoves.remove(move);
                }
                GridModifier.clearOrGridHalf(localGrid, 
                        pfMove.getCycleKey().getGrid());
            }
        }
        
        return validMoves;
    }
    
    private boolean isImpossibleJLOPlacement(long[] localGrid) {
        return (localGrid[Matrix.MID_SURFACE_INDEX] & 0b0010_0010_0010L)
                == 0b0010_0010_0010L
                && (localGrid[Matrix.RIGHT_SURFACE_INDEX] & 0b1110_0010_0010L)
                == 0b1110_0010_0010L
                && (localGrid[Matrix.RIGHT_SURFACE_INDEX] & 0b1100_1100L)
                == 0L;
    }
    
    private static PFCycle[] generateLoops(PFCycle jloLoop, 
            PFCycle loLoop, PFCycle stzLoop) {
        PFCycle[] loops = new PFCycle[NUM_LOOPS];
        for (int i = 0; i < NUM_LOOPS; i++) {
            if (i < NUM_JLO_LOOPS) {
                loops[i] = new PFCycle(jloLoop);
            } else if (i < NUM_JLO_LOOPS+NUM_LO_LOOPS) {
                loops[i] = new PFCycle(loLoop);
            } else {
                loops[i] = new PFCycle(stzLoop);
            }
        } 
        return loops;
    }
}
