package net.philocraft.commands.subcommands.claim;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import dev.littlebigowl.api.constants.Colors;
import dev.littlebigowl.api.errors.InvalidArgumentsException;
import net.philocraft.components.ShopOpenComponent;
import net.philocraft.models.Subcommand;
import net.philocraft.utils.ClaimUtil;

public class ClaimBuySubcommand extends Subcommand {

    @Override
    public String getName() {
        return "buy";
    }

    @Override
    public String getDescription() {
        return "Buys claim blocks from the claim shop";
    }
    
    @Override
    public String getSyntax() {
        return "/claim buy";
    }

    @Override
    public boolean perform(Player player, String[] args) {
        if(args.length != 4) {
            return new InvalidArgumentsException().sendCause(player);
        }

        int currencyAmount = Integer.parseInt(args[1]);
        Material currencyMaterial = Material.getMaterial(args[2]);
        int productAmount = Integer.parseInt(args[3]);

        String command = "/claim buy " + currencyAmount + " " + currencyMaterial + " " + productAmount;
        
        int i = 0;
        while(i < ShopOpenComponent.getPossibleCommands().size() && !ShopOpenComponent.getPossibleCommands().get(i).equals(command)) {
            i++;
        }

        if(i == ShopOpenComponent.getPossibleCommands().size()) {
            return new InvalidArgumentsException().sendCause(player);
        }

        if(player.getInventory().getItemInMainHand().getType() != currencyMaterial) {
            return new InvalidArgumentsException("You don't have the correct material").sendCause(player);
        }

        if(player.getInventory().getItemInMainHand().getAmount() < currencyAmount) {
            return new InvalidArgumentsException("You don't have the correct amount of material").sendCause(player);
        }

        int playerAmount = player.getInventory().getItemInMainHand().getAmount();
        if(playerAmount == currencyAmount) {
            player.getInventory().setItemInMainHand(null);
        
        } else {
            player.getInventory().getItemInMainHand().setAmount(playerAmount - currencyAmount);
        
        }

        ClaimUtil.addClaimBlocks(player, productAmount);
        player.sendMessage(
            Colors.SUCCESS.getChatColor() + "Successfully purchased " + 
            Colors.SUCCESS_DARK.getChatColor() + productAmount + 
            Colors.SUCCESS.getChatColor() + " claims blocks from the shop."
        );
        return true;
    }
    
}
