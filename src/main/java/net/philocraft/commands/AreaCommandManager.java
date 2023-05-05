package net.philocraft.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import dev.littlebigowl.api.errors.InvalidSenderException;
import net.philocraft.commands.subcommands.DemoSubcommand;
import net.philocraft.models.SubCommand;

public class AreaCommandManager implements CommandExecutor, TabCompleter {

    private ArrayList<SubCommand> subcommands = new ArrayList<>();

    public AreaCommandManager() {
        this.subcommands.add(new DemoSubcommand());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(!(sender instanceof Player)) {
            return new InvalidSenderException().sendCause(sender);
        }

        Player player = (Player)sender;

        if(args.length > 0) {
            for(int i = 0; i < this.subcommands.size(); i++) {
                if(args[0].equalsIgnoreCase(this.subcommands.get(i).getName())) {
                    this.subcommands.get(i).perform(player, args);
                }
            }
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return new ArrayList<>();
    }

    public ArrayList<SubCommand> getSubCommands() {
        return this.subcommands;
    }
    
}
