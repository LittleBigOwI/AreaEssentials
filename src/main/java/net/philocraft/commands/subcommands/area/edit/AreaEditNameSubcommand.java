package net.philocraft.commands.subcommands.area.edit;

import org.bukkit.entity.Player;

import net.philocraft.models.Subcommand;

public class AreaEditNameSubcommand extends Subcommand {

    @Override
    public String getName() {
        return "name";
    }

    @Override
    public String getDescription() {
        return "Edits the name of an area";
    }

    @Override
    public String getSyntax() {
        return "/area edit name <areaName>";
    }

    @Override
    public boolean perform(Player player, String[] args) {
        player.sendMessage("name");
        return true;
    }
    
}
