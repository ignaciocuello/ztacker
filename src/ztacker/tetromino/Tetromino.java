package ztacker.tetromino;

import java.awt.Color;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;

public enum Tetromino {
    S_TYPE, Z_TYPE, I_TYPE, L_TYPE, O_TYPE, T_TYPE, J_TYPE;

    private static final HashMap<Tetromino, Tetromino> MIRROR
            = new HashMap<>();
    private static final HashMap<Tetromino, Color> COLOR_MAP
            = new HashMap<>();

    static {
        MIRROR.put(Tetromino.I_TYPE, Tetromino.I_TYPE);
        MIRROR.put(Tetromino.J_TYPE, Tetromino.L_TYPE);
        MIRROR.put(Tetromino.L_TYPE, Tetromino.J_TYPE);
        MIRROR.put(Tetromino.O_TYPE, Tetromino.O_TYPE);
        MIRROR.put(Tetromino.S_TYPE, Tetromino.Z_TYPE);
        MIRROR.put(Tetromino.Z_TYPE, Tetromino.S_TYPE);
        MIRROR.put(Tetromino.T_TYPE, Tetromino.T_TYPE);
        
        COLOR_MAP.put(Tetromino.I_TYPE, Color.CYAN);
        COLOR_MAP.put(Tetromino.J_TYPE, Color.BLUE.brighter());
        COLOR_MAP.put(Tetromino.L_TYPE, Color.ORANGE);
        COLOR_MAP.put(Tetromino.O_TYPE, Color.YELLOW);
        COLOR_MAP.put(Tetromino.S_TYPE, Color.GREEN);
        COLOR_MAP.put(Tetromino.Z_TYPE, Color.RED);
        COLOR_MAP.put(Tetromino.T_TYPE, Color.MAGENTA);
    }

    @Override
    public String toString() {
        return super.toString().replace("_TYPE", "");
    }

    public Tetromino mirror() {
        return MIRROR.get(this);
    }
    
    public Color color() {
        return COLOR_MAP.get(this);
    }
    
    public static LinkedList<Tetromino> valuesAsLinkedList() {
        return new LinkedList<>(Arrays.asList(Tetromino.values()));
    }
}
