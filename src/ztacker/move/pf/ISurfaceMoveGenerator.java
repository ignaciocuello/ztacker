package ztacker.move.pf;

public final class ISurfaceMoveGenerator extends LoopSurfaceMoveGenerator {

    private static final int NUM_I_LOOPS = 6;
    private static final int NUM_IJ_LOOPS = 4;
    private static final int NUM_LOOPS = NUM_I_LOOPS + NUM_IJ_LOOPS;

    public ISurfaceMoveGenerator(PFCycle iLoop, 
            PlayingForeverMoveGenerator generator) {
        super(generateLoops(iLoop, generator), 
                PlayingForeverMoveGenerator.I_SURFACE_INDEX);        
    }

    private static PFCycle[] generateLoops(PFCycle iLoop, 
            PlayingForeverMoveGenerator generator) {
        PFCycle[] loops = new PFCycle[NUM_LOOPS];
        loops[0] = new IICycle(iLoop);
        for (int i = 1; i < NUM_LOOPS; i++) {
            loops[i] = i < NUM_I_LOOPS ? new PFCycle(iLoop) 
                    : new IJCycle(i == NUM_LOOPS-1);
        }
        
        return loops;
    }
}
