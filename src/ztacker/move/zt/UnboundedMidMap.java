package ztacker.move.zt;

import java.util.ArrayList;
import java.util.HashMap;
import ztacker.tetromino.Tetromino;

public final class UnboundedMidMap {

    private static final int INVALID = MidSurfaceDiscriminator.INVALID;

    private static final MidSurfaceDiscriminator FLAT_TOP;

    private static final MidSurfaceDiscriminator S_TOP;
    private static final MidSurfaceDiscriminator O_TOP;
    private static final MidSurfaceDiscriminator Z_TOP;
    private static final MidSurfaceDiscriminator L_TOP;

    private static final MidSurfaceDiscriminator OS_TOP;
    private static final MidSurfaceDiscriminator OI_TOP;
    private static final MidSurfaceDiscriminator OSO_TOP;

    private static final HashMap<Tetromino, ArrayList<MidSurfacePair>> VALUES;
    private static final HashMap<MidSurfaceDiscriminator, Tetromino> 
            REVERSE_VALUES;
    
    static {
        VALUES = new HashMap<>();
        REVERSE_VALUES = new HashMap<>();
        
        FLAT_TOP = initFlatTop();
        S_TOP = initSTop();
        O_TOP = initOTop();
        Z_TOP = initZTop();
        L_TOP = initLTop();
        OS_TOP = initOSTop();
        OI_TOP = initOITop();
        OSO_TOP = initOSOTop();

        initValues();
    }

    private UnboundedMidMap() {
    }

    private static MidSurfaceDiscriminator initFlatTop() {
        return (ltops, mtops) -> {
            return (mtops[0] == mtops[1] && mtops[1] == mtops[2])
                    ? mtops[0] : INVALID;
        };
    }

    private static MidSurfaceDiscriminator initSTop() {
        return (ltops, mtops) -> {
            return ((mtops[1] - mtops[0]) == 1 && mtops[1] == mtops[2])
                    ? mtops[0] : INVALID;
        };
    }

    private static MidSurfaceDiscriminator initOTop() {
        return (ltops, mtops) -> {
            return (mtops[0] == mtops[1] && mtops[1] - mtops[2] >= 1
                    && mtops[1] - mtops[2] < 3)
                            ? mtops[0] - 1 : INVALID;
        };
    }

    private static MidSurfaceDiscriminator initZTop() {
        return (ltops, mtops) -> {
            return (mtops[1] - mtops[0] == 2 && mtops[2] - mtops[1] == 1)
                    ? mtops[0] : INVALID;
        };
    }

    private static MidSurfaceDiscriminator initLTop() {
        return (ltops, mtops) -> {
            return (mtops[0] - mtops[1] == 2 && mtops[1] - mtops[2] <= 1)
                    ? mtops[1] : INVALID;
        };
    }

    private static MidSurfaceDiscriminator initOSTop() {
        return (ltops, mtops) -> {
            return (mtops[1] - mtops[0] == 2 && mtops[1] - mtops[2] == 1)
                    ? mtops[0] : INVALID;
        };
    }

    private static MidSurfaceDiscriminator initOITop() {
        return (ltops, mtops) -> {
            return (mtops[0] - mtops[1] == 4 && mtops[1] - mtops[2] >= 1)
                    ? mtops[1] - 1 : INVALID;
        };
    }

    private static MidSurfaceDiscriminator initOSOTop() {
        return (ltops, mtops) -> {
            return (mtops[1] - mtops[0] == 4 && mtops[1] == mtops[2])
                    ? mtops[0] : INVALID;
        };
    }

    private static void initValues() {
        REVERSE_VALUES.put(S_TOP, Tetromino.L_TYPE);
        REVERSE_VALUES.put(O_TOP, Tetromino.J_TYPE);
        REVERSE_VALUES.put(Z_TOP, Tetromino.J_TYPE);
        REVERSE_VALUES.put(L_TOP, Tetromino.O_TYPE);
        REVERSE_VALUES.put(OSO_TOP, Tetromino.I_TYPE);
        
        ArrayList<MidSurfacePair> S_LIST = new ArrayList<>();
        S_LIST.add(new MidSurfacePair(FLAT_TOP, genGrid(0b0110_1100L)));
        S_LIST.add(new MidSurfacePair(O_TOP, genGrid(0b0100_0110_0010L)));
        S_LIST.add(new MidSurfacePair(OI_TOP, genGrid(0b0100_0110_0010L)));

        ArrayList<MidSurfacePair> O_LIST = new ArrayList<>();
        O_LIST.add(new MidSurfacePair(FLAT_TOP, genGrid(0b1100_1100L)));
        O_LIST.add(new MidSurfacePair(L_TOP, genGrid(0b0110_0110L)));
        O_LIST.add(new MidSurfacePair(OS_TOP, genGrid(0b0110_0110_0000_0000L)));

        ArrayList<MidSurfacePair> L_LIST = new ArrayList<>();
        L_LIST.add(new MidSurfacePair(S_TOP, genGrid(0b1110_1000L)));
        L_LIST.add(new MidSurfacePair(FLAT_TOP, genGrid(0b1000_1000_1100L)));

        ArrayList<MidSurfacePair> Z_LIST = new ArrayList<>();
        Z_LIST.add(new MidSurfacePair(FLAT_TOP, genGrid(0b0010_0110_0100L)));

        ArrayList<MidSurfacePair> J_LIST = new ArrayList<>();
        J_LIST.add(new MidSurfacePair(O_TOP, genGrid(0b1110_0010L)));
        J_LIST.add(new MidSurfacePair(Z_TOP, genGrid(0b1100_1000_1000L)));
        J_LIST.add(new MidSurfacePair(OS_TOP, genGrid(0b1100_1000_1000L)));

        ArrayList<MidSurfacePair> I_LIST = new ArrayList<>();
        I_LIST.add(new MidSurfacePair(OS_TOP,
                genGrid(0b1000_1000_1000_1000L)));
        I_LIST.add(new MidSurfacePair(O_TOP,
                genGrid(0b1000_1000_1000_1000_0000L)));
        I_LIST.add(new MidSurfacePair(OSO_TOP,
                genGrid(0b1000_1000_1000_1000L)));

        VALUES.put(Tetromino.S_TYPE, S_LIST);
        VALUES.put(Tetromino.O_TYPE, O_LIST);
        VALUES.put(Tetromino.L_TYPE, L_LIST);
        VALUES.put(Tetromino.Z_TYPE, Z_LIST);
        VALUES.put(Tetromino.J_TYPE, J_LIST);
        VALUES.put(Tetromino.I_TYPE, I_LIST);
    }

    private static long[][] genGrid(long cbase) {
        return new long[][]{
            {0L, 0L},
            {cbase, 0L},
            {0L, 0L}
        };
    }

    public static ArrayList<MidSurfacePair> getUnboundedMidMapping(
            Tetromino type) {
        return VALUES.get(type);
    }

    public static MidSurfaceDiscriminator getFlatTop() {
        return FLAT_TOP;
    }
    
    public static MidSurfaceDiscriminator getSTop() {
        return S_TOP;
    }
    
    public static Tetromino getCappingTetromino(int[] ltops, int[] mtops) {
        for (MidSurfaceDiscriminator disc : REVERSE_VALUES.keySet()) {
            if (disc.getBaseIndex(ltops, mtops) 
                    != MidSurfaceDiscriminator.INVALID) {
                return REVERSE_VALUES.get(disc);
            }
        }
        
        return null;
    }
}
