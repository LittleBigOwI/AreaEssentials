package net.philocraft.events;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import dev.littlebigowl.api.constants.Colors;
import net.philocraft.utils.DatabaseUtil;

public class OnPlayerInteractEvent implements Listener {

    private static ArrayList<Player> clicks = new ArrayList<>();
    
    @EventHandler
    public void OnPlayerInteract(PlayerInteractEvent event) {
        Action act = event.getAction();
        Player player = event.getPlayer();
        Location location = event.getClickedBlock().getLocation();

        if(!DatabaseUtil.getClaimMode(player)) {
            return;
        }
        
        String loc = "[X=" + location.getBlockX() + ", Y=" + location.getBlockZ() + "]";

        if(act == Action.LEFT_CLICK_BLOCK) {
            player.sendMessage(Colors.SUCCESS.getChatColor() + "Set first corner to " + loc + ".");

        } else if(act == Action.RIGHT_CLICK_BLOCK) {
            if(clicks.contains(player)) {
                clicks.remove(player);
            } else {
                clicks.add(player);
                player.sendMessage(Colors.SUCCESS.getChatColor() + "Set second corner to " + loc + ".");
            }
            
        }

        event.setCancelled(true);
    }

}
