package net.philocraft.commands.subcommands.area.edit;

import java.awt.Color;
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
        if(args.length != 3) {
            return new InvalidArgumentsException().sendCause(player);
        }

        String stringColor = args[2];
        if(!args[2].startsWith("#")) {
            stringColor = "#" + stringColor;
        }
        
        Color color = null;
        
        try {
            color = Color.decode(stringColor);
        } catch (Exception e) {
            return new InvalidArgumentsException("Could not get color. It need to be in a HEX format.").sendCause(player);
        }
        
        Area area = AreaEditSubcommand.getArea();

        area.erase();
        area.setColor(color);
        area.draw();
        
        try {
            AreaUtil.saveArea(area);
        } catch (SQLException e) {
            AreaEssentials.getPlugin().getLogger().severe("Couldn't save area : " + e.getMessage());
        }
        player.sendMessage(Colors.SUCCESS.getChatColor() + "Successfully changed your area to your " + ChatColor.of(color) + "color.");
        return true;
    }
    
}

