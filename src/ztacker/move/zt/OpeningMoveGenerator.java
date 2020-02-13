package ztacker.move.zt;

import ztacker.move.PartialSurfaceMoveGenerator;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import ztacker.history.MoveHistory;
import ztacker.history.zt.ZTStackingMoveHistory;
import ztacker.matrix.GridModifier;
import ztacker.matrix.Matrix;
import ztacker.move.MoveGenerator;
import ztacker.io.OpeningReader;
import ztacker.move.FullSurfaceTops;
import ztacker.move.Move;
import ztacker.move.PresetMove;
import ztacker.tetromino.Tetromino;

public final class OpeningMoveGenerator extends PartialSurfaceMoveGenerator {

    private static final ArrayList<Opening> ZT_OPENINGS;
    private static final HashMap<Tetromino, ArrayList<PresetMove>> ZT_OPENING_MOVES;

    private boolean openingLock;

    static {
        OpeningReader reader = new OpeningReader(new File("openings.stk"));
        ZT_OPENING_MOVES = new HashMap<>();

        ArrayList<Opening> openings = null;
        try {
            openings = reader.readOpenings(ZT_OPENING_MOVES);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        ZT_OPENINGS = openings;
    }

    public OpeningMoveGenerator() {
        for (Opening opening : ZT_OPENINGS) {
            opening.setMade(0);
        }
    }

    @Override
    public ArrayList<Move> getMoveList(Matrix matrix,
            FullSurfaceTops fullTops, MoveHistory history,
            MoveGenerator generator) {
        ArrayList<Move> moves = new ArrayList<>();

        if (!openingLock) {
            Tetromino active = matrix.getActive();
            ArrayList<PresetMove> typeKeys = ZT_OPENING_MOVES.get(active);
            if (typeKeys != null) {
                for (PresetMove typeKey : typeKeys) {
                    attemptAppend(typeKey, matrix,
                            ZTStackingMoveHistory.OPENING_INDEX, moves);
                }
            }
        }

        return moves;
    }

    private void attemptAppend(PresetMove key, Matrix matrix,
            int surfaceIndex, ArrayList<Move> moves) {
        long[][] mgrid = matrix.getGrid();
        long[] localGrid = {
            mgrid[Matrix.LEFT_SURFACE_INDEX][0],
            mgrid[Matrix.MID_SURFACE_INDEX][0],
            mgrid[Matrix.RIGHT_SURFACE_INDEX][0]
        };
        if (GridModifier.isFree(localGrid, key.getGrid())
                && GridModifier.isFilled(localGrid, key.getFill())
                && (GridModifier.isFree(localGrid, key.getFreeHard())
                || (key.getFreeSoft() != null
                && GridModifier.isFree(localGrid, key.getFreeSoft()))))  {
            long[][] grid = new long[][]{
                {key.getGrid()[Matrix.LEFT_SURFACE_INDEX], 0L},
                {key.getGrid()[Matrix.MID_SURFACE_INDEX], 0L},
                {key.getGrid()[Matrix.RIGHT_SURFACE_INDEX], 0L}};

            ZTStackingMove move
                    = new ZTStackingMove(grid, surfaceIndex, matrix.getActive(),
                            matrix.isHoldUsed(), key.getSoftDrop());
            move.setOpeningKey(key);
            moves.add(move);
        }
    }

    @Override
    public void update(Matrix matrix, Move move, MoveHistory history) {
        ZTStackingMove ztStackingMove = (ZTStackingMove) move;
        if (ztStackingMove.getOpeningKey() != null) {
            for (Opening opening : ZT_OPENINGS) {
                opening.update(ztStackingMove.getOpeningKey());
            }
        }

        openingLock = finishedOpening();
    }

    @Override
    public void revert(Matrix matrix, Move move, MoveHistory history) {
        ZTStackingMove ztStackingMove = (ZTStackingMove) move;
        if (ztStackingMove.getOpeningKey() != null) {
            for (Opening opening : ZT_OPENINGS) {
                opening.revert(ztStackingMove.getOpeningKey());
            }
        }

        openingLock = finishedOpening();
    }

    private boolean finishedOpening() {
        for (Opening opening : ZT_OPENINGS) {
            if (opening.isDone()) {
                return true;
            }
        }

        return false;
    }

    public boolean isOpeningLock() {
        return openingLock;
    }

    public double getHighestPercentageOfCompletion() {
        double max = 0.0;
        for (Opening opening : ZT_OPENINGS) {
            if (opening.getPercentageOfCompletion() > max) {
                max = opening.getPercentageOfCompletion();
            }
        }

        return max;
    }
}
