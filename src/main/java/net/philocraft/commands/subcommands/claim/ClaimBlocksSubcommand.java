package net.philocraft.commands.subcommands.claim;

import org.bukkit.entity.Player;

import dev.littlebigowl.api.constants.Colors;
import dev.littlebigowl.api.errors.InvalidArgumentsException;
import net.philocraft.models.Subcommand;
import net.philocraft.utils.ClaimUtil;

public class ClaimBlocksSubcommand extends Subcommand {

    @Override
    public String getName() {
        return "blocks";
    }

    @Override
    public String getDescription() {
        return "Displays the amount of claim blocks a player has";
    }

    @Override
    public String getSyntax() {
        return "/claim blocks";
    }

    @Override
    public boolean perform(Player player, String[] args) {
        if(args.length > 1) {
            return new InvalidArgumentsException().sendCause(player);
        }

        player.sendMessage(
            Colors.INFO.getChatColor() + "You have " + 
            Colors.MAJOR.getChatColor() + ClaimUtil.getClaimBlocks(player) + 
            Colors.INFO.getChatColor() + " claim blocks."
        );

        return true;
    }
    
}
