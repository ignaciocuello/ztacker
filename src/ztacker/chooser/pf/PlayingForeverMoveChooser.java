package ztacker.chooser.pf;

import ztacker.chooser.MoveChooser;
import ztacker.eval.pf.PlayingForeverEvaluator;
import ztacker.history.pf.PlayingForeverMoveHistory;
import ztacker.move.pf.PlayingForeverMoveGenerator;

public final class PlayingForeverMoveChooser extends MoveChooser {

    public PlayingForeverMoveChooser() {
        super(new PlayingForeverMoveGenerator(), new PlayingForeverEvaluator(),
                new PlayingForeverMoveHistory());
    }

    @Override
    public MoveChooser copy() {
        return new PlayingForeverMoveChooser();
    }

    @Override
    public String toString() {
        return "Playing forever";
    }
}
