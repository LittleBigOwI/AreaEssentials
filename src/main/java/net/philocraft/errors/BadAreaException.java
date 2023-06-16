package net.philocraft.errors;

import org.bukkit.command.CommandSender;

import dev.littlebigowl.api.constants.Colors;

public class BadAreaException {
    
    private String cause;

    public BadAreaException() {
        this.cause = Colors.FAILURE.getChatColor() + "Could not find area.";
    }

    public BadAreaException(String cause) {
        this.cause = Colors.FAILURE.getChatColor() + cause;
    }

    public boolean sendCause(CommandSender sender) {
        sender.sendMessage(this.cause);
        return true;
    }

}
