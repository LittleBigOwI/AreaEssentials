package net.philocraft.commands.subcommands.area.edit;

import org.bukkit.entity.Player;

import net.philocraft.models.Subcommand;

public class AreaEditEnterMessageSubcommand extends Subcommand {
    
    @Override
    public String getName() {
        return "enterMessage";
    }

    @Override
    public String getDescription() {
        return "Edits the enter message of an area";
    }

    @Override
    public String getSyntax() {
        return "/area edit enterMessage <set/remove> <enterMessage>";
    }

    @Override
    public boolean perform(Player player, String[] args) {
        player.sendMessage("enterMessage");
        return true;
    }
    
}