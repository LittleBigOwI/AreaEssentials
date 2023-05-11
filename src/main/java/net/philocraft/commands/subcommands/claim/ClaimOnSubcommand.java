package net.philocraft.commands.subcommands.claim;

import org.bukkit.entity.Player;

import dev.littlebigowl.api.constants.Colors;
import net.philocraft.models.Subcommand;
import net.philocraft.utils.ClaimUtil;

public class ClaimOnSubcommand extends Subcommand {

    @Override
    public String getName() {
        return "on";
    }

    @Override
    public String getDescription() {
        return "Turns on player claiming mode";
    }

    @Override
    public String getSyntax() {
        return "/claim on";
    }

    @Override
    public boolean perform(Player player, String[] args) {
        ClaimUtil.setClaimModeOn(player);
        player.sendMessage(Colors.SUCCESS.getChatColor() + "Claim mode activated.");

        return true;
    }
    
}
