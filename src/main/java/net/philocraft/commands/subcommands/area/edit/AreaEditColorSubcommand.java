package net.philocraft.commands.subcommands.area.edit;

import org.bukkit.entity.Player;

import net.philocraft.models.Subcommand;

public class AreaEditColorSubcommand extends Subcommand {
    
    @Override
    public String getName() {
        return "color";
    }

    @Override
    public String getDescription() {
        return "Edits the color of an area";
    }

    @Override
    public String getSyntax() {
        return "/area edit color <color>";
    }

    @Override
    public boolean perform(Player player, String[] args) {
        player.sendMessage("color");
        return true;
    }
    
}

