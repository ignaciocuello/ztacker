package ztacker.move.zt;

import ztacker.move.FullSurfaceTops;
import java.util.ArrayList;
import ztacker.history.MoveHistory;
import ztacker.matrix.GridModifier;
import ztacker.matrix.Matrix;
import ztacker.move.Move;
import ztacker.move.MoveGenerator;
import ztacker.robot.out.HalfGrid;
import ztacker.robot.out.MoveConverter;

public final class ZTStackingMoveGenerator extends MoveGenerator {

    private final LeftSurfaceMoveGenerator leftGenerator;
    private final MidSurfaceMoveGenerator midGenerator;
    private final RightSurfaceMoveGenerator rightGenerator;

    private final OpeningMoveGenerator openingGenerator;

    public ZTStackingMoveGenerator() {
        openingGenerator = new OpeningMoveGenerator();

        leftGenerator = new LeftSurfaceMoveGenerator();
        midGenerator = new MidSurfaceMoveGenerator(openingGenerator);
        rightGenerator = new RightSurfaceMoveGenerator();
    }

    @Override
    public ArrayList<Move> generateMoves(Matrix matrix, MoveHistory history,
            FullSurfaceTops fullTops) {
        ArrayList<Move> moves = new ArrayList<>();
        if (isToppedOutAtTetrominoSpawn(fullTops)) {
            return moves;
        }

        moves.addAll(openingGenerator.
                getMoveList(matrix, fullTops, history, this));
        moves.addAll(leftGenerator.getMoveList(
                matrix, fullTops, history, this));
        moves.addAll(midGenerator.getMoveList(
                matrix, fullTops, history, this));
        moves.addAll(rightGenerator.getMoveList(
                matrix, fullTops, history, this));
        moves.removeIf(m -> {
            long[] norm = GridModifier.normalizeGrid(m.getGrid());
            if (MoveConverter.FREE_MAP.get(m.getType()) != null) {
                long[][] free = MoveConverter.FREE_MAP.get(m.getType()).
                        getOrDefault(new HalfGrid(norm), Matrix.createEmptyGrid());
                return !GridModifier.isFree(matrix.getGrid(), free);
            }
            
            return false;
        });

        return moves;
    }

    @Override
    public void update(Matrix matrix, Move move, MoveHistory history) {
        openingGenerator.update(matrix, move, history);

        leftGenerator.update(matrix, move, history);

        midGenerator.update(matrix, move, history);

        rightGenerator.update(matrix, move, history);
    }

    @Override
    public void revert(Matrix matrix, Move move, MoveHistory history) {
        openingGenerator.revert(matrix, move, history);

        leftGenerator.revert(matrix, move, history);

        midGenerator.revert(matrix, move, history);

        rightGenerator.revert(matrix, move, history);
    }

    public LeftSurfaceMoveGenerator getLeftGenerator() {
        return leftGenerator;
    }

    public MidSurfaceMoveGenerator getMidGenerator() {
        return midGenerator;
    }

    public RightSurfaceMoveGenerator getRightGenerator() {
        return rightGenerator;
    }

    public OpeningMoveGenerator getOpeningGenerator() {
        return openingGenerator;
    }
}
