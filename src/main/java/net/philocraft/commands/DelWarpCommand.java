package net.philocraft.commands;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import dev.littlebigowl.api.components.WarningComponent;
import dev.littlebigowl.api.constants.Colors;
import dev.littlebigowl.api.errors.InvalidArgumentsException;
import dev.littlebigowl.api.errors.InvalidSenderException;
import net.philocraft.AreaEssentials;
import net.philocraft.models.Warp;
import net.philocraft.utils.WarpUtil;

public class DelWarpCommand implements CommandExecutor, TabCompleter {
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player && label.equalsIgnoreCase("delwarp"))) {
            return new InvalidSenderException("You need to be a player to use this command.").sendCause(sender);
        }

        if(args.length != 0 && args.length != 1) {
            return new InvalidArgumentsException().sendCause(sender);
        }

        Player player = (Player)sender;

        Warp warp = WarpUtil.getWarp(player.getUniqueId());
        
        if(warp == null) {
            return new InvalidArgumentsException("Could not find warp.").sendCause(sender);
        }
        
        if(args.length == 1) {
            if(args[0].equals("confirm")) {
                try {
                    WarpUtil.removeWarp(warp);
                } catch (SQLException e) {
                    AreaEssentials.getPlugin().getLogger().severe("Couldn't remove warp : " + e.getMessage());
                }

                warp.erase();
                player.sendMessage(Colors.SUCCESS.getChatColor() + "Successfully deleted " + Colors.SUCCESS_DARK.getChatColor() + warp.getName() + Colors.SUCCESS.getChatColor() + ".");

            } else if(args[0].equals("cancel")) {
                player.sendMessage(Colors.SUCCESS.getChatColor() + "Successfully canceled deletion.");

            } else {
                return new InvalidArgumentsException().sendCause(sender);
            }

        } else if(args.length == 0){
            new WarningComponent(
                player,
                new String[]{"You are about to delete ", warp.getName(), ". Proceed? "},
                "/delwarp confirm",
                "/delwarp cancel"
            ).send();
            
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return new ArrayList<>();
    }
    
}
