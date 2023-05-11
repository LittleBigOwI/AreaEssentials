package net.philocraft.events;

import org.bukkit.entity.Enderman;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;

import net.philocraft.models.Area;
import net.philocraft.utils.AreaUtil;

public class OnEntityChangeBlockEvent implements Listener {
    
    @EventHandler
    public void onEntityChangeBlock(EntityChangeBlockEvent event) {

        Entity entity = event.getEntity();
        if(entity instanceof Enderman) {
            Area area = AreaUtil.getArea(entity.getLocation());
            
            if(area != null && !(area.getPermission("mobGriefing"))) {
                event.setCancelled(true);
            }
        }
    }

}
