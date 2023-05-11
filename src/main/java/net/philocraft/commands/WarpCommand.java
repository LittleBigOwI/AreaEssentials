package net.philocraft.commands;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import dev.littlebigowl.api.errors.InvalidArgumentsException;
import dev.littlebigowl.api.errors.InvalidSenderException;
import net.philocraft.errors.WarpNotFoundException;
import net.philocraft.models.Warp;
import net.philocraft.utils.WarpUtil;

public class WarpCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player && label.equalsIgnoreCase("warp"))) {
            return new InvalidSenderException("You need to be a player to use this command.").sendCause(sender);
        }

        if(args.length != 1) {
            return new InvalidArgumentsException().sendCause(sender);
        }

        Player player = (Player)sender;
        Warp warp = WarpUtil.getWarp(args[0]);

        if(warp == null) {
            return new WarpNotFoundException().sendCause(sender);
        }

        warp.teleport(player);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return WarpUtil.getWarpNames();
    }
    
}
