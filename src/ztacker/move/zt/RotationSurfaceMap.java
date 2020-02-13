package ztacker.move.zt;

import java.util.ArrayList;
import java.util.HashMap;
import ztacker.tetromino.Tetromino;

public final class RotationSurfaceMap {

    /**
     * Mapping of different tetromino shapes to their respective rotations
     */
    private static final HashMap<Tetromino, HashMap<Integer, 
            ArrayList<RotationSurface>>> VALUES = new HashMap<>();

    static {
        initRotationSurfaces();
    }

    private RotationSurfaceMap() {
    }

    private static void initRotationSurfaces() {
        initIRotationSurface();
        initJRotationSurface();
        initLRotationSurface();
        initORotationSurface();
        initSRotationSurface();
        initTRotationSurface();
        initZRotationSurface();
    }

    /**
     * Initializes the set of rotations for the I tetromino and puts them in an
     * <tt>ArrayList</tt> to be added to the main rotation map.
     */
    private static void initIRotationSurface() {
        HashMap<Integer, ArrayList<RotationSurface>> rotationSurfaces
                = new HashMap<>();

        ArrayList<RotationSurface> width1 = new ArrayList<>();
        //Represents the vertical rotation of the I piece like so:
        //1000 
        //1000  No fill is required and the index range is from 0 to 3
        //1000  (inclusive). The rotation has a base width of 1.
        //1000
        width1.add(new RotationSurface(0b1000_1000_1000_1000L, 0L, 0, 3));
        rotationSurfaces.put(1, width1);

        ArrayList<RotationSurface> width4 = new ArrayList<>();
        //Represents the horizontal rotation of the I piece like so:
        //0000 
        //0000  No fill is required and the index range is 0. The rotation
        //0000  has a base width of 4.
        //1111
        width4.add(new RotationSurface(0b0000_0000_0000_1111L, 0L, 0, 0));
        rotationSurfaces.put(4, width4);

        VALUES.put(Tetromino.I_TYPE, rotationSurfaces);
    }

    /**
     * Initializes the set of rotations for the J tetromino and puts them in an
     * <tt>ArrayList</tt> to be added to the main rotation map.
     */
    private static void initJRotationSurface() {
        HashMap<Integer, ArrayList<RotationSurface>> rotationSurfaces
                = new HashMap<>();

        ArrayList<RotationSurface> width1 = new ArrayList<>();
        //Represents the vertical rotation of the J piece like so:
        //0000  
        //1100  With fill represented by the 2s and the index range is from 0 
        //1200  to 2 (inclusive). The rotation has a base width of 1.
        //1200
        width1.add(new RotationSurface(
                0b0000_1100_1000_1000L,
                0b0000_0000_0100_0100L, 0, 2));
        //Represents the horizontal rotation of the J piece like so:
        //0000  
        //0000  With fill represented by the 2s and the index range is from 2 
        //1110  to 3 (inclusive). The rotation has a base width of 1.
        //2210
        width1.add(new RotationSurface(
                0b0000_0000_1110_0010L,
                0b0000_0000_0000_1100L, 2, 3));
        rotationSurfaces.put(1, width1);

        ArrayList<RotationSurface> width2 = new ArrayList<>();
        width2.add(new RotationSurface(0b0000_0100_0100_1100L, 0L, 0, 2));
        rotationSurfaces.put(2, width2);

        ArrayList<RotationSurface> width3 = new ArrayList<>();
        width3.add(new RotationSurface(0b0000_0000_1000_1110L, 0L, 0, 1));
        rotationSurfaces.put(3, width3);

        VALUES.put(Tetromino.J_TYPE, rotationSurfaces);
    }

    private static void initLRotationSurface() {
        HashMap<Integer, ArrayList<RotationSurface>> rotationSurfaces
                = new HashMap<>();

        ArrayList<RotationSurface> width1 = new ArrayList<>();
        width1.add(new RotationSurface(
                0b0000_1100_0100_0100L,
                0b0000_0000_1000_1000L, 1, 3));
        width1.add(new RotationSurface(
                0b0000_0000_1110_1000L,
                0b0000_0000_0000_0110L, 0, 1));
        rotationSurfaces.put(1, width1);

        ArrayList<RotationSurface> width2 = new ArrayList<>();
        width2.add(new RotationSurface(0b0000_1000_1000_1100L, 0L, 0, 2));
        rotationSurfaces.put(2, width2);

        ArrayList<RotationSurface> width3 = new ArrayList<>();
        width3.add(new RotationSurface(0b0000_0000_0010_1110L, 0L, 0, 1));
        rotationSurfaces.put(3, width3);

        VALUES.put(Tetromino.L_TYPE, rotationSurfaces);
    }

    private static void initORotationSurface() {
        HashMap<Integer, ArrayList<RotationSurface>> rotationSurfaces
                = new HashMap<>();

        ArrayList<RotationSurface> width2 = new ArrayList<>();
        width2.add(new RotationSurface(
                0b0000_0000_1100_1100L, 0L, 0, 2));
        rotationSurfaces.put(2, width2);

        VALUES.put(Tetromino.O_TYPE, rotationSurfaces);
    }

    private static void initSRotationSurface() {
        HashMap<Integer, ArrayList<RotationSurface>> rotationSurfaces
                = new HashMap<>();

        ArrayList<RotationSurface> width1 = new ArrayList<>();
        width1.add(new RotationSurface(
                0b0000_1000_1100_0100L,
                0b0000_0000_0000_1000L, 1, 3));
        rotationSurfaces.put(1, width1);

        ArrayList<RotationSurface> width2 = new ArrayList<>();
        width2.add(new RotationSurface(
                0b0000_0000_0110_1100L,
                0b0000_0000_0000_0010L, 0, 1));
        rotationSurfaces.put(2, width2);

        VALUES.put(Tetromino.S_TYPE, rotationSurfaces);
    }

    private static void initTRotationSurface() {
        HashMap<Integer, ArrayList<RotationSurface>> rotationSurfaces
                = new HashMap<>();

        ArrayList<RotationSurface> width1 = new ArrayList<>();
        width1.add(new RotationSurface(
                0b0000_1000_1100_1000L,
                0b0000_0000_0000_0100L, 0, 2));
        width1.add(new RotationSurface(
                0b0000_0100_1100_0100L,
                0b0000_0000_0000_1000L, 1, 3));
        width1.add(new RotationSurface(
                0b0000_0000_1110_0100L,
                0b0000_0000_0000_1010L, 1, 2));
        rotationSurfaces.put(1, width1);

        ArrayList<RotationSurface> width3 = new ArrayList<>();
        width3.add(new RotationSurface(0b0000_0000_0100_1110L, 0L, 0, 1));
        rotationSurfaces.put(3, width3);

        VALUES.put(Tetromino.T_TYPE, rotationSurfaces);
    }

    private static void initZRotationSurface() {
        HashMap<Integer, ArrayList<RotationSurface>> rotationSurfaces
                = new HashMap<>();

        ArrayList<RotationSurface> width1 = new ArrayList<>();
        width1.add(new RotationSurface(
                0b0000_0100_1100_1000L,
                0b0000_0000_0000_0100L, 0, 2));
        rotationSurfaces.put(1, width1);

        ArrayList<RotationSurface> width2 = new ArrayList<>();
        width2.add(new RotationSurface(
                0b0000_0000_1100_0110L,
                0b0000_0000_0000_1000L, 1, 2));
        rotationSurfaces.put(2, width2);

        VALUES.put(Tetromino.Z_TYPE, rotationSurfaces);
    }

    public static HashMap<Integer, ArrayList<RotationSurface>>
            getRotationSurfaces(Tetromino type) {
        return VALUES.get(type);
    }
}
