package ztacker.robot.out;

import java.util.HashMap;
import ztacker.matrix.GridModifier;
import ztacker.matrix.Matrix;
import ztacker.move.Move;
import ztacker.tetromino.mirror.MirrorConverter;
import ztacker.tetromino.Tetromino;

public final class MoveConverter {

    private static final HashMap<Tetromino, HashMap<HalfGrid, Command[]>> 
            FINESSE_MAP = new HashMap<>();
    public static final HashMap<Tetromino, HashMap<HalfGrid, long[][]>> 
            FREE_MAP = new HashMap<>();

    private final Move move;

    static {
        initFinesseMap();
    }

    public MoveConverter(Move move) {
        this.move = move;
    }

    public Instruction[] convert(boolean mirror, Matrix matrix) {
        if (!move.isSoftDrop()) {
            long[] norm = GridModifier.normalizeGrid(move.getGrid());

            Tetromino type = move.getType();
            if (mirror) {
                MirrorConverter mirrorConverter = new MirrorConverter();
                norm = mirrorConverter.convert(norm);
                type = type.mirror();
            }
            CommandConverter cc = new CommandConverter(
                    FINESSE_MAP.get(type).get(new HalfGrid(norm)),
                    move.isHold());
            return cc.convert();
        } else {
            long[] norm = move.getSoftDrop().getNorm();

            Tetromino type = move.getType();
            if (mirror) {
                MirrorConverter mirrorConverter = new MirrorConverter();
                norm = mirrorConverter.convert(norm);
                type = type.mirror();
            }
            Command[] commands
                    = FINESSE_MAP.get(type).get(new HalfGrid(norm));
            Command[] rcommands = new Command[commands.length + 2];
            int i;
            for (i = 0; i < commands.length; i++) {
                rcommands[i] = commands[i];
            }
            rcommands[i++] = Command.SOFT_DROP;
            rcommands[i++] = !mirror ? move.getSoftDrop().getCommand()
                    : move.getSoftDrop().getCommand().mirror();

            int softDropHeight = GridModifier.getYOffset(move.getGrid());
            CommandConverter cc = new CommandConverter(rcommands, move.isHold(),
                    softDropHeight);
            return cc.convert();
        }
    }

    private static void initFinesseMap() {
        initOMap();
        initTMap();
        initLMap();
        initJMap();
        initSMap();
        initZMap();
        initIMap();
    }

    private static void initOMap() {
        HashMap<HalfGrid, Command[]> typeMap = new HashMap<>();
        FINESSE_MAP.put(Tetromino.O_TYPE, typeMap);

        HashMap<HalfGrid, long[][]> freeMap = new HashMap<>();
        FREE_MAP.put(Tetromino.O_TYPE, freeMap);

        HalfGrid a1 = new HalfGrid(new long[]{0b1100_1100L, 0L, 0L});
        typeMap.put(a1, new Command[]{Command.DAS_LEFT});
        freeMap.put(a1, createFreeGrid(0, 4, 1));

        HalfGrid a2 = new HalfGrid(new long[]{0b0110_0110L, 0L, 0L});
        typeMap.put(a2, new Command[]{Command.DAS_LEFT, Command.TAP_RIGHT});
        freeMap.put(a2, createFreeGrid(1, 4, 1));

        HalfGrid a3 = new HalfGrid(new long[]{0b0011_0011L, 0L, 0L});
        typeMap.put(a3, new Command[]{Command.TAP_LEFT, Command.TAP_LEFT});
        freeMap.put(a3, createFreeGrid(2, 4, 1));

        HalfGrid a4 = new HalfGrid(new long[]{0b0001_0001L, 0b1000_1000L, 0L});
        typeMap.put(a4, new Command[]{Command.TAP_LEFT});
        freeMap.put(a4, createFreeGrid(3, 4, 1));

        typeMap.put(new HalfGrid(new long[]{0L, 0b1100_1100L, 0L}),
                new Command[]{});
        typeMap.put(new HalfGrid(new long[]{0L, 0b0110_0110L, 0L}),
                new Command[]{Command.TAP_RIGHT});
        typeMap.put(new HalfGrid(new long[]{0L, 0b0010_0010L, 0b1000_1000L}),
                new Command[]{Command.TAP_RIGHT, Command.TAP_RIGHT});
        typeMap.put(new HalfGrid(new long[]{0L, 0L, 0b1100_1100L}),
                new Command[]{Command.DAS_RIGHT, Command.TAP_LEFT});
        typeMap.put(new HalfGrid(new long[]{0L, 0L, 0b0110_0110L}),
                new Command[]{Command.DAS_RIGHT});
    }

    private static void initTMap() {
        HashMap<HalfGrid, Command[]> typeMap = new HashMap<>();
        FINESSE_MAP.put(Tetromino.T_TYPE, typeMap);

        HashMap<HalfGrid, long[][]> freeMap = new HashMap<>();
        FREE_MAP.put(Tetromino.T_TYPE, freeMap);

        initTOrientationA(typeMap, freeMap);
        initTOrientationB(typeMap, freeMap);
        initTOrientationC(typeMap, freeMap);
        initTOrientationD(typeMap, freeMap);
    }

    private static void initTOrientationA(
            HashMap<HalfGrid, Command[]> typeMap,
            HashMap<HalfGrid, long[][]> freeMap) {
        HalfGrid a1 = new HalfGrid(new long[]{0b0100_1110L, 0L, 0L});
        typeMap.put(a1, new Command[]{Command.DAS_LEFT});
        freeMap.put(a1, createFreeGrid(0, 3, 1));

        HalfGrid a2 = new HalfGrid(new long[]{0b0010_0111L, 0L, 0L});
        typeMap.put(a2, new Command[]{Command.TAP_LEFT, Command.TAP_LEFT});
        freeMap.put(a2, createFreeGrid(1, 3, 1));

        HalfGrid a3 = new HalfGrid(new long[]{0b0001_0011L, 0b1000L, 0L});
        typeMap.put(a3, new Command[]{Command.TAP_LEFT});
        freeMap.put(a3, createFreeGrid(2, 3, 1));

        HalfGrid a4 = new HalfGrid(new long[]{0b0001L, 0b1000_1100L, 0L});
        typeMap.put(a4, new Command[]{});

        typeMap.put(new HalfGrid(new long[]{0L, 0b0100_1110L, 0L}),
                new Command[]{Command.TAP_RIGHT});
        typeMap.put(new HalfGrid(new long[]{0L, 0b0010_0110L, 0b1000L}),
                new Command[]{Command.TAP_RIGHT, Command.TAP_RIGHT});
        typeMap.put(new HalfGrid(new long[]{0L, 0b0010L, 0b1000_1100L}),
                new Command[]{Command.DAS_RIGHT, Command.TAP_LEFT});
        typeMap.put(new HalfGrid(new long[]{0L, 0L, 0b0100_1110L}),
                new Command[]{Command.DAS_RIGHT});
    }

    private static void initTOrientationB(
            HashMap<HalfGrid, Command[]> typeMap,
            HashMap<HalfGrid, long[][]> freeMap) {
        HalfGrid b1 = new HalfGrid(new long[]{0b1000_1100_1000L, 0L, 0L});
        typeMap.put(b1, new Command[]{Command.ROTATE_CW, Command.DAS_LEFT});
        freeMap.put(b1, createFreeGrid(0, 4, 2));

        HalfGrid b2 = new HalfGrid(new long[]{0b0100_0110_0100L, 0L, 0L});
        typeMap.put(b2, new Command[]{Command.DAS_LEFT, Command.ROTATE_CW});
        freeMap.put(b2, createFreeGrid(1, 4, 2));

        HalfGrid b3 = new HalfGrid(new long[]{0b0010_0011_0010L, 0L, 0L});
        typeMap.put(b3,
                new Command[]{Command.ROTATE_CW, Command.TAP_LEFT, Command.TAP_LEFT});
        freeMap.put(b3, createFreeGrid(2, 4, 2));

        HalfGrid b4 = new HalfGrid(new long[]{0b0001_0001_0001L, 0b1000_0000L, 0L});
        typeMap.put(b4, new Command[]{Command.ROTATE_CW, Command.TAP_LEFT});
        freeMap.put(b4, createFreeGrid(3, 4, 2));

        typeMap.put(new HalfGrid(new long[]{0L, 0b1000_1100_1000L, 0L}),
                new Command[]{Command.ROTATE_CW});
        typeMap.put(new HalfGrid(new long[]{0L, 0b0100_0110_0100L, 0L}),
                new Command[]{Command.ROTATE_CW, Command.TAP_RIGHT});
        typeMap.put(new HalfGrid(new long[]{0L, 0b0010_0010_0010L, 0b1000_0000L}),
                new Command[]{Command.ROTATE_CW, Command.TAP_RIGHT, Command.TAP_RIGHT});
        typeMap.put(new HalfGrid(new long[]{0L, 0L, 0b1000_1100_1000L}),
                new Command[]{Command.ROTATE_CW, Command.DAS_RIGHT,
                    Command.TAP_LEFT});
        typeMap.put(new HalfGrid(new long[]{0L, 0L, 0b0100_0110_0100L}),
                new Command[]{Command.DAS_RIGHT, Command.ROTATE_CW});
    }

    private static void initTOrientationC(
            HashMap<HalfGrid, Command[]> typeMap,
            HashMap<HalfGrid, long[][]> freeMap) {
        HalfGrid c1 = new HalfGrid(new long[]{0b0100_1100_0100L, 0L, 0L});
        typeMap.put(c1, new Command[]{Command.ROTATE_CCW, Command.DAS_LEFT});
        freeMap.put(c1, createFreeGrid(1, 4, 2));

        HalfGrid c2 = new HalfGrid(new long[]{0b0010_0110_0010L, 0L, 0L});
        typeMap.put(c2,
                new Command[]{Command.ROTATE_CCW, Command.TAP_LEFT, Command.TAP_LEFT});
        freeMap.put(c2, createFreeGrid(2, 4, 2));

        HalfGrid c3 = new HalfGrid(new long[]{0b0001_0011_0001L, 0L, 0L});
        typeMap.put(c3, new Command[]{Command.ROTATE_CCW, Command.TAP_LEFT});
        freeMap.put(c3, createFreeGrid(3, 4, 2));

        HalfGrid c4 = new HalfGrid(new long[]{0b0001_0000L, 0b1000_1000_1000L, 0L});
        typeMap.put(c4, new Command[]{Command.ROTATE_CCW});

        typeMap.put(new HalfGrid(new long[]{0L, 0b0100_1100_0100L, 0L}),
                new Command[]{Command.ROTATE_CCW, Command.TAP_RIGHT});
        typeMap.put(new HalfGrid(new long[]{0L, 0b0010_0110_0010L, 0L}),
                new Command[]{Command.ROTATE_CCW, Command.TAP_RIGHT, Command.TAP_RIGHT});
        typeMap.put(new HalfGrid(new long[]{0L, 0b0010_0000L, 0b1000_1000_1000L}),
                new Command[]{Command.DAS_RIGHT, Command.TAP_LEFT,
                    Command.ROTATE_CCW});
        typeMap.put(new HalfGrid(new long[]{0L, 0L, 0b0100_1100_0100L}),
                new Command[]{Command.DAS_RIGHT, Command.ROTATE_CCW});
        typeMap.put(new HalfGrid(new long[]{0L, 0L, 0b0010_0110_0010L}),
                new Command[]{Command.ROTATE_CCW, Command.DAS_RIGHT});
    }

    private static void initTOrientationD(
            HashMap<HalfGrid, Command[]> typeMap,
            HashMap<HalfGrid, long[][]> freeMap) {
        HalfGrid d1 = new HalfGrid(new long[]{0b1110_0100L, 0L, 0L});
        typeMap.put(d1,
                new Command[]{Command.DAS_LEFT, Command.ROTATE_CW, Command.ROTATE_CW});
        freeMap.put(d1, createFreeGrid(0, 3, 1));

        HalfGrid d2 = new HalfGrid(new long[]{0b0111_0010L, 0L, 0L});
        typeMap.put(d2,
                new Command[]{Command.TAP_LEFT, Command.TAP_LEFT, Command.ROTATE_CW, Command.ROTATE_CW});
        freeMap.put(d2, createFreeGrid(1, 3, 1));

        HalfGrid d3 = new HalfGrid(new long[]{0b0011_0001L, 0b1000_0000L, 0L});
        typeMap.put(d3,
                new Command[]{Command.TAP_LEFT, Command.ROTATE_CW, Command.ROTATE_CW});
        freeMap.put(d3, createFreeGrid(2, 3, 1));

        typeMap.put(new HalfGrid(new long[]{0b0001_0000L, 0b1100_1000L, 0L}),
                new Command[]{Command.ROTATE_CW, Command.ROTATE_CW});
        typeMap.put(new HalfGrid(new long[]{0L, 0b1110_0100L, 0L}),
                new Command[]{Command.TAP_RIGHT, Command.ROTATE_CW, Command.ROTATE_CW});
        typeMap.put(new HalfGrid(new long[]{0L, 0b0110_0010L, 0b1000_0000L}),
                new Command[]{Command.TAP_RIGHT, Command.TAP_RIGHT,
                    Command.ROTATE_CW, Command.ROTATE_CW});
        typeMap.put(new HalfGrid(new long[]{0L, 0b0010_0000L, 0b1100_1000L}),
                new Command[]{Command.DAS_RIGHT, Command.TAP_LEFT,
                    Command.ROTATE_CW, Command.ROTATE_CW});
        typeMap.put(new HalfGrid(new long[]{0L, 0L, 0b1110_0100L}),
                new Command[]{Command.DAS_RIGHT, Command.ROTATE_CW, Command.ROTATE_CW});
    }

    private static void initLMap() {
        HashMap<HalfGrid, Command[]> typeMap = new HashMap<>();
        FINESSE_MAP.put(Tetromino.L_TYPE, typeMap);

        HashMap<HalfGrid, long[][]> freeMap = new HashMap<>();
        FREE_MAP.put(Tetromino.L_TYPE, freeMap);

        initLOrientationA(typeMap, freeMap);
        initLOrientationB(typeMap, freeMap);
        initLOrientationC(typeMap, freeMap);
        initLOrientationD(typeMap, freeMap);
    }

    private static void initLOrientationA(
            HashMap<HalfGrid, Command[]> typeMap,
            HashMap<HalfGrid, long[][]> freeMap) {
        HalfGrid a1 = new HalfGrid(new long[]{0b0010_1110L, 0L, 0L});
        typeMap.put(a1,
                new Command[]{Command.DAS_LEFT});
        freeMap.put(a1, createFreeGrid(0, 3, 1));

        HalfGrid a2 = new HalfGrid(new long[]{0b0001_0111L, 0L, 0L});
        typeMap.put(a2, new Command[]{Command.TAP_LEFT, Command.TAP_LEFT,});
        freeMap.put(a2, createFreeGrid(1, 3, 1));

        HalfGrid a3 = new HalfGrid(new long[]{0b0000_0011L, 0b1000_1000L, 0L});
        typeMap.put(a3, new Command[]{Command.TAP_LEFT});
        freeMap.put(a3, createFreeGrid(2, 3, 1));

        typeMap.put(new HalfGrid(new long[]{0b0001L, 0b0100_1100L, 0L}),
                new Command[]{});
        typeMap.put(new HalfGrid(new long[]{0L, 0b0010_1110L, 0L}),
                new Command[]{Command.TAP_RIGHT});
        typeMap.put(new HalfGrid(new long[]{0L, 0b0110L, 0b1000_1000L}),
                new Command[]{Command.TAP_RIGHT, Command.TAP_RIGHT});
        typeMap.put(new HalfGrid(new long[]{0L, 0b0010L, 0b0100_1100L}),
                new Command[]{Command.DAS_RIGHT, Command.TAP_LEFT});
        typeMap.put(new HalfGrid(new long[]{0L, 0L, 0b0010_1110L}),
                new Command[]{Command.DAS_RIGHT});
    }

    private static void initLOrientationB(
            HashMap<HalfGrid, Command[]> typeMap,
            HashMap<HalfGrid, long[][]> freeMap) {
        HalfGrid b1 = new HalfGrid(new long[]{0b1000_1000_1100L, 0L, 0L});
        typeMap.put(b1, new Command[]{Command.ROTATE_CW, Command.DAS_LEFT});
        freeMap.put(b1, createFreeGrid(0, 4, 2));

        HalfGrid b2 = new HalfGrid(new long[]{0b0100_0100_0110L, 0L, 0L});
        typeMap.put(b2, new Command[]{Command.DAS_LEFT, Command.ROTATE_CW});
        freeMap.put(b2, createFreeGrid(0, 3, 2));

        HalfGrid b3 = new HalfGrid(new long[]{0b0010_0010_0011L, 0L, 0L});
        typeMap.put(b3,
                new Command[]{Command.ROTATE_CW, Command.TAP_LEFT, Command.TAP_LEFT});
        freeMap.put(b3, createFreeGrid(2, 4, 2));

        HalfGrid b4 = new HalfGrid(new long[]{0b0001_0001_0001L, 0b1000L, 0L});
        typeMap.put(b4,
                new Command[]{Command.ROTATE_CW, Command.TAP_LEFT});
        freeMap.put(b4, createFreeGrid(3, 4, 2));

        typeMap.put(new HalfGrid(new long[]{0L, 0b1000_1000_1100L, 0L}),
                new Command[]{Command.ROTATE_CW});
        typeMap.put(new HalfGrid(new long[]{0L, 0b0100_0100_0110L, 0L}),
                new Command[]{Command.ROTATE_CW, Command.TAP_RIGHT});
        typeMap.put(new HalfGrid(new long[]{0L, 0b0010_0010_0010L, 0b1000L}),
                new Command[]{Command.ROTATE_CW, Command.TAP_RIGHT, Command.TAP_RIGHT});
        typeMap.put(new HalfGrid(new long[]{0L, 0L, 0b1000_1000_1100L}),
                new Command[]{Command.ROTATE_CW, Command.DAS_RIGHT,
                    Command.TAP_LEFT});
        typeMap.put(new HalfGrid(new long[]{0L, 0L, 0b0100_0100_0110L}),
                new Command[]{Command.DAS_RIGHT, Command.ROTATE_CW});
    }

    private static void initLOrientationC(
            HashMap<HalfGrid, Command[]> typeMap,
            HashMap<HalfGrid, long[][]> freeMap) {
        HalfGrid c1 = new HalfGrid(new long[]{0b1100_0100_0100L, 0L, 0L});
        typeMap.put(c1, new Command[]{Command.ROTATE_CCW, Command.DAS_LEFT});
        freeMap.put(c1, createFreeGrid(1, 4, 2));

        HalfGrid c2 = new HalfGrid(new long[]{0b0110_0010_0010L, 0L, 0L});
        typeMap.put(c2,
                new Command[]{Command.ROTATE_CCW, Command.TAP_LEFT, Command.TAP_LEFT});
        freeMap.put(c2, createFreeGrid(2, 4, 2));

        HalfGrid c3 = new HalfGrid(new long[]{0b0011_0001_0001L, 0L, 0L});
        typeMap.put(c3, new Command[]{Command.ROTATE_CCW, Command.TAP_LEFT});
        freeMap.put(c3, createFreeGrid(3, 4, 2));

        typeMap.put(new HalfGrid(new long[]{0b0001_0000_0000L, 0b1000_1000_1000L, 0L}),
                new Command[]{Command.ROTATE_CCW});
        typeMap.put(new HalfGrid(new long[]{0L, 0b1100_0100_0100L, 0L}),
                new Command[]{Command.ROTATE_CCW, Command.TAP_RIGHT});
        typeMap.put(new HalfGrid(new long[]{0L, 0b0110_0010_0010L, 0L}),
                new Command[]{Command.ROTATE_CCW, Command.TAP_RIGHT, Command.TAP_RIGHT});
        typeMap.put(new HalfGrid(new long[]{0L, 0b0010_0000_0000L, 0b1000_1000_1000L}),
                new Command[]{Command.DAS_RIGHT, Command.TAP_LEFT,
                    Command.ROTATE_CCW});
        typeMap.put(new HalfGrid(new long[]{0L, 0L, 0b1100_0100_0100L}),
                new Command[]{Command.DAS_RIGHT, Command.ROTATE_CCW});
        typeMap.put(new HalfGrid(new long[]{0L, 0L, 0b0110_0010_0010L}),
                new Command[]{Command.ROTATE_CCW, Command.DAS_RIGHT});
    }

    private static void initLOrientationD(
            HashMap<HalfGrid, Command[]> typeMap,
            HashMap<HalfGrid, long[][]> freeMap) {
        HalfGrid d1 = new HalfGrid(new long[]{0b1110_1000L, 0L, 0L});
        typeMap.put(d1,
                new Command[]{Command.DAS_LEFT, Command.ROTATE_CW, Command.ROTATE_CW});
        freeMap.put(d1, createFreeGrid(0, 3, 2));

        HalfGrid d2 = new HalfGrid(new long[]{0b0111_0100L, 0L, 0L});
        typeMap.put(d2,
                new Command[]{Command.TAP_LEFT, Command.TAP_LEFT,
                    Command.ROTATE_CW, Command.ROTATE_CW});
        freeMap.put(d2, createFreeGrid(1, 3, 2));

        HalfGrid d3 = new HalfGrid(new long[]{0b0011_0010L, 0b1000_0000L, 0L});
        typeMap.put(d3,
                new Command[]{Command.TAP_LEFT, Command.ROTATE_CW, Command.ROTATE_CW});
        freeMap.put(d3, createFreeGrid(2, 3, 2));

        typeMap.put(new HalfGrid(new long[]{0b0001_0001L, 0b1100_0000L, 0L}),
                new Command[]{Command.ROTATE_CW, Command.ROTATE_CW});
        typeMap.put(new HalfGrid(new long[]{0L, 0b1110_1000L, 0L}),
                new Command[]{Command.TAP_RIGHT, Command.ROTATE_CW, Command.ROTATE_CW});
        typeMap.put(new HalfGrid(new long[]{0L, 0b0110_0100L, 0b1000_0000L}),
                new Command[]{Command.TAP_RIGHT, Command.TAP_RIGHT, Command.ROTATE_CW, Command.ROTATE_CW});
        typeMap.put(new HalfGrid(new long[]{0L, 0b0010_0010L, 0b1100_0000L}),
                new Command[]{Command.DAS_RIGHT, Command.TAP_LEFT,
                    Command.ROTATE_CW, Command.ROTATE_CW});
        typeMap.put(new HalfGrid(new long[]{0L, 0L, 0b1110_1000L}),
                new Command[]{Command.DAS_RIGHT, Command.ROTATE_CW, Command.ROTATE_CW});
    }

    private static void initJMap() {
        HashMap<HalfGrid, Command[]> typeMap = new HashMap<>();
        FINESSE_MAP.put(Tetromino.J_TYPE, typeMap);

        HashMap<HalfGrid, long[][]> freeMap = new HashMap<>();
        FREE_MAP.put(Tetromino.J_TYPE, freeMap);

        initJOrientationA(typeMap, freeMap);
        initJOrientationB(typeMap, freeMap);
        initJOrientationC(typeMap, freeMap);
        initJOrientationD(typeMap, freeMap);
    }

    private static void initJOrientationA(
            HashMap<HalfGrid, Command[]> typeMap,
            HashMap<HalfGrid, long[][]> freeMap) {
        HalfGrid a1 = new HalfGrid(new long[]{0b1000_1110L, 0L, 0L});
        typeMap.put(a1, new Command[]{Command.DAS_LEFT});
        freeMap.put(a1, createFreeGrid(0, 3, 1));

        HalfGrid a2 = new HalfGrid(new long[]{0b0100_0111L, 0L, 0L});
        typeMap.put(a2, new Command[]{Command.TAP_LEFT, Command.TAP_LEFT});
        freeMap.put(a2, createFreeGrid(1, 3, 1));

        HalfGrid a3 = new HalfGrid(new long[]{0b0010_0011L, 0b1000L, 0L});
        typeMap.put(a3, new Command[]{Command.TAP_LEFT});
        freeMap.put(a3, createFreeGrid(2, 3, 1));

        typeMap.put(new HalfGrid(new long[]{0b0001_0001L, 0b1100L, 0L}),
                new Command[]{});
        typeMap.put(new HalfGrid(new long[]{0L, 0b1000_1110L, 0L}),
                new Command[]{Command.TAP_RIGHT});
        typeMap.put(new HalfGrid(new long[]{0L, 0b0100_0110L, 0b1000L}),
                new Command[]{Command.TAP_RIGHT, Command.TAP_RIGHT});
        typeMap.put(new HalfGrid(new long[]{0L, 0b0010_0010L, 0b1100L}),
                new Command[]{Command.DAS_RIGHT, Command.TAP_LEFT});
        typeMap.put(new HalfGrid(new long[]{0L, 0L, 0b1000_1110L}),
                new Command[]{Command.DAS_RIGHT});
    }

    private static void initJOrientationB(
            HashMap<HalfGrid, Command[]> typeMap,
            HashMap<HalfGrid, long[][]> freeMap) {
        HalfGrid b1 = new HalfGrid(new long[]{0b0100_0100_1100L, 0L, 0L});
        typeMap.put(b1, new Command[]{Command.ROTATE_CCW, Command.DAS_LEFT});
        freeMap.put(b1, createFreeGrid(0, 3, 2));

        HalfGrid b2 = new HalfGrid(new long[]{0b0010_0010_0110L, 0L, 0L});
        typeMap.put(b2,
                new Command[]{Command.ROTATE_CCW, Command.TAP_LEFT, Command.TAP_LEFT});
        freeMap.put(b2, createFreeGrid(1, 3, 2));

        HalfGrid b3 = new HalfGrid(new long[]{0b0001_0001_0011L, 0L, 0L});
        typeMap.put(b3, new Command[]{Command.ROTATE_CCW, Command.TAP_LEFT});
        freeMap.put(b3, createFreeGrid(2, 3, 2));

        typeMap.put(new HalfGrid(new long[]{0b0001L, 0b1000_1000_1000L, 0L}),
                new Command[]{Command.ROTATE_CCW});
        typeMap.put(new HalfGrid(new long[]{0L, 0b0100_0100_1100L, 0L}),
                new Command[]{Command.ROTATE_CCW, Command.TAP_RIGHT});
        typeMap.put(new HalfGrid(new long[]{0L, 0b0010_0010_0110L, 0L}),
                new Command[]{Command.ROTATE_CCW, Command.TAP_RIGHT, Command.TAP_RIGHT});
        typeMap.put(new HalfGrid(new long[]{0L, 0b0010L, 0b1000_1000_1000L}),
                new Command[]{Command.DAS_RIGHT, Command.TAP_LEFT,
                    Command.ROTATE_CCW});
        typeMap.put(new HalfGrid(new long[]{0L, 0L, 0b0100_0100_1100L}),
                new Command[]{Command.DAS_RIGHT, Command.ROTATE_CCW});
        typeMap.put(new HalfGrid(new long[]{0L, 0L, 0b0010_0010_0110L}),
                new Command[]{Command.ROTATE_CCW, Command.DAS_RIGHT});
    }

    private static void initJOrientationC(
            HashMap<HalfGrid, Command[]> typeMap,
            HashMap<HalfGrid, long[][]> freeMap) {
        HalfGrid c1 = new HalfGrid(new long[]{0b1100_1000_1000L, 0L, 0L});
        typeMap.put(c1, new Command[]{Command.ROTATE_CW, Command.DAS_LEFT});
        freeMap.put(c1, createFreeGrid(0, 4, 2));

        HalfGrid c2 = new HalfGrid(new long[]{0b0110_0100_0100L, 0L, 0L});
        typeMap.put(c2, new Command[]{Command.DAS_LEFT, Command.ROTATE_CW});
        freeMap.put(c2, createFreeGrid(1, 4, 2));

        HalfGrid c3 = new HalfGrid(new long[]{0b0011_0010_0010L, 0L, 0L});
        typeMap.put(c3,
                new Command[]{Command.ROTATE_CW, Command.TAP_LEFT, Command.TAP_LEFT});
        freeMap.put(c3, createFreeGrid(2, 4, 2));

        typeMap.put(new HalfGrid(new long[]{0b0001_0001_0001L, 0b1000_0000_0000L, 0L}),
                new Command[]{Command.ROTATE_CW, Command.TAP_LEFT});
        typeMap.put(new HalfGrid(new long[]{0L, 0b1100_1000_1000L, 0L}),
                new Command[]{Command.ROTATE_CW});
        typeMap.put(new HalfGrid(new long[]{0L, 0b0110_0100_0100L, 0L}),
                new Command[]{Command.ROTATE_CW, Command.TAP_RIGHT});
        typeMap.put(new HalfGrid(new long[]{0L, 0b0010_0010_0010L, 0b1000_0000_0000L}),
                new Command[]{Command.ROTATE_CW, Command.TAP_RIGHT, Command.TAP_RIGHT});
        typeMap.put(new HalfGrid(new long[]{0L, 0L, 0b1100_1000_1000L}),
                new Command[]{Command.ROTATE_CW, Command.DAS_RIGHT,
                    Command.TAP_LEFT});
        typeMap.put(new HalfGrid(new long[]{0L, 0L, 0b0110_0100_0100L}),
                new Command[]{Command.DAS_RIGHT, Command.ROTATE_CW});
    }

    private static void initJOrientationD(
            HashMap<HalfGrid, Command[]> typeMap,
            HashMap<HalfGrid, long[][]> freeMap) {
        HalfGrid d1 = new HalfGrid(new long[]{0b1110_0010L, 0L, 0L});
        typeMap.put(d1, new Command[]{Command.DAS_LEFT, Command.ROTATE_CW, Command.ROTATE_CW});
        freeMap.put(d1, createFreeGrid(0, 3, 2));

        HalfGrid d2 = new HalfGrid(new long[]{0b0111_0001L, 0L, 0L});
        typeMap.put(d2,
                new Command[]{Command.TAP_LEFT, Command.TAP_LEFT, Command.ROTATE_CW, Command.ROTATE_CW});
        freeMap.put(d2, createFreeGrid(1, 3, 2));

        HalfGrid d3 = new HalfGrid(new long[]{0b0011_0000L, 0b1000_1000L, 0L});
        typeMap.put(d3,
                new Command[]{Command.TAP_LEFT, Command.ROTATE_CW, Command.ROTATE_CW});
        freeMap.put(d3, createFreeGrid(2, 3, 2));

        typeMap.put(new HalfGrid(new long[]{0b0001_0000L, 0b1100_0100L, 0L}),
                new Command[]{Command.ROTATE_CW, Command.ROTATE_CW});
        typeMap.put(new HalfGrid(new long[]{0L, 0b1110_0010L, 0L}),
                new Command[]{Command.TAP_RIGHT, Command.ROTATE_CW, Command.ROTATE_CW});
        typeMap.put(new HalfGrid(new long[]{0L, 0b0110_0000L, 0b1000_1000L}),
                new Command[]{Command.TAP_RIGHT, Command.TAP_RIGHT,
                    Command.ROTATE_CW, Command.ROTATE_CW});
        typeMap.put(new HalfGrid(new long[]{0L, 0b0010_0000L, 0b1100_0100L}),
                new Command[]{Command.DAS_RIGHT, Command.TAP_LEFT,
                    Command.ROTATE_CW, Command.ROTATE_CW});
        typeMap.put(new HalfGrid(new long[]{0L, 0L, 0b1110_0010L}),
                new Command[]{Command.DAS_RIGHT, Command.ROTATE_CW, Command.ROTATE_CW});
    }

    private static void initSMap() {
        HashMap<HalfGrid, Command[]> typeMap = new HashMap<>();
        FINESSE_MAP.put(Tetromino.S_TYPE, typeMap);

        HashMap<HalfGrid, long[][]> freeMap = new HashMap<>();
        FREE_MAP.put(Tetromino.S_TYPE, freeMap);

        initSOrientationA(typeMap, freeMap);
        initSOrientationB(typeMap, freeMap);
    }

    private static void initSOrientationA(
            HashMap<HalfGrid, Command[]> typeMap,
            HashMap<HalfGrid, long[][]> freeMap) {
        HalfGrid a1 = new HalfGrid(new long[]{0b0110_1100L, 0L, 0L});
        typeMap.put(a1, new Command[]{Command.DAS_LEFT});
        freeMap.put(a1, createFreeGrid(0, 3, 1));

        HalfGrid a2 = new HalfGrid(new long[]{0b0011_0110L, 0L, 0L});
        typeMap.put(a2,
                new Command[]{Command.TAP_LEFT, Command.TAP_LEFT,});
        freeMap.put(a2, createFreeGrid(1, 3, 1));

        HalfGrid a3 = new HalfGrid(new long[]{0b0001_0011L, 0b1000_0000L, 0L});
        typeMap.put(a3, new Command[]{Command.TAP_LEFT});
        freeMap.put(a3, createFreeGrid(2, 3, 1));

        typeMap.put(new HalfGrid(new long[]{0b0001L, 0b1100_1000L, 0L}),
                new Command[]{});
        typeMap.put(new HalfGrid(new long[]{0L, 0b0110_1100L, 0L}),
                new Command[]{Command.TAP_RIGHT});
        typeMap.put(new HalfGrid(new long[]{0L, 0b0010_0110L, 0b1000_0000L}),
                new Command[]{Command.TAP_RIGHT, Command.TAP_RIGHT});
        typeMap.put(new HalfGrid(new long[]{0L, 0b0010L, 0b1100_1000L}),
                new Command[]{Command.DAS_RIGHT, Command.TAP_LEFT});
        typeMap.put(new HalfGrid(new long[]{0L, 0L, 0b0110_1100L}),
                new Command[]{Command.DAS_RIGHT});
    }

    private static void initSOrientationB(
            HashMap<HalfGrid, Command[]> typeMap,
            HashMap<HalfGrid, long[][]> freeMap) {
        HalfGrid b1 = new HalfGrid(new long[]{0b1000_1100_0100L, 0L, 0L});
        typeMap.put(b1, new Command[]{Command.ROTATE_CCW, Command.DAS_LEFT});
        freeMap.put(b1, createFreeGrid(1, 4, 2));

        HalfGrid b2 = new HalfGrid(new long[]{0b0100_0110_0010L, 0L, 0L});
        typeMap.put(b2, new Command[]{Command.DAS_LEFT, Command.ROTATE_CW});
        freeMap.put(b2, createFreeGrid(2, 4, 2));

        HalfGrid b3 = new HalfGrid(new long[]{0b0010_0011_0001L, 0L, 0L});
        typeMap.put(b3, new Command[]{Command.ROTATE_CCW, Command.TAP_LEFT});
        freeMap.put(b3, createFreeGrid(3, 4, 2));

        typeMap.put(new HalfGrid(new long[]{0b0001_0001_0000L, 0b1000_1000L, 0L}),
                new Command[]{Command.ROTATE_CCW});
        typeMap.put(new HalfGrid(new long[]{0L, 0b1000_1100_0100L, 0L}),
                new Command[]{Command.ROTATE_CW});
        typeMap.put(new HalfGrid(new long[]{0L, 0b0100_0110_0010L, 0L}),
                new Command[]{Command.ROTATE_CW, Command.TAP_RIGHT});
        typeMap.put(new HalfGrid(new long[]{0L, 0b0010_0010_0000L, 0b1000_1000L}),
                new Command[]{Command.ROTATE_CW, Command.TAP_RIGHT, Command.TAP_RIGHT});
        typeMap.put(new HalfGrid(new long[]{0L, 0L, 0b1000_1100_0100L}),
                new Command[]{Command.DAS_RIGHT, Command.ROTATE_CCW});
        typeMap.put(new HalfGrid(new long[]{0L, 0L, 0b0100_0110_0010L}),
                new Command[]{Command.ROTATE_CW, Command.DAS_RIGHT});
    }

    private static void initZMap() {
        HashMap<HalfGrid, Command[]> typeMap = new HashMap<>();
        FINESSE_MAP.put(Tetromino.Z_TYPE, typeMap);

        HashMap<HalfGrid, long[][]> freeMap = new HashMap<>();
        FREE_MAP.put(Tetromino.Z_TYPE, freeMap);

        initZOrientationA(typeMap, freeMap);
        initZOrientationB(typeMap, freeMap);
    }

    private static void initZOrientationA(
            HashMap<HalfGrid, Command[]> typeMap,
            HashMap<HalfGrid, long[][]> freeMap) {
        HalfGrid a1 = new HalfGrid(new long[]{0b1100_0110L, 0L, 0L});
        typeMap.put(a1, new Command[]{Command.DAS_LEFT});
        freeMap.put(a1, createFreeGrid(1, 4, 1));

        HalfGrid a2 = new HalfGrid(new long[]{0b0110_0011L, 0L, 0L});
        typeMap.put(a2,
                new Command[]{Command.TAP_LEFT, Command.TAP_LEFT});
        freeMap.put(a2, createFreeGrid(2, 4, 1));

        HalfGrid a3 = new HalfGrid(new long[]{0b0011_0001L, 0b1000L, 0L});
        typeMap.put(a3, new Command[]{Command.TAP_LEFT});
        freeMap.put(a3, createFreeGrid(3, 4, 1));

        typeMap.put(new HalfGrid(new long[]{0b0001_0000L, 0b1000_1100L, 0L}),
                new Command[]{});
        typeMap.put(new HalfGrid(new long[]{0L, 0b1100_0110L, 0L}),
                new Command[]{Command.TAP_RIGHT});
        typeMap.put(new HalfGrid(new long[]{0L, 0b0110_0010L, 0b1000L}),
                new Command[]{Command.TAP_RIGHT, Command.TAP_RIGHT});
        typeMap.put(new HalfGrid(new long[]{0L, 0b0010_0000L, 0b1000_1100L}),
                new Command[]{Command.DAS_RIGHT, Command.TAP_LEFT});
        typeMap.put(new HalfGrid(new long[]{0L, 0L, 0b1100_0110L}),
                new Command[]{Command.DAS_RIGHT});
    }

    private static void initZOrientationB(
            HashMap<HalfGrid, Command[]> typeMap,
            HashMap<HalfGrid, long[][]> freeMap) {
        HalfGrid b1 = new HalfGrid(new long[]{0b0100_1100_1000L, 0L, 0L});
        typeMap.put(b1, new Command[]{Command.ROTATE_CCW, Command.DAS_LEFT});
        freeMap.put(b1, createFreeGrid(0, 3, 2));

        HalfGrid b2 = new HalfGrid(new long[]{0b0010_0110_0100L, 0L, 0L});
        typeMap.put(b2,
                new Command[]{Command.DAS_LEFT, Command.ROTATE_CW});
        freeMap.put(b2, createFreeGrid(1, 3, 2));

        HalfGrid b3 = new HalfGrid(new long[]{0b0001_0011_0010L, 0L, 0L});
        typeMap.put(b3,
                new Command[]{Command.ROTATE_CCW, Command.TAP_LEFT});
        freeMap.put(b3, createFreeGrid(2, 3, 2));

        typeMap.put(new HalfGrid(new long[]{0b0001_0001L, 0b1000_1000_0000L, 0L}),
                new Command[]{Command.ROTATE_CCW});
        typeMap.put(new HalfGrid(new long[]{0L, 0b0100_1100_1000L, 0L}),
                new Command[]{Command.ROTATE_CW});
        typeMap.put(new HalfGrid(new long[]{0L, 0b0010_0110_0100L, 0L}),
                new Command[]{Command.ROTATE_CW, Command.TAP_RIGHT});
        typeMap.put(new HalfGrid(new long[]{0L, 0b0010_0010L, 0b1000_1000_0000L}),
                new Command[]{Command.ROTATE_CW, Command.TAP_RIGHT, Command.TAP_RIGHT});
        typeMap.put(new HalfGrid(new long[]{0L, 0L, 0b0100_1100_1000L}),
                new Command[]{Command.DAS_RIGHT, Command.ROTATE_CCW});
        typeMap.put(new HalfGrid(new long[]{0L, 0L, 0b0010_0110_0100L}),
                new Command[]{Command.ROTATE_CW, Command.DAS_RIGHT});
    }

    private static void initIMap() {
        HashMap<HalfGrid, Command[]> typeMap = new HashMap<>();
        FINESSE_MAP.put(Tetromino.I_TYPE, typeMap);

        HashMap<HalfGrid, long[][]> freeMap = new HashMap<>();
        FREE_MAP.put(Tetromino.I_TYPE, freeMap);

        initIOrientationA(typeMap, freeMap);
        initIOrientationB(typeMap, freeMap);
    }

    private static void initIOrientationA(
            HashMap<HalfGrid, Command[]> typeMap,
            HashMap<HalfGrid, long[][]> freeMap) {
        typeMap.put(new HalfGrid(new long[]{0b1111L, 0L, 0L}),
                new Command[]{Command.DAS_LEFT});
        typeMap.put(new HalfGrid(new long[]{0b0111L, 0b1000L, 0L}),
                new Command[]{Command.TAP_LEFT, Command.TAP_LEFT,});
        typeMap.put(new HalfGrid(new long[]{0b0011L, 0b1100L, 0L}),
                new Command[]{Command.TAP_LEFT});
        typeMap.put(new HalfGrid(new long[]{0b0001L, 0b1110L, 0L}),
                new Command[]{});
        typeMap.put(new HalfGrid(new long[]{0L, 0b1110L, 0b1000L}),
                new Command[]{Command.TAP_RIGHT});
        typeMap.put(new HalfGrid(new long[]{0L, 0b0110L, 0b1100L}),
                new Command[]{Command.TAP_RIGHT, Command.TAP_RIGHT});
        typeMap.put(new HalfGrid(new long[]{0L, 0b0010L, 0b1110L}),
                new Command[]{Command.DAS_RIGHT});
    }

    private static void initIOrientationB(
            HashMap<HalfGrid, Command[]> typeMap,
            HashMap<HalfGrid, long[][]> freeMap) {
        HalfGrid b1 = new HalfGrid(new long[]{0b1000_1000_1000_1000L, 0L, 0L});
        typeMap.put(b1, new Command[]{Command.ROTATE_CCW, Command.DAS_LEFT});
        freeMap.put(b1, createFreeGrid(0, 4, 4));

        HalfGrid b2 = new HalfGrid(new long[]{0b0100_0100_0100_0100L, 0L, 0L});
        typeMap.put(b2,
                new Command[]{Command.DAS_LEFT, Command.ROTATE_CCW});
        freeMap.put(b2, createFreeGrid(1, 4, 3));

        HalfGrid b3 = new HalfGrid(new long[]{0b0010_0010_0010_0010L, 0L, 0L});
        typeMap.put(b3,
                new Command[]{Command.DAS_LEFT, Command.ROTATE_CW});
        freeMap.put(b3, createFreeGrid(2, 4, 3));

        HalfGrid b4 = new HalfGrid(new long[]{0b0001_0001_0001_0001L, 0L, 0L});
        typeMap.put(b4,
                new Command[]{Command.TAP_LEFT, Command.ROTATE_CCW});
        freeMap.put(b4, createFreeGrid(3, 4, 3));

        typeMap.put(new HalfGrid(new long[]{0L, 0b1000_1000_1000_1000L, 0L}),
                new Command[]{Command.ROTATE_CCW});
        typeMap.put(new HalfGrid(new long[]{0L, 0b0100_0100_0100_0100L, 0L}),
                new Command[]{Command.ROTATE_CW});
        typeMap.put(new HalfGrid(new long[]{0L, 0b0010_0010_0010_0010L, 0L}),
                new Command[]{Command.TAP_RIGHT, Command.ROTATE_CW});
        typeMap.put(new HalfGrid(new long[]{0L, 0L, 0b1000_1000_1000_1000L}),
                new Command[]{Command.DAS_RIGHT, Command.ROTATE_CCW});
        typeMap.put(new HalfGrid(new long[]{0L, 0L, 0b0100_0100_0100_0100L}),
                new Command[]{Command.DAS_RIGHT, Command.ROTATE_CW});

        HalfGrid b10 = new HalfGrid(new long[]{0L, 0L, 0b0010_0010_0010_0010L});
        typeMap.put(b10, new Command[]{Command.ROTATE_CW, Command.DAS_RIGHT});
        freeMap.put(b10, new long[][]{{0L,0L}, 
            GridModifier.shiftUp(new long[]{0b0010_0010_0010_0010L, 0L}, 17), 
            GridModifier.shiftUp(new long[]{0b1110_1110_1110_1110L, 0L}, 17)});
    }

    public static long[][] createFreeGrid(int i0, int i1, int h) {
        long[][] free = new long[Matrix.NUM_PARTIAL_GRIDS][2];
        for (int j = i0; j < i1; j++) {
            for (int i = 0; i < h; i++) {
                int shift = (Matrix.MAX_PROCESS_HEIGHT
                        - Matrix.MAX_UPPER_HEIGHT - 1 - i) % Matrix.HALF_BORDER
                        * Matrix.PARTIAL_GRID_WIDTH;
                long mask = (0b1000L >>> j) << shift;

                free[Matrix.LEFT_SURFACE_INDEX][1] |= mask;
            }
        }

        return free;
    }
    
}
