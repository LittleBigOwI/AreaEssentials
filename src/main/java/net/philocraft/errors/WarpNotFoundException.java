package net.philocraft.errors;

import org.bukkit.command.CommandSender;

import dev.littlebigowl.api.constants.Colors;

public class WarpNotFoundException {

    private String cause;

    public WarpNotFoundException() {
        this.cause = Colors.FAILURE.getChatColor() + "Could not find warp.";
    }

    public WarpNotFoundException(String cause) {
        this.cause = Colors.FAILURE.getChatColor() + cause;
    }

    public boolean sendCause(CommandSender sender) {
        sender.sendMessage(this.cause);
        return true;
    }

}