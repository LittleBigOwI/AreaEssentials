package net.philocraft.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import dev.littlebigowl.api.components.WarningComponent;
import dev.littlebigowl.api.constants.Colors;
import dev.littlebigowl.api.constants.Worlds;
import dev.littlebigowl.api.errors.InvalidArgumentsException;
import dev.littlebigowl.api.errors.InvalidSenderException;
import dev.littlebigowl.api.errors.InvalidWorldException;
import net.philocraft.AreaEssentials;
import net.philocraft.models.Warp;
import net.philocraft.utils.WarpUtil;

public class SetWarpCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player && label.equalsIgnoreCase("setwarp"))) {
            return new InvalidSenderException("You need to be a player to use this command.").sendCause(sender);
        }

        Player player = (Player) sender;

        if(!player.getWorld().equals(Worlds.OVERWORLD.getWorld())) {
            return new InvalidWorldException("Warps are only available in the overword.").sendCause(sender);
        }

        if(args.length != 1 && args.length != 2) {
            return new InvalidArgumentsException().sendCause(sender);
        }

        String name = args[0];
        Location location = player.getLocation();

        if(name.contains("'") || name.contains("\\") || name.contains("\"")) {
            return new InvalidArgumentsException("Warp names can't contain \\, ' or \" characters.").sendCause(sender);
        }

        Warp warp = WarpUtil.getWarp(player.getUniqueId());

        for(Warp w : WarpUtil.getWarps()) {
            if(warp != null && w.getName().equals(warp.getName()) && !warp.getUUID().equals(w.getUUID())) {
                return new InvalidArgumentsException("There is already a warp with that name.").sendCause(player);
            }
        }
        
        if(warp != null && args.length == 2) {
            if(args[1].equals("override")) {
                String warpName = args[0];

                for(Warp w : WarpUtil.getWarps()) {
                    if(w.getName().equals(warpName) && !w.getUUID().equals(player.getUniqueId())) {
                        return new InvalidArgumentsException("There is already a warp with that name.").sendCause(player);
                    }
                }
                
                warp.erase();
                warp.setLocation(location);
                warp.setName(warpName);

                try {
                    WarpUtil.saveWarp(warp);
                } catch (Exception e) {
                    AreaEssentials.getPlugin().getLogger().severe("Couldn't save warp : " + e.getMessage());
                }

                warp.draw();
                player.sendMessage(Colors.SUCCESS.getChatColor() + "Successfully overridden " + Colors.SUCCESS_DARK.getChatColor() + warp.getName() + Colors.SUCCESS.getChatColor() + ".");

            } else if(args[1].equals("cancel")) {
                player.sendMessage(Colors.SUCCESS.getChatColor() + "Successfully canceled override.");

            } else {
                return new InvalidArgumentsException().sendCause(sender);
            }

        } else if(warp != null){
            new WarningComponent(
                player,
                new String[]{"You are about to override ", warp.getName(), ". Proceed? "},
                "/setwarp " + args[0] + " override",
                "/setwarp " + args[0] + " cancel"
            ).send();
            
        } else {
            warp = new Warp(player.getUniqueId(), name, location);

            for(Warp w : WarpUtil.getWarps()) {
                if(w.getName().equals(warp.getName()) && !warp.getUUID().equals(w.getUUID())) {
                    return new InvalidArgumentsException("There is already a warp with that name.").sendCause(player);
                }
            }
            
            try {
                WarpUtil.saveWarp(warp);
            } catch (Exception e) {
                AreaEssentials.getPlugin().getLogger().severe("Couldn't save warp : " + e.getMessage());
            }

            warp.draw();
            player.sendMessage(Colors.SUCCESS.getChatColor() + "Successfully created new " + Colors.SUCCESS_DARK.getChatColor() + warp.getName() + Colors.SUCCESS.getChatColor() + " warp.");
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return new ArrayList<>();
    }
    
}
