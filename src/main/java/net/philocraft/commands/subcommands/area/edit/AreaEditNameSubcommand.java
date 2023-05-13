package net.philocraft.commands.subcommands.area.edit;

import java.sql.SQLException;

import org.bukkit.entity.Player;

import dev.littlebigowl.api.constants.Colors;
import dev.littlebigowl.api.errors.InvalidArgumentsException;
import net.philocraft.AreaEssentials;
import net.philocraft.commands.subcommands.area.AreaEditSubcommand;
import net.philocraft.errors.AreaExistsException;
import net.philocraft.models.Area;
import net.philocraft.models.Subcommand;
import net.philocraft.utils.AreaUtil;

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
        if(args.length != 3) {
            return new InvalidArgumentsException().sendCause(player);
        }

        String name = args[2];
        Area area = AreaEditSubcommand.getArea();

        if(name != null && name.contains("'") || name.contains("\\") || name.contains("\"")) {
            return new InvalidArgumentsException("Area names can't contain \\, ' or \" characters.").sendCause(player);
        }

        for(Area playerArea : AreaUtil.getAreas(player)) {
            if(playerArea.getName().equals(name)) {
                return new AreaExistsException("You already have an area with that name.").sendCause(player);
            }
        }

        try {
            AreaUtil.saveArea(area, name);
        } catch (SQLException e) {
            AreaEssentials.getPlugin().getLogger().severe("Couldn't save area : " + e.getMessage());
        }

        area.erase();
        area.setName(name);
        area.draw();
    
        player.sendMessage(Colors.SUCCESS.getChatColor() + "Successfully renamed your area.");
        return true;
    }
    
}
