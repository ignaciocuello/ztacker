package ztacker.robot.in.modes;

import java.io.File;
import ztacker.robot.in.InputWrapper;

public final class SurvivalInputWrapper extends InputWrapper {
    
    public SurvivalInputWrapper() {
        super(new File("robin/survivalgrid.stk"), new File("robin/survivalloc.stk"), 
                new File("robin/survivalque.stk"));
    }
    
    @Override
    public String toString() {
        return "Survival";
    }
}
