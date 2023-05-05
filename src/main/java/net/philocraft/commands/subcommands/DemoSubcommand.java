package net.philocraft.commands.subcommands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import dev.littlebigowl.api.errors.InvalidArgumentsException;
import net.philocraft.models.SubCommand;

public class DemoSubcommand extends SubCommand {

    @Override
    public String getName() {
        return "on";
    }

    @Override
    public String getDescription() {
        return "turns claiming mode on";
    }

    @Override
    public String getSyntax() {
        return "/claim on";
    }

    @Override
    public boolean perform(Player player, String[] args) {
        
        if(args.length < 1) {
            return new InvalidArgumentsException().sendCause((CommandSender)player);
        }

        player.sendMessage("You are " + player.getName());

        return true;

    }
    
}
