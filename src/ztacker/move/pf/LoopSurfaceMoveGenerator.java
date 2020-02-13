package ztacker.move.pf;

import java.util.ArrayList;
import ztacker.history.MoveHistory;
import ztacker.matrix.Matrix;
import ztacker.move.FullSurfaceTops;
import ztacker.move.Move;
import ztacker.move.MoveGenerator;
import ztacker.move.PartialSurfaceMoveGenerator;

public class LoopSurfaceMoveGenerator extends PartialSurfaceMoveGenerator {

    private final int surfaceIndex;

    private final PFCycle[] loops;
    private int index;
    
    private int made;
    
    public LoopSurfaceMoveGenerator(PFCycle[] loops, int surfaceIndex) {
        this.loops = loops;
        this.surfaceIndex = surfaceIndex;
        if (loops.length > 0) {
            loops[0].setBase(0);
        }
    }

    @Override
    public ArrayList<Move> getMoveList(Matrix matrix, FullSurfaceTops fullTops,
            MoveHistory history, MoveGenerator generator) {
        return loops[index].getValidMoves(matrix, fullTops, surfaceIndex, 
                ((PlayingForeverMoveGenerator) generator).getDownShift());
    }

    @Override
    public void update(Matrix matrix, Move move, MoveHistory history) {
        loops[index].updateMove(matrix, move);

        if (loops[index].isTarget()) {
            PFCycle prev = loops[index];
            index++;
            if (index == loops.length) {
                made++;
                index = 0;
                for (PFCycle cycle : loops) {
                    cycle.clear();
                }
            }
            
            loops[index].setBase(prev.getBase() + prev.getHeight());
        }
    }

    @Override
    public void revert(Matrix matrix, Move move, MoveHistory history) {
        if (loops[index].isEmpty()) {
            index--;
            if (index < 0) {
                made--;
                index = loops.length - 1;
                for (PFCycle cycle : loops) {
                    cycle.fill();
                }
            }
        }
        loops[index].revertMove(matrix, move);
    }

    public final int getIndex() {
        return index;
    }
    
    public final PFCycle[] getLoops() {
        return loops;
    }

    public final PFCycle getCurrentCycle() {
        return loops[index];
    }
    
    public final boolean isLastCycle() {
        return index == loops.length - 1;
    }
    
    public final boolean isFirstCycle() {
        return index == 0;
    }
    
    public int getMade() {
        return made;
    }
}
