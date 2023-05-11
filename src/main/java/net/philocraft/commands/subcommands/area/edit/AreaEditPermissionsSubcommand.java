package net.philocraft.commands.subcommands.area.edit;

import java.sql.SQLException;

import org.bukkit.entity.Player;

import dev.littlebigowl.api.constants.Colors;
import dev.littlebigowl.api.errors.InvalidArgumentsException;
import net.philocraft.AreaEssentials;
import net.philocraft.commands.subcommands.area.AreaEditSubcommand;
import net.philocraft.models.Area;
import net.philocraft.models.Subcommand;
import net.philocraft.utils.AreaUtil;

public class AreaEditPermissionsSubcommand extends Subcommand {

    @Override
    public String getName() {
        return "permissions";
    }

    @Override
    public String getDescription() {
        return "Edits the permissions of an area";
    }

    @Override
    public String getSyntax() {
        return "/area edit permissions <permission> <true/false>";
    }

    @Override
    public boolean perform(Player player, String[] args) {
        if(args.length != 4) {
            return new InvalidArgumentsException().sendCause(player);
        }

        String permission = args[2];
        String stringValue = args[3];
        Area area = AreaEditSubcommand.getArea();

        if(!AreaUtil.getPermissionKeys().contains(permission)) {
            return new InvalidArgumentsException().sendCause(player);
        }

        boolean value;
        try {
            value = Boolean.parseBoolean(stringValue);
        } catch (Exception e) {
            return new InvalidArgumentsException().sendCause(player);
        }

        area.setPermission(permission, value);
        
        try {
            AreaUtil.saveArea(area);
        } catch (SQLException e) {
            AreaEssentials.getPlugin().getLogger().severe("Couldn't save area : " + e.getMessage());
        }
        player.sendMessage(Colors.SUCCESS.getChatColor() + "Successfully set permission.");
        return true;
    }

}
