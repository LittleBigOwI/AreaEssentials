package net.philocraft.commands.subcommands;

import org.bukkit.entity.Player;

import dev.littlebigowl.api.constants.Colors;
import dev.littlebigowl.api.errors.InvalidArgumentsException;
import net.philocraft.errors.NoAreaException;
import net.philocraft.models.Area;
import net.philocraft.models.Subcommand;
import net.philocraft.utils.AreaUtil;

public class ExpandSubcommand extends Subcommand {

    @Override
    public String getName() {
        return "expand";
    }

    @Override
    public String getDescription() {
        return "Allows areas to be expanded";
    }

    @Override
    public String getSyntax() {
        return "/area expand <distance>";
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
            return new InvalidArgumentsException("You cannot expand an area by a negative amount.").sendCause(player);
        }

        double cost = area.expand(player, amount);        
        player.sendMessage(Colors.SUCCESS.getChatColor() + "Expanded " + area.getName() + " by " + amount + " blocks for " + cost + " claim blocks.");
        return true;
    }
    
}
