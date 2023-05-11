package net.philocraft.commands.subcommands;

import java.sql.SQLException;

import org.bukkit.entity.Player;

import dev.littlebigowl.api.constants.Colors;
import dev.littlebigowl.api.errors.InvalidArgumentsException;
import net.philocraft.AreaEssentials;
import net.philocraft.errors.NoAreaException;
import net.philocraft.models.Area;
import net.philocraft.models.Subcommand;
import net.philocraft.utils.AreaUtil;

public class CreateSubcommand extends Subcommand {

    @Override
    public String getName() {
        return "create";
    }

    @Override
    public String getDescription() {
        return "Creates an area if player has a potential one.";
    }

    @Override
    public String getSyntax() {
        return "/area create";
    }

    @Override
    public boolean perform(Player player, String[] args) {
        if(args.length != 1) {
            return new InvalidArgumentsException().sendCause(player);
        }

        Area potentialArea = AreaUtil.getPotentialArea(player);
        
        if(potentialArea == null) {
            return new NoAreaException("Could not find corners for area.").sendCause(player);
        }

        try {
            AreaUtil.createArea(potentialArea);
        } catch (SQLException e) {
            AreaEssentials.getPlugin().getLogger().severe("Couldn't save area : " + e.getMessage());
        }
        player.sendMessage(Colors.SUCCESS.getChatColor() + "Successfully created your area.");
        
        return true;
    }
    
}
