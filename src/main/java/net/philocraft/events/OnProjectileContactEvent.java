package net.philocraft.events;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.projectiles.ProjectileSource;

import net.philocraft.models.Area;
import net.philocraft.utils.AreaUtil;

public class OnProjectileContactEvent implements Listener {
    
    @EventHandler
    public void onProjectileContact(ProjectileHitEvent event) {
        ProjectileSource source = event.getEntity().getShooter();
        Entity target = event.getHitEntity();

        if(source != null && target != null && source instanceof Player && target instanceof Player) {
            Player shooter = (Player)source;
            Area shooterArea = AreaUtil.getArea(shooter.getLocation());            
            Area targetArea = AreaUtil.getArea(target.getLocation());

            if((shooterArea != null && !shooterArea.getPermission("doPVP")) || (targetArea != null && !targetArea.getPermission("doPVP"))) {
                event.setCancelled(true);
            }
        }

    }

}
