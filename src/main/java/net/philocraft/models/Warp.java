package net.philocraft.models;

import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.Player;

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
}