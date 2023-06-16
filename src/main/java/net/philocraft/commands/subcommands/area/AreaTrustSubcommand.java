package net.philocraft.commands.subcommands.area;

import java.sql.SQLException;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import dev.littlebigowl.api.constants.Colors;
import dev.littlebigowl.api.errors.InvalidArgumentsException;
import net.philocraft.AreaEssentials;
import net.philocraft.errors.BadAreaException;
import net.philocraft.models.Area;
import net.philocraft.models.Subcommand;
import net.philocraft.utils.AreaUtil;

public class AreaTrustSubcommand extends Subcommand {

    @Override
    public String getName() {
        return "trust";
    }

    @Override
    public String getDescription() {
        return "Allows players to be trusted in an area";
    }

    @Override
    public String getSyntax() {
        return "/area trust <playerName> <permission>";
    }

    @Override
    public boolean perform(Player player, String[] args) {
        if(args.length != 4) {
            return new InvalidArgumentsException().sendCause(player);
        }
        
        Area area = AreaUtil.getArea(player.getLocation());

        if(area == null) {
            return new BadAreaException().sendCause(player);
        }

        if(!area.getUUID().equals(player.getUniqueId())) {
            return new BadAreaException("You are not the owner of this area.").sendCause(player);
        }

        UUID trusted;
        String permission = args[2];
        boolean value;

        Player trustedPlayer = Bukkit.getPlayer(args[1]);
        if(trustedPlayer == null || !AreaUtil.getPermissionKeys().contains(permission)) {
            return new InvalidArgumentsException().sendCause(player);
        }

        trusted = trustedPlayer.getUniqueId();

        if(args[3].equals("default")) {
            try {
                AreaUtil.distrustPlayer(area, permission, trusted);
            } catch (SQLException e) {
                AreaEssentials.getPlugin().getLogger().severe("Couldn't distrust player : " + e.getMessage());
            }
            player.sendMessage(
                Colors.SUCCESS.getChatColor() + "Successfully set " + 
                Colors.SUCCESS_DARK.getChatColor() + trustedPlayer.getName() + 
                Colors.SUCCESS.getChatColor() + " to default for " + 
                Colors.SUCCESS_DARK.getChatColor() + permission + 
                Colors.SUCCESS.getChatColor() + "."
            );

            return true;
        }
        
        try {
            value = Boolean.parseBoolean(args[3]);
        } catch (Exception e) {
            return new InvalidArgumentsException().sendCause(player);
        }

        if(player.getName().equals(args[1])) {
            return new InvalidArgumentsException("You can't change permissions for yourself.").sendCause(player);
        }

        String yesOrNo;
        if(value) {
           yesOrNo = " to "; 
        } else {
            yesOrNo = " not to ";
        }

        try {
            AreaUtil.trustPlayer(area, permission, trusted, value);
        } catch (SQLException e) {
            AreaEssentials.getPlugin().getLogger().severe("Couldn't trust player : " + e.getMessage());
        }

        player.sendMessage(
            Colors.SUCCESS.getChatColor() + "Successfully trusted " + 
            Colors.SUCCESS_DARK.getChatColor() + trustedPlayer.getName() + 
            Colors.SUCCESS.getChatColor() + yesOrNo + 
            Colors.SUCCESS_DARK.getChatColor() + permission + 
            Colors.SUCCESS.getChatColor() + "."
        );

        return true;
    }
}
