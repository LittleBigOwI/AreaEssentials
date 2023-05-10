package net.philocraft.errors;

import org.bukkit.command.CommandSender;

import dev.littlebigowl.api.constants.Colors;

public class NoAreaException {
    
    private String cause;

    public NoAreaException() {
        this.cause = Colors.FAILURE.getChatColor() + "Could not find area.";
    }

    public NoAreaException(String cause) {
        this.cause = Colors.FAILURE.getChatColor() + cause;
    }

    public boolean sendCause(CommandSender sender) {
        sender.sendMessage(this.cause);
        return true;
    }

}
