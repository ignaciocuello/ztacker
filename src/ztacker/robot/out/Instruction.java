package ztacker.robot.out;

import java.awt.Robot;
import ztacker.environment.MainEnvironment;

public interface Instruction {
    
    public void execute(Robot robot, MainEnvironment ge);
    
}
