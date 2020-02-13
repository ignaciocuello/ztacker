package ztacker.robot.in.modes;

import java.io.File;
import ztacker.robot.in.InputWrapper;

public final class SprintInputWrapper extends InputWrapper {
    
    public SprintInputWrapper() {
        super(new File("robin/sprintgrid.stk"), new File("robin/sprintloc.stk"), 
                new File("robin/sprintque.stk"));
    }
    
    @Override
    public String toString() {
        return "Sprint";
    }
}
