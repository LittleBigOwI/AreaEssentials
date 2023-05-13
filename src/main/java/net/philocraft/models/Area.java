package net.philocraft.models;

import java.awt.Color;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

import com.flowpowered.math.vector.Vector2d;

import de.bluecolored.bluemap.api.BlueMapMap;
import de.bluecolored.bluemap.api.markers.ExtrudeMarker;
import de.bluecolored.bluemap.api.markers.MarkerSet;
import de.bluecolored.bluemap.api.math.Shape;
import net.philocraft.AreaEssentials;
import net.philocraft.utils.AreaUtil;

public class Area {
    
    private String name;
    private String enterMessage;
    private String leaveMessage;
    private String groupName;

    private Color color;
    private long creationDate;
    private UUID uuid;

    private BoundingBox box;
    
    private HashMap<String, Boolean> permissions = new HashMap<>();

    public Area(String name, String enterMessage, String leaveMessage, String groupName, long creationDate, Color color, UUID uuid, BoundingBox box, boolean mobGriefing, boolean doPVP) {
        this.name = name;
        this.enterMessage = enterMessage;
        this.leaveMessage = leaveMessage;
        this.groupName = groupName;

        this.color = color;
        this.creationDate = creationDate;
        this.uuid = uuid;

        this.box = box;

        this.permissions.put("mobGriefing", mobGriefing);
        this.permissions.put("doPVP", doPVP);
    }

    public Area(String name, Color color, UUID uuid, BoundingBox box) {
        this(name, null, null, uuid.toString(), new Date().getTime(), color, uuid, box, false, false);
    }

    private boolean hasAirBlocksAbove(World world, Location location) {
        return (
            !world.getBlockAt((int)location.getX(), (int)location.getY(), (int)location.getZ()).getType().equals(Material.AIR) && 
            world.getBlockAt((int)location.getX(), (int)location.getY()+1, (int)location.getZ()).getType().equals(Material.AIR) &&
            world.getBlockAt((int)location.getX(), (int)location.getY()+2, (int)location.getZ()).getType().equals(Material.AIR)
        );
    }

    private void drawLineX(Player player, Vector2d v1, Vector2d v2, AreaAction action) {
        for(double i = v1.getX(); i < v2.getX(); i++) {

            Location location = new Location(player.getWorld(), i, -64, v1.getY());
            ArrayList<Location> possibleBlockLocations = new ArrayList<>();

            for(int j = -64; j <= 320; j++) {
                possibleBlockLocations.add(new Location(player.getWorld(), i, j, v1.getY()));
            }

            for(Location loc : possibleBlockLocations) {
                if(this.hasAirBlocksAbove(player.getWorld(), loc) && loc.distance(player.getLocation()) < location.distance(player.getLocation())) {
                    location = loc;
                }
            }
            
            if(action == AreaAction.SHOW) {
                player.sendBlockChange(location, Material.GOLD_BLOCK.createBlockData());

            } else {
                player.sendBlockChange(location, player.getWorld().getBlockData(location));

            }
        }
    }

    private void drawLineY(Player player, Vector2d v1, Vector2d v2, AreaAction action) {
        
        for(double i = v1.getY(); i < v2.getY(); i++) {
            
            Location location = new Location(player.getWorld(), v1.getX(), -64, i);
            ArrayList<Location> possibleBlockLocations = new ArrayList<>();
            
            for(int j = -64; j <= 320; j++) {
                possibleBlockLocations.add(new Location(player.getWorld(), v1.getX(), j, i));
            }

            for(Location loc : possibleBlockLocations) {
                if(this.hasAirBlocksAbove(player.getWorld(), loc) && loc.distance(player.getLocation()) < location.distance(player.getLocation())) {
                    location = loc;
                }
            }

            if(action == AreaAction.SHOW) {
                player.sendBlockChange(location, Material.GOLD_BLOCK.createBlockData());

            } else {
                player.sendBlockChange(location, player.getWorld().getBlockData(location));

            }
            
        }
    }

    private double getSurface(BoundingBox box) {
        return box.getWidthX() * box.getWidthZ();
    }

    private boolean isValid(BoundingBox box) {
        boolean surface = (this.getSurface(box) >= AreaEssentials.api.areas.getMinAreaSurface());
        boolean sides = (
            box.getWidthX() >= AreaEssentials.api.areas.getMinAreaWidthX() && 
            box.getWidthZ() >= AreaEssentials.api.areas.getMinAreaWidthY()
        );

        return (surface && sides);
    }

    private CardinalDirection getDirection(Player p) {
        double y = p.getLocation().getYaw();

        if(y >= 135 || y < -135){
            return CardinalDirection.NORTH;
        }
        if(y >= -135 && y < -45){
            return CardinalDirection.EAST;
        }
        if(y >= -45 && y < 45){
           return CardinalDirection.SOUTH;
        }
        if(y >= 45 && y < 135){
            return CardinalDirection.WEST;
        }

        return null;
    }

    private Vector2d[] getAnchorPoints() {
        Vector2d p1 = new Vector2d(this.box.getMinX(), this.box.getMinZ());
        Vector2d p2 = new Vector2d(this.box.getMaxX(), this.box.getMinZ());
        Vector2d p3 = new Vector2d(this.box.getMaxX(), this.box.getMaxZ());
        Vector2d p4 = new Vector2d(this.box.getMinX(), this.box.getMaxZ());

        return new Vector2d[]{p1, p2, p3, p4};
    }

    public String getName() {
        return this.name;
    }

    public String getEnterMessage() {
        return this.enterMessage;
    }

    public String getLeaveMessage() {
        return this.leaveMessage;
    }

    public String getGroupName() {
        return this.groupName;
    }

    public Color getColor() {
        return this.color;
    }

    public long getCreationDate() {
        return this.creationDate;
    }

    public UUID getUUID() {
        return this.uuid;
    }

    public boolean getPermission(String permission) {
        return this.permissions.get(permission);
    }

    public double getSurface() {
        return this.getSurface(this.box);
    }

    public boolean contains(Vector v) {
        return this.box.contains(v);
    }

    public boolean contains(Block b) {
        return this.box.contains(b.getLocation().toVector());
    }

    public boolean contains(Player p) {
        return this.box.contains(p.getLocation().toVector());
    }

    public boolean contains(Location l) {
        return this.box.contains(l.toVector());
    }

    public Vector[] getPoints() {
        return new Vector[]{
            new Vector(this.box.getMinX(), this.box.getMinZ(), this.box.getMinY()), 
            new Vector(this.box.getMaxX(), this.box.getMaxZ(), this.box.getMaxY())
        };
    }

    public boolean isValid() {
        return this.isValid(this.box);
    }

    public void show(Player player) {
        Vector2d p1 = new Vector2d(this.box.getMinX(), this.box.getMinZ());
        Vector2d p2 = new Vector2d(this.box.getMaxX()-1, this.box.getMinZ());
        Vector2d p3 = new Vector2d(this.box.getMaxX()-1, this.box.getMaxZ()-1);
        Vector2d p4 = new Vector2d(this.box.getMinX(), this.box.getMaxZ()-1);

        drawLineX(player, p1, p2, AreaAction.SHOW);
        drawLineY(player, p2, p3, AreaAction.SHOW);
        drawLineX(player, p4, p3, AreaAction.SHOW);
        drawLineY(player, p1, p4, AreaAction.SHOW);

        Location location = new Location(player.getWorld(), p3.getX(), -64, p3.getY());
        ArrayList<Location> possibleBlockLocations = new ArrayList<>();
        
        for(int j = -64; j <= 320; j++) {
            possibleBlockLocations.add(new Location(player.getWorld(), p3.getX(), j, p3.getY()));
        }

        for(Location loc : possibleBlockLocations) {
            if(this.hasAirBlocksAbove(player.getWorld(), loc) && loc.distance(player.getLocation()) < location.distance(player.getLocation())) {
                location = loc;
            }
        }

        player.sendBlockChange(location, Material.GOLD_BLOCK.createBlockData());        
    }

    public void hide(Player player) {
        Vector2d p1 = new Vector2d(this.box.getMinX(), this.box.getMinZ());
        Vector2d p2 = new Vector2d(this.box.getMaxX()-1, this.box.getMinZ());
        Vector2d p3 = new Vector2d(this.box.getMaxX()-1, this.box.getMaxZ()-1);
        Vector2d p4 = new Vector2d(this.box.getMinX(), this.box.getMaxZ()-1);

        drawLineX(player, p1, p2, AreaAction.HIDE);
        drawLineY(player, p2, p3, AreaAction.HIDE);
        drawLineX(player, p4, p3, AreaAction.HIDE);
        drawLineY(player, p1, p4, AreaAction.HIDE);

        Location location = new Location(player.getWorld(), p3.getX(), -64, p3.getY());
        ArrayList<Location> possibleBlockLocations = new ArrayList<>();
        
        for(int j = -64; j <= 320; j++) {
            possibleBlockLocations.add(new Location(player.getWorld(), p3.getX(), j, p3.getY()));
        }

        for(Location loc : possibleBlockLocations) {
            if(this.hasAirBlocksAbove(player.getWorld(), loc) && loc.distance(player.getLocation()) < location.distance(player.getLocation())) {
                location = loc;
            }
        }

        player.sendBlockChange(location, player.getWorld().getBlockData(location));      
    }

    public double expand(Player player, int amount) {
        CardinalDirection direction = this.getDirection(player);
        BoundingBox newBox = this.box.clone().expand(direction.asVector(), amount);
        
        double currentSurface = this.getSurface();
        double newSurface = newBox.getWidthX() * newBox.getWidthZ();

        if(!this.isValid(newBox)) {
            return -1;
        }

        for(Area area : AreaUtil.getAreas()) {
            if(area.overlaps(newBox) && !area.getName().equals(this.getName())) {
                return -1;
            }
        }

        this.hide(player);

        double cost = newSurface - currentSurface;
        this.box.expand(direction.asVector(), amount);
        
        this.show(player);

        try {
            AreaUtil.saveArea(this);
        } catch (SQLException e) {
            AreaEssentials.getPlugin().getLogger().severe("Couldn't save area : " + e.getMessage());
        }
        return cost;
    }

    public double shrink(Player player, int amount) {
        double cost = this.expand(player, amount*-1);

        if(cost == -1) {
            return cost;
        }
        return cost*-1;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setEnterMessage(String message) {
        this.enterMessage = message;
    }

    public void setLeaveMessage(String message) {
        this.leaveMessage = message;
    }

    public void setPermission(String permission, boolean value) {
        if(this.permissions.keySet().contains(permission)) {
            this.permissions.put(permission, value);
        }
    }

    public void draw() {
        int[] borderColor = new int[]{this.color.getRed(), this.color.getGreen(), this.color.getBlue()};
        int[] fillcolor = new int[3];
        for(int k = 0; k < borderColor.length; k++) {
            if(borderColor[k] > 55) {
                fillcolor[k] = borderColor[k] - 55;
            } else {
                fillcolor[k] = 0;
            }
        }

        ExtrudeMarker marker = ExtrudeMarker.builder()
            .label(this.getName())
            .shape(new Shape(this.getAnchorPoints()), -64, 320)
            .lineColor(new de.bluecolored.bluemap.api.math.Color(borderColor[0], borderColor[1], borderColor[2], 1.0f))
            .fillColor(new de.bluecolored.bluemap.api.math.Color(fillcolor[0], fillcolor[1], fillcolor[2], 0.3f))
            .minDistance(0.0)
            .build();
                
        AreaEssentials.blueMap.getWorld("world").ifPresent(world -> {
            for(BlueMapMap map : world.getMaps()) {
                
                if(map.getMarkerSets().get("Areas") != null) {
                    map.getMarkerSets().get("Areas").put(this.getName(), marker);

                } else {
                    MarkerSet markerSet = MarkerSet.builder().label("Areas").build();
                    map.getMarkerSets().put("Areas", markerSet);
                    map.getMarkerSets().get("Areas").put(this.getName(), marker);

                }
            }
        });
    }

    public void erase() {
        AreaEssentials.blueMap.getWorld("world").ifPresent(world -> {
            for(BlueMapMap map : world.getMaps()) {         
                map.getMarkerSets().get("Areas").remove(this.getName());
            }
        });
    }

    public boolean overlaps(BoundingBox box) {
        return this.box.overlaps(box);
    }

    public boolean overlaps(Area area) {
        return this.box.overlaps(area.box);
    }
}