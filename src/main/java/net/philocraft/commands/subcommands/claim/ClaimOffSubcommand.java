package net.philocraft.commands.subcommands.claim;

import org.bukkit.entity.Player;

import dev.littlebigowl.api.constants.Colors;
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
        ClaimUtil.setClaimModeOff(player);
        player.sendMessage(Colors.SUCCESS.getChatColor() + "Claim mode deactivated.");

        return true;
    }
    
}
