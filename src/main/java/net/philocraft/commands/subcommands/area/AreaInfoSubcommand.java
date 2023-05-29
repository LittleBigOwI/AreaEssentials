package net.philocraft.commands.subcommands.area;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import dev.littlebigowl.api.constants.Colors;
import dev.littlebigowl.api.errors.InvalidArgumentsException;
import net.md_5.bungee.api.ChatColor;
import net.philocraft.errors.NoAreaException;
import net.philocraft.models.Area;
import net.philocraft.models.Subcommand;
import net.philocraft.utils.AreaUtil;

public class AreaInfoSubcommand extends Subcommand {

    @Override
    public String getName() {
        return "info";
    }

    @Override
    public String getDescription() {
        return "Displays information about an area";
    }

    @Override
    public String getSyntax() {
        return "/area info";
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

        Area area = AreaUtil.getAreas().get(i);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyy HH:mm:ss z");
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+2"));

        String info = "" +
            ChatColor.WHITE + "Area Info :" + ChatColor.RESET + "\n" +
            Colors.INFO.getChatColor() + "[Owner] - " + Colors.INFO_DARK.getChatColor() + Bukkit.getOfflinePlayer(area.getUUID()).getName() + "\n" +
            Colors.INFO.getChatColor() + "[Name] - " + Colors.INFO_DARK.getChatColor() + area.getName() + "\n" +
            Colors.INFO.getChatColor() + "[Surface] - " + Colors.INFO_DARK.getChatColor() + area.getSurface() + " m²\n" +
            Colors.INFO.getChatColor() + "[Creation] - " + Colors.INFO_DARK.getChatColor() + dateFormat.format(new Date(area.getCreationDate())) + "\n" +
            Colors.INFO.getChatColor() + "[mobGriefing] - " + Colors.INFO_DARK.getChatColor() + area.getPermission("mobGriefing") + "\n" +
            Colors.INFO.getChatColor() + "[doPVP] - " + Colors.INFO_DARK.getChatColor() + area.getPermission("doPVP")
        ;

        player.sendMessage(info);
        return true;
    }
    
}
