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
    /**
     * Updates the internal state of the bot for a single step.
     * <p>
     * This involves first updating the portion of the screen that the bot is capturing.
     * Then feeding relevant grid and tetromino data derived from this screen to the appropriate data structures.
     * If the bot has not been initialized yet, it will check if there is a valid tetromino queue on the screen, 
     * to determine if the game has started. If this is the case then it will update the internal queue and prepare
     * the grid capturer. The bot will be now initialized.
     * <p>
     * When the bot has been initialized update will get the top piece from the tetromino queue and calculate
     * an appropriate position for this piece to be placed. It will then apply this move in the game by pressing
     * the appropriate keys.
     * 
     * @param deltaTime time elapsed since last call to update
     * @param totalElapsedTime time elapsed since first call to update
     */
    public void update(double deltaTime, double totalElapsedTime) {
        updateCapture();

        if (locator.inGame()) {
            if (!initialized) {
            	//if the bot can find a tetromino queue, then it will initialize its fields.
            	//otherwise keep pressing continue, to start the game.
                if (generator.init()) {
                    //initialize the queue tracker with the first queue of tetrominoes
                	tracker.initQueue(new LinkedList<>(generator.getQueue()));
                	//initialize what it looks like when a grid is unoccupied by a tetromino part
                    gridCapturer.initUnsetRGBGrid();
                    initialized = true;
                } else {
                	executor.executeInstruction(CommandConverter.COMMAND_INSTRUCTION_MAP.get(Command.CONTINUE));
                	
                	//the following commented out code was for the Tetris friends implementation
                	//which required the mouse to start the game, Nullpomino does not require this.
                	
                	//locator.updateInStart();
                    //if (locator.inStart()) {
                    //    executor.press(locator.getStartPress());
                    //}
                }
            } else {
                Tetromino next = genNextPiece();
                if (next != null) {
                	//if move chooser has not been initialized set its queue to that shown on screen
                    if (!chooserInitialized) {
                        matrix.setQueue(generator.getQueue());
                    }

                    matrix.setActive(next);
                    //the tetromino queue as shown on screen
                    LinkedList<Tetromino> queue
                            = new LinkedList<>(generator.getQueue());
                    //the tetrominoes not shown on screen, but derived from the given data (as piece generation
                    //is only pseudo random, see tetris bag randomizer)
                    LinkedList<Tetromino> remainder
                            = new LinkedList<>(tracker.getRemainder());
                    //if there is only one piece in the remainder then we can be certain it will be shown next,
                    //so we can assume it is part of the normal queue
                    if (remainder.size() == 1) {
                        queue.add(remainder.remove(0));
                    }
                    //choose a move
                    Node chosen = !chooserInitialized
                            ? chooser.chooseFirstMove(next, queue, remainder)
                            : chooser.chooseNextMove(matrix, queue, remainder);
                    apply(chosen);
                    
                    
                    chooserInitialized = true;
                }
            }
        //the following lines of code were only relevant in the Tetris friends implementation of this bot,
        //they are not applicable to the Nullpomino implementation.
        } else if (locator.inPlay()) {
        	executor.resetPress(locator.getPlayPress());
            reset();
        } else if (locator.inRestart()) {
            executor.resetPress(locator.getRestartPress());
        	reset();
        }
    }

    /**
     * Updates relevant internal data (queue, grid structure and grid position) based on the 
     * portion of the screen that is captured by the bot.
     * 
     * @return true if the bot is in a game right now
     */
    public boolean updateCapture() {
        locator.update();
        if (locator.inGame()) {
            setGameSource(locator.getGameSource());
            gridCapturer.updateGrid(history.getMatrix(), chooser.isMirror());
            
            //set if the normalized grid is broken or not 
            capDisplay.setBrokenNorm(gridCapturer.isBrokenNorm());
            //set the nomalized grid to be displayed
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

    /**
     * Applies the move chosen by the tetris bot in the external game. It does so
     * by executing key presses in the correct order and timing.
     * 
     * @see InstructionExecutor
     * @see CommandConverter
     * @see Command
     * 
     * @param chosen a <tt>Node</tt> containing the move chosen by the bot
     */
    private void apply(Node chosen) {
        if (chosen != null) {
            makeMove(chosen.getMove());
        } else {
        	//restart game (only works on tetris friends)
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
