package ztacker.robot.out;

import java.util.HashMap;

public enum Command {
    DAS_LEFT, DAS_RIGHT,
    ROTATE_CW, ROTATE_CCW, 
    TAP_LEFT, TAP_RIGHT, 
    DROP, HOLD, SOFT_DROP,
    PAUSE, CONTINUE;
    
    private static final HashMap<Command, Command> MIRROR 
            = new HashMap<>();
    
    static {
        MIRROR.put(Command.DAS_LEFT, Command.DAS_RIGHT);
        MIRROR.put(Command.DAS_RIGHT, Command.DAS_RIGHT);
        MIRROR.put(Command.ROTATE_CW, Command.ROTATE_CCW);
        MIRROR.put(Command.ROTATE_CCW, Command.ROTATE_CW);
        MIRROR.put(Command.TAP_LEFT, Command.TAP_RIGHT);
        MIRROR.put(Command.TAP_RIGHT, Command.TAP_LEFT);
    }
    
    public Command mirror() {
        return MIRROR.get(this);
    }
}
