package net.philocraft.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import net.philocraft.models.Area;
import net.philocraft.utils.AreaUtil;

public class OnEntityDamageEvent implements Listener {
    
    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {

        if(event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
            Player attacker = (Player)event.getDamager();
            Player attacked = (Player)event.getEntity();

            Area attackerArea = AreaUtil.getArea(attacker.getLocation());
            Area attackedArea = AreaUtil.getArea(attacked.getLocation());

            if((attackerArea != null && !attackerArea.getPermission("doPVP")) || (attackedArea != null && !attackedArea.getPermission("doPVP"))) {
                event.setCancelled(true);
            }
        }

    }
}
