package net.philocraft.models;

import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.flowpowered.math.vector.Vector2i;

import de.bluecolored.bluemap.api.AssetStorage;
import de.bluecolored.bluemap.api.BlueMapMap;
import de.bluecolored.bluemap.api.markers.MarkerSet;
import de.bluecolored.bluemap.api.markers.POIMarker;
import net.philocraft.AreaEssentials;

public class Warp {
    
    private UUID uuid;
    private String name;

    private Location location;

    public Warp(UUID uuid, String name, Location location) {
        this.uuid = uuid;
        this.name = name;

        this.location = location;
    }

    public UUID getUUID() {
        return this.uuid;
    }

    public String getName() {
        return this.name;
    }

    public double getX() {
        return this.location.getX();
    }
    
    public double getY() {
        return this.location.getY();
    }

    public double getZ() {
        return this.location.getZ();
    }

    public double getYaw() {
        return this.location.getYaw();
    }

    public double getPitch() {
        return this.location.getPitch();
    }

    public void teleport(Player player) {
        player.teleport(this.location);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void draw() {

        POIMarker marker = POIMarker.builder()
            .label(this.getName())
            .minDistance(0.0)
            .maxDistance(500)
            .position(this.getX(), this.getY(), this.getZ())
            .build();

        AreaEssentials.blueMap.getWorld("world").ifPresent(world -> {
            for(BlueMapMap map : world.getMaps()) {
                AssetStorage assetStorage = map.getAssetStorage();
                String icon = assetStorage.getAssetUrl("markericons/pin.png");

                marker.setIcon(icon, new Vector2i(12, 24));

                if(map.getMarkerSets().get("Warps") != null) {
                    map.getMarkerSets().get("Warps").put(this.getName(), marker);

                } else {
                    MarkerSet markerSet = MarkerSet.builder().label("Warps").build();
                    map.getMarkerSets().put("Warps", markerSet);
                    map.getMarkerSets().get("Warps").put(this.getName(), marker);

                }
            }
        });
    }

    public void erase() {
        AreaEssentials.blueMap.getWorld("world").ifPresent(world -> {
            for(BlueMapMap map : world.getMaps()) {         
                map.getMarkerSets().get("Warps").remove(this.getName());
            }
        });
    }
}