package net.philocraft.commands.subcommands.area;

import java.sql.SQLException;

import org.bukkit.entity.Player;

import dev.littlebigowl.api.constants.Colors;
import dev.littlebigowl.api.errors.InvalidArgumentsException;
import net.philocraft.AreaEssentials;
import net.philocraft.errors.AreaExistsException;
import net.philocraft.errors.NoAreaException;
import net.philocraft.errors.NotEnoughClaimsException;
import net.philocraft.models.Area;
import net.philocraft.models.Subcommand;
import net.philocraft.utils.AreaUtil;
import net.philocraft.utils.ClaimUtil;

public class AreaCreateSubcommand extends Subcommand {

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
        return "/area create <optional:areaName>";
    }

    @Override
    public boolean perform(Player player, String[] args) {
        if(args.length != 1 && args.length != 2) {
            return new InvalidArgumentsException().sendCause(player);
        }

        Area potentialArea = AreaUtil.getPotentialArea(player);
        
        if(potentialArea == null) {
            return new NoAreaException("Could not find corners for area.").sendCause(player);
        }

        if(potentialArea.getSurface() > ClaimUtil.getClaimBlocks(player)) {
            return new NotEnoughClaimsException().sendCause(player);
        }

        if(args.length == 2 && args[1] != null && AreaUtil.getArea(player, args[1]) != null) {
            return new AreaExistsException("You already have an area with that name.").sendCause(player);
        }

        for(Area playerArea : AreaUtil.getAreas()) {
            if(potentialArea.overlaps(playerArea)) {
                return new AreaExistsException("This area overlaps with another.").sendCause(player);
            }
        }

        String name = null;

        if(args.length == 2 && args[1] != null) {
            name = args[1];
        }

        if(name != null && (name.contains("'") || name.contains("\\") || name.contains("\""))) {
            return new InvalidArgumentsException("Area names can't contain \\, ' or \" characters.").sendCause(player);

        } else if(name != null) {
            potentialArea.setName(name);

        }

        try {
            AreaUtil.createArea(potentialArea);
            ClaimUtil.addClaimBlocks(player, (int)potentialArea.getSurface()*-1);
        } catch (SQLException e) {
            AreaEssentials.getPlugin().getLogger().severe("Couldn't save area : " + e.getMessage());
        }
        
        player.sendMessage(
            Colors.SUCCESS.getChatColor() + "Successfully created new area for " + 
            Colors.SUCCESS_DARK.getChatColor() + potentialArea.getSurface() + 
            Colors.SUCCESS.getChatColor() + " claim blocks."
        );
        ClaimUtil.setClaimModeOff(player);

        potentialArea.draw();
        return true;
    }
    
}
