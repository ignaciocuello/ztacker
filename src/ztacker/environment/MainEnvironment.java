package ztacker.environment;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import ztacker.chooser.MoveChooser;
import ztacker.chooser.Node;
import ztacker.display.DisplayPanel;
import ztacker.display.HistoryDisplay;
import ztacker.framework.Dynamic;
import ztacker.matrix.Matrix;
import ztacker.move.Move;
import ztacker.queue.QueueCapturerGenerator;
import ztacker.queue.QueueTracker;
import ztacker.robot.in.GridCapturer;
import ztacker.robot.in.GridCapturerDisplay;
import ztacker.robot.in.InputWrapper;
import ztacker.robot.in.ObjectLocator;
import ztacker.robot.in.QueueCapturer;
import ztacker.robot.out.Command;
import ztacker.robot.out.CommandConverter;
import ztacker.robot.out.Instruction;
import ztacker.robot.out.InstructionExecutor;
import ztacker.robot.out.MoveConverter;
import ztacker.tetromino.Tetromino;

public final class MainEnvironment implements Dynamic {

    private Matrix matrix;

    private QueueTracker tracker;

    private final QueueCapturer queueCapturer;
    private final ObjectLocator locator;
    private final GridCapturer gridCapturer;

    private QueueCapturerGenerator generator;

    private final InstructionExecutor executor;

    private HistoryDisplay history;

    private MoveChooser chooser;

    private boolean initialized;
    private boolean chooserInitialized;
    
    private final DisplayPanel display;
    private final GridCapturerDisplay capDisplay;

    private final Robot robot;

    public MainEnvironment(InputWrapper inputWrapper, MoveChooser chooser, 
            DisplayPanel display)
            throws AWTException {
        this.chooser = chooser;
        
        this.robot = new Robot();
        robot.setAutoWaitForIdle(true);
        
        this.display = display;
        this.capDisplay = new GridCapturerDisplay();
        display.addDisplayable(capDisplay);
        
        this.queueCapturer = inputWrapper.getQueueCapturer();
        this.locator = inputWrapper.getObjectLocator();
        locator.setRobot(robot);

        this.gridCapturer = inputWrapper.getGridCapturer();
        this.executor = new InstructionExecutor(robot, this);

        reset();
    }

    private void reset() {
        matrix = new Matrix();
        tracker = new QueueTracker();
        generator = new QueueCapturerGenerator(queueCapturer);

        history = new HistoryDisplay();
        chooser = chooser.copy();

        initialized = false;
    }

    @Override
    public void update(double deltaTime, double totalElapsedTime) {
        updateCapture();

        if (locator.inGame()) {
            if (!initialized) {
                if (generator.init()) {
                    tracker.initQueue(new LinkedList<>(generator.getQueue()));
                    gridCapturer.initUnsetRGBGrid();
                    initialized = true;
                } else {
                    locator.updateInStart();
                    if (locator.inStart()) {
                        executor.press(locator.getStartPress());
                    }
                }
            } else {
                Tetromino next = genNextPiece();
                if (next != null) {
                    if (!chooserInitialized) {
                        matrix.setQueue(generator.getQueue());
                    }

                    matrix.setActive(next);
                    LinkedList<Tetromino> queue
                            = new LinkedList<>(generator.getQueue());
                    LinkedList<Tetromino> remainder
                            = new LinkedList<>(tracker.getRemainder());
                    if (remainder.size() == 1) {
                        queue.add(remainder.remove(0));
                    }
                    Node chosen = !chooserInitialized
                            ? chooser.chooseFirstMove(next, queue, remainder)
                            : chooser.chooseNextMove(matrix, queue, remainder);
                    apply(chosen);
                    chooserInitialized = true;
                }
            }
        } else if (locator.inPlay()) {
            executor.resetPress(locator.getPlayPress());
            reset();
        } else if (locator.inRestart()) {
            executor.resetPress(locator.getRestartPress());
            reset();
        }
    }

    public boolean updateCapture() {
        locator.update();
        if (locator.inGame()) {
            setGameSource(locator.getGameSource());
            gridCapturer.updateGrid(history.getMatrix(), chooser.isMirror());
            
            capDisplay.setBrokenNorm(gridCapturer.isBrokenNorm());
            capDisplay.setGrid(gridCapturer.getNorm());
            display.repaint();
        }
        
        return locator.inGame();
    }

    private void setGameSource(BufferedImage gmsrc) {
        generator.setGameSource(gmsrc);
        gridCapturer.setGameSource(gmsrc);
    }

    public Tetromino genNextPiece() {
        Tetromino active = generator.getNext();
        if (active != null) {
            tracker.updateQueue(generator.getQueue().getLast());
        }

        return active;
    }

    private void apply(Node chosen) {
        if (chosen != null) {
            makeMove(chosen.getMove());
        } else {
            executor.executeInstruction(
                    CommandConverter.COMMAND_INSTRUCTION_MAP.get(Command.PAUSE));
        }
    }

    private void makeMove(Move move) {
        MoveConverter converter = new MoveConverter(move);
        Instruction[] instructions = converter.convert(chooser.isMirror(),
                matrix);
        executor.executeInstructions(instructions);

        matrix.set(move.getGrid());

        if (move.isHold()) {
            matrix.setHeld(matrix.getActive());
        }

        if (move.isClear()) {
            matrix.clearRange(move.getClearFrom(), move.getClearTo());
        }

        matrix.setQueue(generator.getQueue());
        chooser.updateAll(move, matrix);
        history.makeMove(move);
    }

    public Matrix getMatrix() {
        return matrix;
    }

    public GridCapturer getGridCapturer() {
        return gridCapturer;
    }

    public ObjectLocator getLocator() {
        return locator;
    }

    public QueueCapturerGenerator getQueueCapturerGenerator() {
        return generator;
    }
    
    public InstructionExecutor getExecutor() {
        return executor;
    }
}
