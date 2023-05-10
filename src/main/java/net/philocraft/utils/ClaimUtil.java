package net.philocraft.utils;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import dev.littlebigowl.api.constants.Colors;
import dev.littlebigowl.api.models.EssentialsScoreboard;
import net.philocraft.AreaEssentials;

public class ClaimUtil {
    
    private static HashMap<Player, Integer> playtimeBlocks = new HashMap<>();
    private static final int claimAmount = AreaEssentials.api.areas.getPassiveClaimAmount();
    private static final int claimInterval = AreaEssentials.api.areas.getPassiveClaimInterval();

    public static void checkPlaytimeBlocks() {

        AreaEssentials.getPlugin().getServer().getScheduler().runTaskTimer(AreaEssentials.getPlugin(), () -> {
            for(Player player : Bukkit.getOnlinePlayers()) {
                Integer playerClaimAmount = ClaimUtil.playtimeBlocks.get(player);

                if(playerClaimAmount == null) {
                    ClaimUtil.playtimeBlocks.put(player, EssentialsScoreboard.getPlaytime(player)/claimInterval);
                    playerClaimAmount = ClaimUtil.playtimeBlocks.get(player);
                }

                if(playerClaimAmount != EssentialsScoreboard.getPlaytime(player)/claimInterval) {
                    ClaimUtil.playtimeBlocks.put(player, EssentialsScoreboard.getPlaytime(player)/claimInterval);
                    
                    player.playSound(player.getEyeLocation(), Sound.ENTITY_PLAYER_LEVELUP, 2f, 2f);
                    player.sendMessage(
                        Colors.WARNING.getChatColor() + "You earned " + 
                        Colors.POPUP.getChatColor() + claimAmount + 
                        Colors.WARNING.getChatColor() + " claim blocks for playing " + 
                        Colors.POPUP.getChatColor() + claimInterval + 
                        Colors.WARNING.getChatColor() + " minutes!"
                    );
                }
            }
        }, 0, 20);

    }

    public static int getPassiveClaimBlocks(Player player) {
        return ClaimUtil.playtimeBlocks.get(player) * claimAmount;
    }

}
