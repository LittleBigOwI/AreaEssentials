package net.philocraft.commands.subcommands.area.edit;

import org.bukkit.entity.Player;

import net.philocraft.models.Subcommand;

public class AreaEditLeaveMessageSubcommand extends Subcommand {
    
    @Override
    public String getName() {
        return "leaveMessage";
    }

    @Override
    public String getDescription() {
        return "Edits the leave message of an area";
    }

    @Override
    public String getSyntax() {
        return "/area edit leaveMessage <set/remove> <leaveMessage>";
    }

    @Override
    public boolean perform(Player player, String[] args) {
        player.sendMessage("leaveMessage");
        return true;
    }
    
}