package net.philocraft.commands.subcommands.claim;

import org.bukkit.entity.Player;

import dev.littlebigowl.api.errors.InvalidArgumentsException;
import net.philocraft.components.ShopOpenComponent;
import net.philocraft.models.Subcommand;

public class ClaimShopSubcommand extends Subcommand {

    @Override
    public String getName() {
        return "shop";
    }

    @Override
    public String getDescription() {
        return "Opens the claim shop";
    }

    @Override
    public String getSyntax() {
        return "/claim shop";
    }

    @Override
    public boolean perform(Player player, String[] args) {
        if(args.length > 1) {
            return new InvalidArgumentsException().sendCause(player);
        }

        new ShopOpenComponent(
            player, 
            new String[][]{
                {"[1] ", "50 claim blocks", " [Buy for 1 diamond]\n"},
                {"[2] ", "3200 claim blocks", " [Buy for 64 diamond]\n"},
                {"[3] ", "450 claim blocks", " [Buy for 1 diamond block]\n"},
                {"[4] ", "28800 claim blocks", " [Buy for 64 diamond blocks]"}
            },
            new String[]{
                "/claim buy 1 DIAMOND 50",
                "/claim buy 64 DIAMOND 3200",
                "/claim buy 1 DIAMOND_BLOCK 450",
                "/claim buy 64 DIAMOND_BLOCK 28800"
            }
        ).send();
        
        return true;
    }

}
