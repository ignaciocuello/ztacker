package ztacker.move;

import java.util.ArrayList;
import ztacker.history.MoveHistory;
import ztacker.matrix.Matrix;

public abstract class MoveGenerator {

    public abstract ArrayList<Move> generateMoves(Matrix matrix,
            MoveHistory history, FullSurfaceTops fullTops);

    public abstract void update(Matrix matrix, Move move,
            MoveHistory history);

    public abstract void revert(Matrix matrix, Move move,
            MoveHistory history);

    public final ArrayList<Move> generateMoves(Matrix matrix, 
            MoveHistory history) {
        return generateMoves(matrix, history, 
                FullSurfaceTops.constructFullSurfaceTop(matrix));
    }

    public boolean isToppedOutAtTetrominoSpawn(FullSurfaceTops fullTops) {
        return fullTops.getLeftTops()[Matrix.PARTIAL_GRID_WIDTH - 1]
                >= Matrix.MAX_REAL_HEIGHT - 1
                || fullTops.getMidMax() >= Matrix.MAX_REAL_HEIGHT - 1;
    }

}
