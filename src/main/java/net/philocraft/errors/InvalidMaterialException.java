package net.philocraft.errors;

import org.bukkit.command.CommandSender;

import dev.littlebigowl.api.constants.Colors;

public class InvalidMaterialException {
    
    private String cause;

    public InvalidMaterialException() {
        this.cause = Colors.FAILURE.getChatColor() + "You don't have the correct material";
    }

    public InvalidMaterialException(String cause) {
        this.cause = Colors.FAILURE.getChatColor() + cause;
    }

    public boolean sendCause(CommandSender sender) {
        sender.sendMessage(this.cause);
        return true;
    }

}
