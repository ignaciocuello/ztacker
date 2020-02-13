package ztacker.chooser.zt;

import ztacker.chooser.MoveChooser;
import ztacker.eval.zt.ZTStackingEvaluator;
import ztacker.history.zt.ZTStackingMoveHistory;
import ztacker.move.zt.ZTStackingMoveGenerator;

public final class ZTStackingMoveChooser extends MoveChooser {
    
    public ZTStackingMoveChooser() {
        super(new ZTStackingMoveGenerator(), new ZTStackingEvaluator(), 
                new ZTStackingMoveHistory());
    }

    @Override
    public MoveChooser copy() {
        return new ZTStackingMoveChooser();
    }
    
    @Override
    public String toString() {
        return "ST/ZT stacking";
    }
}
