package ztacker.move.pf;

public final class STZSurfaceMoveGenerator extends LoopSurfaceMoveGenerator {

    private static final int NUM_STZ_LOOPS = 4;
    private static final int NUM_OL_LOOPS = 2;
    private static final int NUM_LOOPS = NUM_STZ_LOOPS + NUM_OL_LOOPS;

    public STZSurfaceMoveGenerator(PFCycle stzLoop, PFCycle olLoop) {
        super(generateLoops(stzLoop, olLoop), 
                PlayingForeverMoveGenerator.STZ_SURFACE_INDEX);
    }
    
    private static PFCycle[] generateLoops(PFCycle stzLoop, 
            PFCycle olLoop) {
        PFCycle[] loops = new PFCycle[NUM_LOOPS];
        for (int i = 0; i < NUM_LOOPS; i++) {
            loops[i] = new PFCycle(i < NUM_STZ_LOOPS ? stzLoop : olLoop);
        }
        
        return loops;
    }
}
