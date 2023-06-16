package net.philocraft.events;

import org.bukkit.entity.Creeper;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;

import net.philocraft.models.Area;
import net.philocraft.utils.AreaUtil;

public class OnEntityExplodeEvent implements Listener {

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
        
        Entity entity = event.getEntity();
        if(event.getEntity() instanceof Creeper) {
            Area area = AreaUtil.getArea(entity.getLocation());
            
            if(area != null && !(area.getPermission("mobGriefing"))) {
                event.setCancelled(true);
            }
        }

    }
    
}