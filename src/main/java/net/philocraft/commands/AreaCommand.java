package net.philocraft.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import dev.littlebigowl.api.constants.Worlds;
import dev.littlebigowl.api.errors.InvalidArgumentsException;
import dev.littlebigowl.api.errors.InvalidSenderException;
import dev.littlebigowl.api.errors.InvalidWorldException;
import net.philocraft.commands.subcommands.area.AreaCreateSubcommand;
import net.philocraft.commands.subcommands.area.AreaEditSubcommand;
import net.philocraft.commands.subcommands.area.AreaExpandSubcommand;
import net.philocraft.commands.subcommands.area.AreaHideSubcommand;
import net.philocraft.commands.subcommands.area.AreaInfoSubcommand;
import net.philocraft.commands.subcommands.area.AreaRemoveSubommand;
import net.philocraft.commands.subcommands.area.AreaShowSubcommand;
import net.philocraft.commands.subcommands.area.AreaShrinkSubcommand;
import net.philocraft.models.Subcommand;
import net.philocraft.utils.AreaUtil;

public class AreaCommand implements CommandExecutor, TabCompleter {

    private ArrayList<Subcommand> subcommands = new ArrayList<>();

    public AreaCommand() {
        this.subcommands.add(new AreaCreateSubcommand());
        this.subcommands.add(new AreaRemoveSubommand());

        this.subcommands.add(new AreaShowSubcommand());
        this.subcommands.add(new AreaHideSubcommand());

        this.subcommands.add(new AreaExpandSubcommand());
        this.subcommands.add(new AreaShrinkSubcommand());
        
        this.subcommands.add(new AreaInfoSubcommand());
        this.subcommands.add(new AreaEditSubcommand());
    }

    public ArrayList<Subcommand> getSubCommands() {
        return this.subcommands;
    }

    public ArrayList<String> getSubcommandsNames() {
        ArrayList<String> names = new ArrayList<>();
        this.subcommands.forEach(subcommand -> names.add(subcommand.getName()));

        return names;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(!(sender instanceof Player)) {
            return new InvalidSenderException().sendCause(sender);
        }

        Player player = (Player)sender;

        if(!player.getWorld().equals(Worlds.OVERWORLD.getWorld())) {
            return new InvalidWorldException().sendCause(player);   
        }

        boolean performedCommand = false;

        if(args.length > 0) {
            for(int i = 0; i < this.subcommands.size(); i++) {
                if(args[0].equalsIgnoreCase(this.subcommands.get(i).getName())) {
                    this.subcommands.get(i).perform(player, args);
                    performedCommand = true;
                }
            }
        }

        if(!performedCommand) {
            return new InvalidArgumentsException().sendCause(sender);
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        
        if(label.equalsIgnoreCase("area") && args.length == 1) {
            return Arrays.asList("show", "hide", "info", "expand", "shrink", "create", "remove", "edit");

        } else if(label.equalsIgnoreCase("area") && args.length == 2 && args[0].equals("edit")) {
            return Arrays.asList("name", "color", "enterMessage", "leaveMessage", "permissions");

        } else if(label.equalsIgnoreCase("area") && args.length == 3 && args[0].equals("edit") && args[1].equals("permissions")) {
            return AreaUtil.getPermissionKeys();

        } else if(label.equalsIgnoreCase("area") && args.length == 3 && args[0].equals("edit") && (args[1].equals("enterMessage") || args[1].equals("leaveMessage"))) {
            return Arrays.asList("set", "remove");

        } else if(label.equalsIgnoreCase("area") && args.length == 4 && args[0].equals("edit") && args[1].equals("permissions") && AreaUtil.getPermissionKeys().contains(args[2])) {
            return Arrays.asList("true", "false");

        }
        
        return new ArrayList<>();
    }
    
}
