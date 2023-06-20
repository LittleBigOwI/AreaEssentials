package net.philocraft.events;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

import dev.littlebigowl.api.constants.Colors;
import dev.littlebigowl.api.constants.Worlds;
import dev.littlebigowl.api.errors.InvalidWorldException;
import net.philocraft.AreaEssentials;
import net.philocraft.components.AreaCreateComponent;
import net.philocraft.errors.BadAreaException;
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

        if(block == null) {
            return;
        }

        if(!ClaimUtil.getClaimMode(player)) {
            Area area = AreaUtil.getArea(player.getLocation());

            if(area == null || player.isOp()) {
                return;
            }

            Boolean permission = area.getPlayerPermission("doInteracting", player.getUniqueId());

            if(action != Action.RIGHT_CLICK_BLOCK
                && (
                    !(block.getState() instanceof InventoryHolder)
                    || player.getInventory().getItemInMainHand().getType() != Material.WATER_BUCKET
                    || player.getInventory().getItemInMainHand().getType() != Material.LAVA_BUCKET
                )
            ) {
                return;
            }

            if(permission != null && !permission && !area.getPermission("doInteracting")) {
                event.getPlayer().sendMessage(Colors.FAILURE.getChatColor() + "You can't interact with blocks here.");
                event.setCancelled(true);

            } else if(permission != null && !permission && area.getPermission("doInteracting")) {
                event.getPlayer().sendMessage(Colors.FAILURE.getChatColor() + "You can't interact with blocks here.");
                event.setCancelled(true);

            } else if(permission == null && !area.getPermission("doInteracting") && !area.getUUID().equals(event.getPlayer().getUniqueId())) {
                event.getPlayer().sendMessage(Colors.FAILURE.getChatColor() + "You can't interact with blocks here.");
                event.setCancelled(true);
            }

        } else {
            block = block.getRelative(event.getBlockFace());

            int i = 0;
            while(i < AreaUtil.getAreas().size() && !AreaUtil.getAreas().get(i).contains(block)) {
                i++;
            }

            if(i != AreaUtil.getAreas().size() && event.getHand().equals(EquipmentSlot.HAND)) {
                new BadAreaException("There is already an area at this location.").sendCause(player);
                return;
            }

            if(!player.getWorld().equals(Worlds.OVERWORLD.getWorld())) {
                new InvalidWorldException().sendCause(player);
                return;
            }

            if(action == Action.LEFT_CLICK_BLOCK) {
                OnPlayerInteractEvent.addCorner(player, block, 0);
                
                if(corners.get(player).size() == 2 && OnPlayerInteractEvent.setupArea(player) == null) {
                    new BadAreaException("Invalid corner placement.").sendCause(player);
                    corners.put(player, new ArrayList<>());
                } else {
                    player.sendMessage(Colors.SUCCESS.getChatColor() + "Position #1 is set."); 
                }

                if(corners.get(player).size() == 2 && OnPlayerInteractEvent.setupArea(player) != null) {
                    new AreaCreateComponent(player, "Area selected! Click this message to create it!").send();
                    AreaUtil.addPotentialArea(player, OnPlayerInteractEvent.setupArea(player));
                }

            } else if(action == Action.RIGHT_CLICK_BLOCK && event.getHand().equals(EquipmentSlot.HAND)) {     
                OnPlayerInteractEvent.addCorner(player, block, 1);

                if(corners.get(player).size() == 2 && OnPlayerInteractEvent.setupArea(player) == null) {
                    new BadAreaException("Invalid corner placement.").sendCause(player);
                    corners.put(player, new ArrayList<>());
                } else {
                    player.sendMessage(Colors.SUCCESS.getChatColor() + "Position #2 is set."); 
                }

                if(corners.get(player).size() == 2 && OnPlayerInteractEvent.setupArea(player) != null) {
                    new AreaCreateComponent(player, "Area selected! Click this message to create it!").send();
                    AreaUtil.addPotentialArea(player, OnPlayerInteractEvent.setupArea(player));
                }
            }
            Bukkit.getLogger().warning("interact cancelled");
            event.setCancelled(true);
        }
    }

}
