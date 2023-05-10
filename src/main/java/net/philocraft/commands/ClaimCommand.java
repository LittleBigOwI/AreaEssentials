package net.philocraft.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import dev.littlebigowl.api.constants.Colors;
import dev.littlebigowl.api.errors.InvalidArgumentsException;
import dev.littlebigowl.api.errors.InvalidSenderException;
import net.philocraft.utils.DatabaseUtil;

public class ClaimCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        
        if(!(sender instanceof Player)) {
            return new InvalidSenderException().sendCause(sender);
        }

        if(args.length > 1) {
            return new InvalidArgumentsException().sendCause(sender);
        }

        if(args.length == 1 && !args[0].equals("on") && !args[0].equals("off")) {
            return new InvalidArgumentsException("You can only turn claim mode on or off.").sendCause(sender);
        }

        Player player = (Player)sender;
        
        if(args.length == 0) {
            DatabaseUtil.toggleClaimMode(player);
            player.sendMessage(Colors.SUCCESS.getChatColor() + "Toggled claim mode " + DatabaseUtil.getFormatedClaimMode(player) + ".");

        } else if(args[0].equals("on")) {
            DatabaseUtil.setClaimModeOn(player);
            player.sendMessage(Colors.SUCCESS.getChatColor() + "Claim mode activated.");
        
        } else {
            DatabaseUtil.setClaimModeOff(player);
            player.sendMessage(Colors.SUCCESS.getChatColor() + "Claim mode deactivated.");

        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if(args.length == 1) {
            return new ArrayList<>(Arrays.asList("on", "off", "blocks"));
        }
        return new ArrayList<>();
    }
    
}
