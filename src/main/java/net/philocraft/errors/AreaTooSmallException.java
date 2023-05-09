package net.philocraft.errors;

import org.bukkit.command.CommandSender;

import dev.littlebigowl.api.constants.Colors;

public class AreaTooSmallException {
    
    private String cause;

    public AreaTooSmallException() {
        this.cause = Colors.FAILURE.getChatColor() + "Area is too small! It needs to be at least 16x16.";
    }

    public AreaTooSmallException(String cause) {
        this.cause = Colors.FAILURE.getChatColor() + cause;
    }

    public boolean sendCause(CommandSender sender) {
        sender.sendMessage(this.cause);
        return true;
    }

}

