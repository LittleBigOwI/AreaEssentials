package net.philocraft.commands.subcommands;

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
import net.philocraft.utils.DatabaseUtil;

public class InfoSubcommand extends Subcommand {

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
        if(args.length > 1) {
            return new InvalidArgumentsException().sendCause(player);
        }

        int i = 0;
        while(i < DatabaseUtil.getAreas().size() && !DatabaseUtil.getAreas().get(i).contains(player)) {
            i++;
        }

        if(i == DatabaseUtil.getAreas().size()) {
            return new NoAreaException().sendCause(player);
        }

        Area area = DatabaseUtil.getAreas().get(i);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss z");
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+2"));

        String info = "" +
            ChatColor.WHITE + ChatColor.BOLD + "Area Info :" + ChatColor.RESET + "\n" +
            Colors.INFO.getChatColor() + "[Owner] - " + Colors.MAJOR.getChatColor() + Bukkit.getPlayer(area.getUUID()).getName() + "\n" +
            Colors.INFO.getChatColor() + "[Name] - " + Colors.MAJOR.getChatColor() + area.getName() + "\n" +
            Colors.INFO.getChatColor() + "[Surface] - " + Colors.MAJOR.getChatColor() + area.getSurface() + "\n" +
            Colors.INFO.getChatColor() + "[Creation] - " + Colors.MAJOR.getChatColor() + dateFormat.format(new Date(area.getCreationDate())) + "\n" +
            Colors.INFO.getChatColor() + "[mobGriefing] - " + Colors.MAJOR.getChatColor() + area.getPermission("mobGriefing") + "\n" +
            Colors.INFO.getChatColor() + "[doPVP] - " + Colors.MAJOR.getChatColor() + area.getPermission("doPVP")
        ;

        player.sendMessage(info);
        return true;
    }
    
}
