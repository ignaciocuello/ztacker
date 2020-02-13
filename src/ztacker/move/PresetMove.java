package ztacker.move;

import ztacker.matrix.GridModifier;
import ztacker.matrix.GridStringConverter;
import ztacker.matrix.Matrix;
import ztacker.robot.out.Command;
import ztacker.tetromino.mirror.MirrorConverter;
import ztacker.tetromino.Tetromino;

public final class PresetMove {

    private final long[] grid;
    private final long[] fill;

    private final long[] freeHard;
    private final long[] freeSoft;
    private final SoftDropCommands softDrop;
    
    private final Tetromino type;

    public PresetMove(long[] grid, Tetromino type) {
        this(grid, new long[Matrix.NUM_PARTIAL_GRIDS], type);
    }

    public PresetMove(long[] grid, long[] fill, Tetromino type) {
        this(grid, fill, null, type, null);
    }

    public PresetMove(long[] grid, long[] fill, long[] freeSoft,
            Tetromino type, SoftDropCommands softDrop) {
        this.grid = grid;
        this.fill = fill;
        this.freeSoft = freeSoft;
        this.type = type;
        this.softDrop = softDrop;

        this.freeHard = new long[Matrix.NUM_PARTIAL_GRIDS];
        GridModifier.setUpwardRayExtension(grid, freeHard);
    }

    public PresetMove(long[] grid, long[] fill, long[] freeSoft,
            long[] freeHard, Tetromino type,
            SoftDropCommands softDrop) {
        this.grid = grid;
        this.fill = fill;
        this.freeSoft = freeSoft;
        this.freeHard = freeHard;
        this.type = type;
        this.softDrop = softDrop;
    }

    public long[] getGrid() {
        return grid;
    }

    public long[] getFill() {
        return fill;
    }

    public long[] getFreeHard() {
        return freeHard;
    }

    public long[] getFreeSoft() {
        return freeSoft;
    }

    public Tetromino getType() {
        return type;
    }

    public PresetMove mirror() {
        MirrorConverter mirrorConverter = new MirrorConverter();
        long[] mgrid = mirrorConverter.convert(grid);
        long[] mfill = mirrorConverter.convert(fill);
        long[] mfreeSoft = mirrorConverter.convert(freeSoft);

        Tetromino mtype = type.mirror();

        return new PresetMove(mgrid, mfill, mfreeSoft, mtype, 
                mirrorSoftDropCommand(softDrop, mirrorConverter));
    }
    
    private SoftDropCommands mirrorSoftDropCommand(
            SoftDropCommands softDrop,
            MirrorConverter mirrorConverter) {
        if (softDrop != null) {
            long[] norm = mirrorConverter.convert(softDrop.getNorm());
            Command com = softDrop.getCommand().mirror();
            
            return new SoftDropCommands(norm, com);
        }
        
        return null;
    }
    
    public SoftDropCommands getSoftDrop() {
        return softDrop;
    }
}
