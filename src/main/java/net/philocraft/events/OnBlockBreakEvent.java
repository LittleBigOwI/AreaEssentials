package net.philocraft.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import dev.littlebigowl.api.constants.Colors;
import net.philocraft.models.Area;
import net.philocraft.utils.AreaUtil;

public class OnBlockBreakEvent implements Listener {
    
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {

        Area area = AreaUtil.getArea(event.getPlayer().getLocation());

        if(area == null || event.getPlayer().isOp()) {
            return;
        }

        Boolean permission = area.getPlayerPermission("doBuilding", event.getPlayer().getUniqueId());

        if(permission != null && !permission && !area.getPermission("doBuilding")) {
            event.getPlayer().sendMessage(Colors.FAILURE.getChatColor() + "You can't break blocks here.");
            event.setCancelled(true);

        } else if(permission != null && !permission && area.getPermission("doBuilding")) {
            event.getPlayer().sendMessage(Colors.FAILURE.getChatColor() + "You can't break blocks here.");
            event.setCancelled(true);

        } else if(permission == null && !area.getPermission("doBuilding") && !area.getUUID().equals(event.getPlayer().getUniqueId())) {
            event.getPlayer().sendMessage(Colors.FAILURE.getChatColor() + "You can't break blocks here.");
            event.setCancelled(true);
        }

    }

}
