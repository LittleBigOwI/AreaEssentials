package net.philocraft.commands.subcommands.area.edit;

import java.sql.SQLException;

import org.bukkit.entity.Player;

import dev.littlebigowl.api.constants.Colors;
import dev.littlebigowl.api.errors.InvalidArgumentsException;
import net.md_5.bungee.api.ChatColor;
import net.philocraft.AreaEssentials;
import net.philocraft.commands.subcommands.area.AreaEditSubcommand;
import net.philocraft.models.Area;
import net.philocraft.models.Subcommand;
import net.philocraft.utils.AreaUtil;

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
        if(args.length < 3) {
            return new InvalidArgumentsException().sendCause(player);
        }

        String action = args[2];
        Area area = AreaEditSubcommand.getArea();

        String splash = "";
        for(int i = 3; i < args.length; i++) {
            splash = splash + " " + args[i];
        }
        splash = ChatColor.translateAlternateColorCodes('&', splash);

        if(splash != null && splash.contains("'") || splash.contains("\\") || splash.contains("\"")) {
            return new InvalidArgumentsException("Area names can't contain \\, ' or \" characters.").sendCause(player);
        }

        if(action.equals("set")) {
            area.setEnterMessage(splash);

        } else if(action.equals("remove") && args.length == 3) {
            area.setEnterMessage(null);

        } else {
            return new InvalidArgumentsException().sendCause(player);
        }
        
        try {
            AreaUtil.saveArea(area);
        } catch (SQLException e) {
            AreaEssentials.getPlugin().getLogger().severe("Couldn't save area : " + e.getMessage());
        }
        player.sendMessage(Colors.SUCCESS.getChatColor() + "Successfully set enter message.");
        return true;
    }
    
}