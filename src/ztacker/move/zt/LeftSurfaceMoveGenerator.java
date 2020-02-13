package ztacker.move.zt;

import ztacker.move.PartialSurfaceMoveGenerator;
import java.util.ArrayList;
import java.util.HashMap;
import ztacker.history.MoveHistory;
import ztacker.matrix.Matrix;
import ztacker.move.MoveGenerator;
import ztacker.move.FullSurfaceTops;
import ztacker.move.Move;
import ztacker.tetromino.Tetromino;

public final class LeftSurfaceMoveGenerator
        extends PartialSurfaceMoveGenerator {

    @Override
    public ArrayList<Move> getMoveList(Matrix matrix,
            FullSurfaceTops fullTops, MoveHistory history,
            MoveGenerator generator) {
        ZTStackingMoveGenerator ztStackingGenerator
                = (ZTStackingMoveGenerator) generator;

        ArrayList<Move> moves = new ArrayList<>();
        Tetromino active = matrix.getActive();

        if (isInvalid(active, ztStackingGenerator)) {
            return moves;
        }

        HashMap<Integer, ArrayList<RotationSurface>> rotationSurfaces
                = RotationSurfaceMap.getRotationSurfaces(active);
        ArrayList<LeftBaseSurface> surfaces
                = extractLeftBasicSurfaces(fullTops.getLeftTops());

        for (LeftBaseSurface baseSurface : surfaces) {
            ArrayList<RotationSurface> rs
                    = rotationSurfaces.get(baseSurface.getWidth());
            if (rs != null) {
                for (RotationSurface rotation : rs) {
                    long[] grid = rotation.getGridFromBaseSurface(baseSurface,
                            matrix.getGrid()[Matrix.LEFT_SURFACE_INDEX]);
                    if (grid != null) {
                        ZTStackingMove move = new ZTStackingMove(
                                new long[][]{
                                    grid,
                                    new long[2],
                                    new long[2]},
                                Matrix.LEFT_SURFACE_INDEX, active,
                                matrix.isHoldUsed());
                        moves.add(move);
                    }
                }
            }
        }

        return moves;
    }

    private boolean isInvalid(Tetromino type,
            ZTStackingMoveGenerator generator) {
        return (type == Tetromino.T_TYPE
                && !generator.getRightGenerator().allowLMT())
                || (type == Tetromino.Z_TYPE
                && !generator.getRightGenerator().allowLMZ());
    }

    @Override
    public void update(Matrix matrix, Move move, MoveHistory history) {
    }

    @Override
    public void revert(Matrix matrix, Move move, MoveHistory history) {
    }

    private ArrayList<LeftBaseSurface> extractLeftBasicSurfaces(int[] ltops) {
        ArrayList<LeftBaseSurface> basicSurfaces = new ArrayList<>();

        int width = 1;
        int index = 0;
        for (int i = 1; i < ltops.length; i++) {
            if (ltops[i] != ltops[i - 1]) {
                LeftBaseSurface basicSurface = new LeftBaseSurface(
                        width, ltops[i - 1], index);
                basicSurfaces.add(basicSurface);
                ArrayList<LeftBaseSurface> children
                        = basicSurface.generateChildrenSurfaces();
                if (children != null) {
                    basicSurfaces.addAll(children);
                }

                width = 1;
                index = i;
            } else {
                width++;
            }
        }

        LeftBaseSurface basicSurface = new LeftBaseSurface(
                width, ltops[ltops.length - 1], index);
        basicSurfaces.add(basicSurface);
        ArrayList<LeftBaseSurface> children
                = basicSurface.generateChildrenSurfaces();
        if (children != null) {
            basicSurfaces.addAll(children);
        }

        return basicSurfaces;
    }
}
