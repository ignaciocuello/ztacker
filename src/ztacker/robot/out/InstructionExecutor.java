package ztacker.robot.out;

import java.awt.Point;
import java.awt.Robot;
import java.awt.event.InputEvent;
import ztacker.environment.MainEnvironment;

public final class InstructionExecutor {
            
    private final Robot robot;
    private final MainEnvironment me;
    
    public InstructionExecutor(Robot robot, MainEnvironment me) {
        this.robot = robot;
        this.me = me;
    }

    public synchronized void executeInstruction(Instruction instruction) {
        instruction.execute(robot, me);
    }

    public void executeInstructions(Instruction[] instructions) {
        for (Instruction instruction : instructions) {
            executeInstruction(instruction);
        }
    }
    
    public void releaseAll() {
        for (int key : CommandConverter.COMMAND_KEY_MAP.values()) {
            robot.keyRelease(key);
        }
    }
    
    public void resetPress(Point point) {
        releaseAll();
        
        press(point);
        
        while (!me.updateCapture()) {
        }
    }
    
    public void press(Point point) {
        robot.mouseMove(point.x, point.y);
        robot.mousePress(InputEvent.BUTTON1_MASK);
        robot.mouseRelease(InputEvent.BUTTON1_MASK);
    }
}                                          