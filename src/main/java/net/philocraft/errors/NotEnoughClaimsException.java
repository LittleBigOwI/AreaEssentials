package net.philocraft.errors;

import org.bukkit.command.CommandSender;

import dev.littlebigowl.api.constants.Colors;

public class NotEnoughClaimsException {

    private String cause;

    public NotEnoughClaimsException() {
        this.cause = Colors.FAILURE.getChatColor() + "You don't have enough claim blocks.";
    }

    public NotEnoughClaimsException(String cause) {
        this.cause = Colors.FAILURE.getChatColor() + cause;
    }

    public boolean sendCause(CommandSender sender) {
        sender.sendMessage(this.cause);
        return true;
    }

}
