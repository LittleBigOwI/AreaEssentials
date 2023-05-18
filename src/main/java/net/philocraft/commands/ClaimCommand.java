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
import net.philocraft.commands.subcommands.claim.ClaimBlocksSubcommand;
import net.philocraft.commands.subcommands.claim.ClaimBuySubcommand;
import net.philocraft.commands.subcommands.claim.ClaimOffSubcommand;
import net.philocraft.commands.subcommands.claim.ClaimOnSubcommand;
import net.philocraft.commands.subcommands.claim.ClaimShopSubcommand;
import net.philocraft.models.Subcommand;
import net.philocraft.utils.ClaimUtil;

public class ClaimCommand implements CommandExecutor, TabCompleter {

    private ArrayList<Subcommand> subcommands = new ArrayList<>();

    public ClaimCommand() {
        this.subcommands.add(new ClaimOnSubcommand());
        this.subcommands.add(new ClaimOffSubcommand());

        this.subcommands.add(new ClaimBlocksSubcommand());
        this.subcommands.add(new ClaimShopSubcommand());
        this.subcommands.add(new ClaimBuySubcommand());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        
        if(!(sender instanceof Player)) {
            return new InvalidSenderException().sendCause(sender);
        }

        Player player = (Player)sender;
        
        if(args.length == 0) {
            ClaimUtil.toggleClaimMode(player);
            player.sendMessage(Colors.SUCCESS.getChatColor() + "Toggled claim mode " + ClaimUtil.getFormatedClaimMode(player) + ".");
            return true;
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
            return new ArrayList<>(Arrays.asList("on", "off", "blocks", "shop"));
        }
        return new ArrayList<>();
    }
    
}
