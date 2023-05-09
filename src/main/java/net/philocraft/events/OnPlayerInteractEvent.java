package net.philocraft.events;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

import com.flowpowered.math.vector.Vector2d;

import dev.littlebigowl.api.constants.Colors;
import net.philocraft.AreaEssentials;
import net.philocraft.components.AreaCreateComponent;
import net.philocraft.models.Area;
import net.philocraft.utils.DatabaseUtil;

public class OnPlayerInteractEvent implements Listener {

    private static HashMap<Player, ArrayList<Vector2d>> corners = new HashMap<>();

    private static void addCorner(Player player, Location location, int index) {
        ArrayList<Vector2d> playerCorners = corners.get(player);
        if(playerCorners == null) {
            playerCorners = new ArrayList<>();
        }

        Vector2d corner = new Vector2d(location.getBlockX(), location.getBlockZ());
        
        if(playerCorners.size() > index && playerCorners.get(index) != null) {
            playerCorners.remove(index);
        }
        playerCorners.add(index, corner);

        corners.put(player, playerCorners);
    }

    private static Area setupArea(Player player) {
        Area area = new Area(
            player.getName() + "-Area-" + DatabaseUtil.getAreas(player).size(), 
            AreaEssentials.api.scoreboard.getEssentialsTeam(player).getColor(), 
            player.getUniqueId(), 
            corners.get(player).get(0),
            corners.get(player).get(1)
        );

        if(area.getSurface() < 225) {
            return null;
        }

        return area;
    }
    
    @EventHandler
    public void OnPlayerInteract(PlayerInteractEvent event) {
        Action act = event.getAction();
        Player player = event.getPlayer();
        Block block = event.getClickedBlock();
        
        Location location = null;
        if(block != null) {
            location = block.getLocation();
        }

        if(!DatabaseUtil.getClaimMode(player) || block == null) {
            return;
        }
        
        String loc = location.getBlockX() + " " + location.getBlockZ();

        if(act == Action.LEFT_CLICK_BLOCK) {
            OnPlayerInteractEvent.addCorner(player, location, 0);
            player.sendMessage(Colors.SUCCESS.getChatColor() + "Position #1 set to " + loc + ".");

            if(corners.get(player).size() == 2 && OnPlayerInteractEvent.setupArea(player) != null) {
                new AreaCreateComponent(player, "Area selected! Click this message to create it!").send();
                DatabaseUtil.addPotentialArea(player, OnPlayerInteractEvent.setupArea(player));
            
            } else if(corners.get(player).size() == 2 && OnPlayerInteractEvent.setupArea(player) == null) {
                player.sendMessage(Colors.FAILURE.getChatColor() + "Area is to small! Area needs to be at least 15x15.");

            }

        } else if(act == Action.RIGHT_CLICK_BLOCK && event.getHand().equals(EquipmentSlot.HAND)) {            
            OnPlayerInteractEvent.addCorner(player, location, 1);
            player.sendMessage(Colors.SUCCESS.getChatColor() + "Position #2 set to " + loc + ".");

            if(corners.get(player).size() == 2 && OnPlayerInteractEvent.setupArea(player) != null) {
                new AreaCreateComponent(player, "Area selected! Click this message to create it!").send();
                DatabaseUtil.addPotentialArea(player, OnPlayerInteractEvent.setupArea(player));
            
            } else if(corners.get(player).size() == 2 && OnPlayerInteractEvent.setupArea(player) == null) {
                player.sendMessage(Colors.FAILURE.getChatColor() + "Area is to small! Area needs to be at least 15x15.");

            }
        }

        event.setCancelled(true);
    }

}
