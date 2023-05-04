package net.philocraft.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class OnPlayerInteractEvent implements Listener {
    
    @EventHandler
    public void OnPlayerInteract(PlayerInteractEvent event) {
        Action act = event.getAction();
        Player player = event.getPlayer();

        if(act == Action.RIGHT_CLICK_BLOCK) {
            event.setCancelled(true);
            player.sendMessage("NO.");
        } else if(act == Action.LEFT_CLICK_BLOCK) {
            event.setCancelled(true);
            player.sendMessage("NO.");
        }
    }

}
