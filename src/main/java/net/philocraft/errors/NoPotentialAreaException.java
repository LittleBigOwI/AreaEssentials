package net.philocraft.errors;

import org.bukkit.command.CommandSender;

import dev.littlebigowl.api.constants.Colors;

public class NoPotentialAreaException {
    
    private String cause;

    public NoPotentialAreaException() {
        this.cause = Colors.FAILURE.getChatColor() + "Could not create Area.";
    }

    public NoPotentialAreaException(String cause) {
        this.cause = Colors.FAILURE.getChatColor() + cause;
    }

    public boolean sendCause(CommandSender sender) {
        sender.sendMessage(this.cause);
        return true;
    }

}
