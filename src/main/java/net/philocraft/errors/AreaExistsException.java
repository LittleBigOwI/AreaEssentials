package net.philocraft.errors;

import org.bukkit.command.CommandSender;

import dev.littlebigowl.api.constants.Colors;

public class AreaExistsException {
    
    private String cause;

    public AreaExistsException() {
        this.cause = Colors.FAILURE.getChatColor() + "There is already an area at this location.";
    }

    public AreaExistsException(String cause) {
        this.cause = Colors.FAILURE.getChatColor() + cause;
    }

    public boolean sendCause(CommandSender sender) {
        sender.sendMessage(this.cause);
        return true;
    }

}
