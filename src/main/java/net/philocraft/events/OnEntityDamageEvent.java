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

            if(attackerArea != null) {
                Boolean pvpAttackerAttacker = attackerArea.getPlayerPermission("doPVP", attacker.getUniqueId());            
                Boolean pvpAttackerAttacked = attackerArea.getPlayerPermission("doPVP", attacked.getUniqueId());

                if(pvpAttackerAttacker != null && !pvpAttackerAttacker && pvpAttackerAttacked != null && !pvpAttackerAttacked) {
                    event.setCancelled(true);
                }
            }

            if(attackedArea != null) {
                Boolean pvpAttackedAttacked = attackedArea.getPlayerPermission("doPVP", attacked.getUniqueId());
                Boolean pvpAttackedAttacker = attackedArea.getPlayerPermission("doPVP", attacker.getUniqueId());

                if(pvpAttackedAttacker != null && !pvpAttackedAttacker && pvpAttackedAttacked != null && !pvpAttackedAttacked) {
                    event.setCancelled(true);
                }
            }

            if((attackerArea != null && !attackerArea.getPermission("doPVP")) || (attackedArea != null && !attackedArea.getPermission("doPVP"))) {
                event.setCancelled(true);
            }
        }

    }
}
