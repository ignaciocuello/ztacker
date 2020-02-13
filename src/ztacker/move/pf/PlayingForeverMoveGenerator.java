package ztacker.move.pf;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import ztacker.history.MoveHistory;
import ztacker.io.PFCycleReader;
import ztacker.matrix.Matrix;
import ztacker.move.FullSurfaceTops;
import ztacker.move.Move;
import ztacker.move.MoveGenerator;

public final class PlayingForeverMoveGenerator extends MoveGenerator {

    public static final int STZ_SURFACE_INDEX = 0;
    public static final int I_SURFACE_INDEX = 1;
    public static final int JLO_SURFACE_INDEX = 2;

    private static ArrayList<PFCycle> LOOPS;
    private static final int STZ_LOOP = 0;
    private static final int STZ_FLIP_LOOP = 1;
    private static final int JLO_LOOP = 2;
    private static final int I_LOOP = 3;
    private static final int LO_LOOP = 4;
    private static final int OL_LOOP = 5;

    private final STZSurfaceMoveGenerator stzGenerator;
    private final ISurfaceMoveGenerator iGenerator;
    private final JLOSurfaceMoveGenerator jloGenerator;

    private int downShift;

    static {
        PFCycleReader reader = new PFCycleReader(new File("cycles.stk"));
        try {
            LOOPS = reader.readPFCycles();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public PlayingForeverMoveGenerator() {
        stzGenerator = new STZSurfaceMoveGenerator(LOOPS.get(STZ_LOOP),
                LOOPS.get(OL_LOOP));
        iGenerator = new ISurfaceMoveGenerator(LOOPS.get(I_LOOP), this);
        jloGenerator = new JLOSurfaceMoveGenerator(LOOPS.get(JLO_LOOP),
                LOOPS.get(LO_LOOP), LOOPS.get(STZ_FLIP_LOOP));
    }

    @Override
    public ArrayList<Move> generateMoves(Matrix matrix, MoveHistory history,
            FullSurfaceTops fullTops) {
        ArrayList<Move> moves = new ArrayList<>();
        if (isToppedOutAtTetrominoSpawn(fullTops)) {
            return moves;
        }
        moves.addAll(stzGenerator.getMoveList(matrix, fullTops, history, this));
        moves.addAll(iGenerator.getMoveList(matrix, fullTops, history, this));
        moves.addAll(jloGenerator.getMoveList(matrix, fullTops, history, this));

        return moves;
    }

    @Override
    public void update(Matrix matrix, Move move, MoveHistory history) {
        PlayingForeverMove pfMove = (PlayingForeverMove) move;

        switch (pfMove.getSurfaceIndex()) {
            case STZ_SURFACE_INDEX:
                stzGenerator.update(matrix, move, history);
                break;
            case I_SURFACE_INDEX:
                iGenerator.update(matrix, move, history);
                break;
            case JLO_SURFACE_INDEX:
                jloGenerator.update(matrix, move, history);
                break;
            default:
                break;
        }

        if (move.isClear()) {
            int dif = move.getClearTo() - move.getClearFrom() + 1;
            downShift += dif;
        }
    }

    @Override
    public void revert(Matrix matrix, Move move, MoveHistory history) {
        PlayingForeverMove pfMove = (PlayingForeverMove) move;

        switch (pfMove.getSurfaceIndex()) {
            case STZ_SURFACE_INDEX:
                stzGenerator.revert(matrix, move, history);
                break;
            case I_SURFACE_INDEX:
                iGenerator.revert(matrix, move, history);
                break;
            case JLO_SURFACE_INDEX:
                jloGenerator.revert(matrix, move, history);
                break;
            default:
                break;
        }

       
        if (move.isClear()) {
            int dif = move.getClearTo() - move.getClearFrom() + 1;
            downShift -= dif;
        }
    }

    public STZSurfaceMoveGenerator getSTZGenerator() {
        return stzGenerator;
    }
    
    public ISurfaceMoveGenerator getIGenerator() {
        return iGenerator;
    }
    
    public JLOSurfaceMoveGenerator getJLOGenerator() {
        return jloGenerator;
    }

    public int getDownShift() {
        return downShift;
    }

    public void setDownShift(int downShift) {
        this.downShift = downShift;
    }
}
