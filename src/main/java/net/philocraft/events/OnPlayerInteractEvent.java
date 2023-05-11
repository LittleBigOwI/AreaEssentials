package net.philocraft.events;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

import dev.littlebigowl.api.constants.Colors;
import net.philocraft.AreaEssentials;
import net.philocraft.components.AreaCreateComponent;
import net.philocraft.errors.AreaExistsException;
import net.philocraft.models.Area;
import net.philocraft.utils.AreaUtil;
import net.philocraft.utils.ClaimUtil;

public class OnPlayerInteractEvent implements Listener {

    private static HashMap<Player, ArrayList<Block>> corners = new HashMap<>();

    private static void addCorner(Player player, Block block, int index) {
        ArrayList<Block> playerCorners = corners.get(player);
        if(playerCorners == null) {
            playerCorners = new ArrayList<>();
        }
        
        if(playerCorners.size() > index && playerCorners.get(index) != null) {
            playerCorners.remove(index);
        }

        if(playerCorners.size() < 1 && index == 1) {
            playerCorners.add(0, block);
        }

        playerCorners.add(index, block);
        corners.put(player, playerCorners);
    }

    private static Area setupArea(Player player) {
        BoundingBox boundingBox = BoundingBox.of(
            corners.get(player).get(0),
            corners.get(player).get(1)
        );

        boundingBox.expand(new Vector(0, 320, 0));
        boundingBox.expand(new Vector(0, -64, 0));

        Area area = new Area(
            player.getName() + "-" + AreaUtil.getAreas(player).size(), 
            AreaEssentials.api.scoreboard.getEssentialsTeam(player).getColor(), 
            player.getUniqueId(), 
            boundingBox
        );

        Bukkit.getLogger().warning(area.getCreationDate() + "");

        if(!area.isValid()) {
            return null;
        }

        return area;
    }
    
    @EventHandler
    public void OnPlayerInteract(PlayerInteractEvent event) {
        Action action = event.getAction();
        Player player = event.getPlayer();
        Block block = event.getClickedBlock();

        if(!ClaimUtil.getClaimMode(player) || block == null) {
            return;
        }

        block = block.getRelative(event.getBlockFace());

        int i = 0;
        while(i < AreaUtil.getAreas().size() && !AreaUtil.getAreas().get(i).contains(block)) {
            i++;
        }

        if(i != AreaUtil.getAreas().size() && event.getHand().equals(EquipmentSlot.HAND)) {
            new AreaExistsException().sendCause(player);
            return;
        }

        if(action == Action.LEFT_CLICK_BLOCK) {
            OnPlayerInteractEvent.addCorner(player, block, 0);
            player.sendMessage(Colors.SUCCESS.getChatColor() + "Position #1 is set.");

            if(corners.get(player).size() == 2 && OnPlayerInteractEvent.setupArea(player) != null) {
                new AreaCreateComponent(player, "Area selected! Click this message to create it!").send();
                AreaUtil.addPotentialArea(player, OnPlayerInteractEvent.setupArea(player));
            
            } else if(corners.get(player).size() == 2 && OnPlayerInteractEvent.setupArea(player) == null) {
                new AreaExistsException().sendCause(player);

            }

        } else if(action == Action.RIGHT_CLICK_BLOCK && event.getHand().equals(EquipmentSlot.HAND)) {            
            OnPlayerInteractEvent.addCorner(player, block, 1);
            player.sendMessage(Colors.SUCCESS.getChatColor() + "Position #2 is set.");

            if(corners.get(player).size() == 2 && OnPlayerInteractEvent.setupArea(player) != null) {
                new AreaCreateComponent(player, "Area selected! Click this message to create it!").send();
                AreaUtil.addPotentialArea(player, OnPlayerInteractEvent.setupArea(player));
            
            } else if(corners.get(player).size() == 2 && OnPlayerInteractEvent.setupArea(player) == null) {
                new AreaExistsException().sendCause(player);

            }
        }

        event.setCancelled(true);
    }

}
