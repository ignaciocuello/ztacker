package ztacker.move;

import ztacker.robot.out.Command;

public final class SoftDropCommands {

    private final long[] norm;
    private final Command command;
    
    public SoftDropCommands(long[] norm, Command command) {
        this.norm = norm;
        this.command = command;
    }
    
    public long[] getNorm() {
        return norm;
    }
    
    public Command getCommand() {
        return command;
    }
}
