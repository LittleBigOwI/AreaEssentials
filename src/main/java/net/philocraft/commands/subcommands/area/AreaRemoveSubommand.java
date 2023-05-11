package net.philocraft.commands.subcommands.area;

import java.sql.SQLException;

import org.bukkit.entity.Player;

import dev.littlebigowl.api.constants.Colors;
import dev.littlebigowl.api.errors.InvalidArgumentsException;
import net.philocraft.AreaEssentials;
import net.philocraft.errors.NoAreaException;
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

        int i = 0;
        while(i < AreaUtil.getAreas().size() && !AreaUtil.getAreas().get(i).contains(player)) {
            i++;
        }

        if(i == AreaUtil.getAreas().size()) {
            return new NoAreaException().sendCause(player);
        }

        if(!AreaUtil.getAreas().get(i).getUUID().equals(player.getUniqueId())) {
            return new NoAreaException("You are not the owner of this area.").sendCause(player);
        }

        Area area = AreaUtil.getAreas().get(i);
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
