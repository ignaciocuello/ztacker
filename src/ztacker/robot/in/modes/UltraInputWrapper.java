package ztacker.robot.in.modes;

import java.io.File;
import ztacker.robot.in.InputWrapper;

public final class UltraInputWrapper extends InputWrapper {
    
    public UltraInputWrapper() {
        super(new File("robin/ultragrid.stk"), new File("robin/ultraloc.stk"), 
                new File("robin/ultraque.stk"));
    }
    
    @Override
    public String toString() {
        return "Ultra";
    }
}
