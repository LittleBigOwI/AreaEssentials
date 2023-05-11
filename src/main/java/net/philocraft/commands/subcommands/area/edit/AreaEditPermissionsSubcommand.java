package net.philocraft.commands.subcommands.area.edit;

import org.bukkit.entity.Player;

import net.philocraft.models.Subcommand;

public class AreaEditPermissionsSubcommand extends Subcommand {

    @Override
    public String getName() {
        return "permissions";
    }

    @Override
    public String getDescription() {
        return "Edits the permissions of an area";
    }

    @Override
    public String getSyntax() {
        return "/area edit permissions <permission> <true/false>";
    }

    @Override
    public boolean perform(Player player, String[] args) {
        player.sendMessage("permissions");
        return true;
    }

}
