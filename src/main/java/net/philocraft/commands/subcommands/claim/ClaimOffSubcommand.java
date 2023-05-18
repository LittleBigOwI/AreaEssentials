package net.philocraft.commands.subcommands.claim;

import org.bukkit.entity.Player;

import dev.littlebigowl.api.constants.Colors;
import dev.littlebigowl.api.errors.InvalidArgumentsException;
import net.philocraft.models.Subcommand;
import net.philocraft.utils.ClaimUtil;

public class ClaimOffSubcommand extends Subcommand {

    @Override
    public String getName() {
        return "off";
    }

    @Override
    public String getDescription() {
        return "Turns off player claiming mode";
    }

    @Override
    public String getSyntax() {
        return "/claim off";
    }

    @Override
    public boolean perform(Player player, String[] args) {
        if(args.length == 1 && !args[0].equals("on") && !args[0].equals("off")) {
            return new InvalidArgumentsException("You can only turn claim mode on or off.").sendCause(player);
        }

        if(args.length > 1) {
            return new InvalidArgumentsException().sendCause(player);
        }

        ClaimUtil.setClaimModeOff(player);
        player.sendMessage(Colors.SUCCESS.getChatColor() + "Claim mode deactivated.");

        return true;
    }
    
}
