package net.philocraft.commands.subcommands.area;

import java.sql.SQLException;

import org.bukkit.entity.Player;

import dev.littlebigowl.api.constants.Colors;
import dev.littlebigowl.api.errors.InvalidArgumentsException;
import net.philocraft.AreaEssentials;
import net.philocraft.errors.BadAreaException;
import net.philocraft.models.Area;
import net.philocraft.models.Subcommand;
import net.philocraft.utils.AreaUtil;
import net.philocraft.utils.ClaimUtil;

public class AreaRemoveSubommand extends Subcommand {

    @Override
    public String getName() {
        return "remove";
    }

    @Override
    public String getDescription() {
        return "Removes an area";
    }

    @Override
    public String getSyntax() {
        return "/area remove";
    }

    @Override
    public boolean perform(Player player, String[] args) {
        if(args.length != 1) {
            return new InvalidArgumentsException().sendCause(player);
        }

        Area area = AreaUtil.getArea(player.getLocation());

        if(area == null) {
            return new BadAreaException().sendCause(player);
        }

        if(!area.getUUID().equals(player.getUniqueId())) {
            return new BadAreaException("You are not the owner of this area.").sendCause(player);
        }

        int cost = (int)area.getSurface();
        try {
            AreaUtil.removeArea(area);
        } catch (SQLException e) {
            AreaEssentials.getPlugin().getLogger().severe("Couldn't remove area : " + e.getMessage());
        }

        ClaimUtil.addClaimBlocks(player, cost);
        player.sendMessage(Colors.SUCCESS.getChatColor() + "Successfully removed your area.");
        
        area.erase();
        return true;
    }
    
}
