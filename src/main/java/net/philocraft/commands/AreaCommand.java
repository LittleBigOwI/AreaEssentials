package net.philocraft.commands;

import java.util.ArrayList;
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
import net.philocraft.commands.subcommands.CreateSubcommand;
import net.philocraft.commands.subcommands.ExpandSubcommand;
import net.philocraft.commands.subcommands.HideSubcommand;
import net.philocraft.commands.subcommands.InfoSubcommand;
import net.philocraft.commands.subcommands.RemoveSubommand;
import net.philocraft.commands.subcommands.ShowSubcommand;
import net.philocraft.commands.subcommands.ShrinkSubcommand;
import net.philocraft.models.Subcommand;

public class AreaCommand implements CommandExecutor, TabCompleter {

    private ArrayList<Subcommand> subcommands = new ArrayList<>();

    public AreaCommand() {
        this.subcommands.add(new CreateSubcommand());
        this.subcommands.add(new RemoveSubommand());

        this.subcommands.add(new ShowSubcommand());
        this.subcommands.add(new HideSubcommand());

        this.subcommands.add(new ExpandSubcommand());
        this.subcommands.add(new ShrinkSubcommand());
        
        this.subcommands.add(new InfoSubcommand());
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
        if(args.length == 1) {
            return this.getSubcommandsNames();

        } else {
            return new ArrayList<>();
            
        }
    }
    
}
