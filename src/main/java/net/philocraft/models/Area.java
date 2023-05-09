package net.philocraft.models;

import java.awt.Color;
import java.time.Instant;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

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
        this(name, null, null, uuid.toString(), Instant.now().getEpochSecond(), color, uuid, box, false, false);
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

    public boolean getPermissions(String permission) {
        return this.permissions.get(permission);
    }

    public double getSurface() {
        double x1 = this.box.getMinX();
        double y1 = this.box.getMinY();

        double x3 = this.box.getMaxX();
        double y3 = this.box.getMaxY();

        double x2 = x3;
        double y2 = y1;

        double width = Math.sqrt(Math.pow((x2 - x1), 2) + Math.pow((y2 - y1), 2));
        double height = Math.sqrt(Math.pow((x3 - x2), 2) + Math.pow((y3 - y2), 2));

        return width*height;
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

    public Vector[] getPoints() {
        return new Vector[]{
            new Vector(this.box.getMinX(), this.box.getMinZ(), this.box.getMinY()), 
            new Vector(this.box.getMaxX(), this.box.getMaxZ(), this.box.getMaxY())
        };
    }

    public boolean isValid() {
        boolean surface = (this.getSurface() > 256);

        double x1 = this.box.getMinX();
        double y1 = this.box.getMinY();

        double x3 = this.box.getMaxX();
        double y3 = this.box.getMaxY();

        double x2 = x3;
        double y2 = y1;

        double width = Math.sqrt(Math.pow((x2 - x1), 2) + Math.pow((y2 - y1), 2));
        double height = Math.sqrt(Math.pow((x3 - x2), 2) + Math.pow((y3 - y2), 2));

        boolean sides = (width > 15 && height > 15);

        return (surface && sides);
    }

}