package net.philocraft.models;

import java.awt.Color;
import java.time.Instant;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.block.BlockFace;

import com.flowpowered.math.vector.Vector2d;

public class Area {
    
    private String name;
    private String enterMessage;
    private String leaveMessage;
    private String groupName;

    private Color color;
    private long creationDate;
    private UUID uuid;

    private Vector2d p1;
    private Vector2d p2;
    private Vector2d p3;
    private Vector2d p4;
    
    private HashMap<String, Boolean> permissions = new HashMap<>();

    public Area(String name, String enterMessage, String leaveMessage, String groupName, Color color, UUID uuid, Vector2d p1, Vector2d p2, Vector2d p3, Vector2d p4, boolean mobGriefing, boolean doPVP) {
        this.name = name;
        this.enterMessage = enterMessage;
        this.leaveMessage = leaveMessage;
        this.groupName = groupName;

        this.color = color;
        this.creationDate = Instant.now().getEpochSecond();
        this.uuid = uuid;

        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;
        this.p4 = p4;

        this.permissions.put("mobGriefing", mobGriefing);
        this.permissions.put("doPVP", doPVP);
    }

    public Area(String name, Color color, UUID uuid, Vector2d p1, Vector2d p2) {
        this(name, null, null, null, color, uuid, p1, new Vector2d(p2.getX(), p1.getY()), p2, new Vector2d(p1.getX(), p2.getX()), false, false);
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

    public Vector2d[] getVector2ds() {
        return new Vector2d[]{this.p1, this.p2, this.p3, this.p4};
    }

    public boolean getPermissions(String permission) {
        return this.permissions.get(permission);
    }

    public double getSurface() {
        double bottom = Math.sqrt(Math.pow((p2.getX() - p1.getX()), 2) + Math.pow((p2.getY() - p1.getY()), 2));
        double side = Math.sqrt(Math.pow((p3.getX() - p2.getX()), 2) + Math.pow((p3.getY() - p2.getY()), 2));

        return bottom*side;
    }

    public double getExpantionCost(BlockFace blockface, double distance) {
        Area area;
        area = this;

        if(blockface == BlockFace.SOUTH) {
            area = new Area(
                this.name, 
                this.color,
                this.uuid,
                new Vector2d(this.p1.getX(), this.p1.getY()-distance), 
                new Vector2d(p2)
            );
        
        } else if(blockface == BlockFace.EAST) {
            area = new Area(
                this.name, 
                this.color, 
                this.uuid,
                new Vector2d(p2), 
                new Vector2d(this.p3.getX()+distance, this.p3.getY())
            );

        } else if(blockface == BlockFace.NORTH) {
            area = new Area(
                this.name, 
                this.color, 
                this.uuid,
                new Vector2d(p4), 
                new Vector2d(this.p3.getX(), this.p3.getY() + distance)
            );

        } else if(blockface == BlockFace.WEST) {
            area = new Area(
                this.name, 
                this.color, 
                this.uuid,
                new Vector2d(p1), 
                new Vector2d(this.p4.getX()-distance, this.p4.getY())
            );

        }

        return area.getSurface();
    }

}