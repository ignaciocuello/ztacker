package ztacker.robot.out;

import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import ztacker.environment.MainEnvironment;

public final class CommandConverter {
    
    private static final int PAUSE_DELAY = 150;
    
    public static final HashMap<Command, Integer> COMMAND_KEY_MAP
            = new HashMap<>();
    public static final HashMap<Command, Instruction> COMMAND_INSTRUCTION_MAP
            = new HashMap<>();
    
    static {
        initKeyMap();
        initCommands();
    }

    private final Command[] commands;
    private final boolean hold;

    private final int softDropHeight;

    public CommandConverter(Command[] commands, boolean hold) {
        this(commands, hold, 0);
    }

    public CommandConverter(Command[] commands, boolean hold,
            int softDropHeight) {
        System.out.println(Arrays.toString(commands));
        this.commands = commands;
        this.hold = hold;
        this.softDropHeight = softDropHeight;
    }

    public Instruction[] convert() {
        ArrayList<Instruction> instructions = new ArrayList<>();
        if (hold) {
            instructions.add(COMMAND_INSTRUCTION_MAP.get(Command.HOLD));
        }

        for (Command command : commands) {
            if (command != Command.SOFT_DROP) {
                instructions.add(COMMAND_INSTRUCTION_MAP.get(command));
            } else {
                instructions.add(getSoftDropInstruction());
            }
        }

        instructions.add(COMMAND_INSTRUCTION_MAP.get(Command.DROP));

        return instructions.toArray(new Instruction[0]);
    }

    private Instruction getSoftDropInstruction() {
        return (Robot robot, MainEnvironment be) -> {
            int key = COMMAND_KEY_MAP.get(Command.SOFT_DROP);
            while (be.getGridCapturer().getHeight() != softDropHeight) {
                robot.keyPress(key);
                if (!be.updateCapture()) {
                    robot.keyRelease(key);
                    break;
                }
            }
            robot.keyRelease(key);
        };
    }

    public Command[] getCommands() {
        return commands;
    }

    private static void initKeyMap() {
        COMMAND_KEY_MAP.put(Command.DAS_LEFT, KeyEvent.VK_LEFT);
        COMMAND_KEY_MAP.put(Command.DAS_RIGHT, KeyEvent.VK_RIGHT);
        COMMAND_KEY_MAP.put(Command.DROP, KeyEvent.VK_SPACE);
        COMMAND_KEY_MAP.put(Command.HOLD, KeyEvent.VK_C);
        COMMAND_KEY_MAP.put(Command.PAUSE, KeyEvent.VK_ESCAPE);
        COMMAND_KEY_MAP.put(Command.ROTATE_CCW, KeyEvent.VK_Z);
        COMMAND_KEY_MAP.put(Command.ROTATE_CW, KeyEvent.VK_X);
        COMMAND_KEY_MAP.put(Command.SOFT_DROP, KeyEvent.VK_DOWN);
        COMMAND_KEY_MAP.put(Command.TAP_LEFT, KeyEvent.VK_LEFT);
        COMMAND_KEY_MAP.put(Command.TAP_RIGHT, KeyEvent.VK_RIGHT);
    }

    private static void initCommands() {
        initDASCommands();
        initRotationCommands();
        initTapCommands();
        initPauseCommand();
    }

    private static void initDASCommands() {
        COMMAND_INSTRUCTION_MAP.put(Command.DAS_LEFT,
                (Instruction) (Robot robot, MainEnvironment be) -> {
                    int key = COMMAND_KEY_MAP.get(Command.DAS_LEFT);
                    while (!be.getGridCapturer().isDasLeft()) {
                        robot.keyPress(key);
                        if (!be.updateCapture()) {
                            robot.keyRelease(key);
                            break;
                        }
                    }
                    robot.keyRelease(key);
                });

        COMMAND_INSTRUCTION_MAP.put(Command.DAS_RIGHT,
                (Instruction) (Robot robot, MainEnvironment be) -> {
                    int key = COMMAND_KEY_MAP.get(Command.DAS_RIGHT);
                    while (!be.getGridCapturer().isDasRight()) {
                        robot.keyPress(key);
                        if (!be.updateCapture()) {
                            robot.keyRelease(key);
                            break;
                        }
                    }
                    robot.keyRelease(key);
                });
    }

    private static void initRotationCommands() {
        COMMAND_INSTRUCTION_MAP.put(Command.ROTATE_CW,
                generateTapInstruction(Command.ROTATE_CW));
        COMMAND_INSTRUCTION_MAP.put(Command.ROTATE_CCW,
                generateTapInstruction(Command.ROTATE_CCW));
    }

    private static void initTapCommands() {
        COMMAND_INSTRUCTION_MAP.put(Command.TAP_LEFT,
                generateTapInstruction(Command.TAP_LEFT));
        COMMAND_INSTRUCTION_MAP.put(Command.TAP_RIGHT,
                generateTapInstruction(Command.TAP_RIGHT));

        COMMAND_INSTRUCTION_MAP.put(Command.DROP,
                (Instruction) (Robot robot, MainEnvironment be) -> {
                    int key = COMMAND_KEY_MAP.get(Command.DROP);
                    boolean[][] bg0 = be.getGridCapturer().getGrid();

                    boolean nvchange = false;
                    while (!nvchange) {
                        robot.keyPress(key);
                        if (!be.updateCapture()) {
                            robot.keyRelease(key);
                            break;
                        }

                        boolean[][] bg1 = be.getGridCapturer().getGrid();
                        nvchange
                        = be.getGridCapturer().isNonVerticalChange(bg0, bg1)
                        || !be.getQueueCapturerGenerator().getQueue().equals(
                                be.getQueueCapturerGenerator().getCapturer().
                                readQueue(be.getLocator().getGameSource()));  
                    }

                    robot.keyRelease(key);
                });

        COMMAND_INSTRUCTION_MAP.put(Command.HOLD,
                (Instruction) (Robot robot, MainEnvironment be) -> {
                    int key = COMMAND_KEY_MAP.get(Command.HOLD);
                    boolean[][] bg0 = be.getGridCapturer().getGrid();

                    boolean nvchange = false;
                    while (!nvchange) {
                        robot.keyPress(key);
                        if (!be.updateCapture()) {
                            robot.keyRelease(key);
                            break;
                        }

                        boolean[][] bg1 = be.getGridCapturer().getGrid();
                        nvchange
                        = be.getGridCapturer().isNonVerticalChange(bg0, bg1);
                    }

                    robot.keyRelease(key);
                    if (be.getMatrix().getHeld() == null) {
                        be.updateCapture();
                        be.genNextPiece();
                    }
                });
    }

    private static Instruction generateTapInstruction(Command command) {
        return (Robot robot, MainEnvironment be) -> {
            int key = COMMAND_KEY_MAP.get(command);
            boolean[][] bg0 = be.getGridCapturer().getGrid();

            boolean nvchange = false;
            while (!nvchange) {
                robot.keyPress(key);
                if (!be.updateCapture()) {
                    robot.keyRelease(key);
                    break;
                }

                boolean[][] bg1 = be.getGridCapturer().getGrid();
                nvchange
                        = be.getGridCapturer().isNonVerticalChange(bg0, bg1);
            }

            robot.keyRelease(key);
        };
    }

    private static void initPauseCommand() {
        COMMAND_INSTRUCTION_MAP.put(Command.PAUSE, (r, ge) -> {
            int key = COMMAND_KEY_MAP.get(Command.PAUSE);
            r.keyPress(key);
            r.delay(PAUSE_DELAY);
            r.keyRelease(key);
        });
    }
}
