package net.philocraft.commands.subcommands.area;

import org.bukkit.entity.Player;

import dev.littlebigowl.api.constants.Colors;
import dev.littlebigowl.api.errors.InvalidArgumentsException;
import net.philocraft.errors.AreaTooSmallException;
import net.philocraft.errors.NoAreaException;
import net.philocraft.models.Area;
import net.philocraft.models.Subcommand;
import net.philocraft.utils.AreaUtil;
import net.philocraft.utils.ClaimUtil;

public class AreaShrinkSubcommand extends Subcommand {
    
    @Override
    public String getName() {
        return "shrink";
    }

    @Override
    public String getDescription() {
        return "Allows areas to be shrinked";
    }

    @Override
    public String getSyntax() {
        return "/area shrink <distance>";
    }

    @Override
    public boolean perform(Player player, String[] args) {
        if(args.length != 2) {
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
        int amount;

        try {
            amount = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            return new InvalidArgumentsException().sendCause(player);
        }

        if(amount < 0) {
            return new InvalidArgumentsException("You cannot shrink an area by a negative amount.").sendCause(player);
        }

        int cost = (int)area.getShrinkCost(player, amount);

        if(cost == -1) {
            return new AreaTooSmallException().sendCause(player);
        }

        ClaimUtil.addClaimBlocks(player, cost);      
                
        area.shrink(player, amount);
        area.draw();
        
        player.sendMessage(
            Colors.SUCCESS.getChatColor() + "Shrinked " + 
            Colors.SUCCESS_DARK.getChatColor() + area.getName() + 
            Colors.SUCCESS.getChatColor() + " by " + 
            Colors.SUCCESS_DARK.getChatColor() + amount + 
            Colors.SUCCESS.getChatColor() + " blocks for " + 
            Colors.SUCCESS_DARK.getChatColor() + cost + 
            Colors.SUCCESS.getChatColor() + " claim blocks."
        );
        return true;
    }
    
}
