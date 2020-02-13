package ztacker.robot.in.modes;

import java.io.File;
import ztacker.robot.in.InputWrapper;

public final class MarathonInputWrapper extends InputWrapper {
    
    public MarathonInputWrapper() {
        super(new File("robin/marathongrid.stk"), new File("robin/marathonloc.stk"), 
                new File("robin/marathonque.stk"));
    }
    
    @Override
    public String toString() {
        return "Marathon";
    }
}
