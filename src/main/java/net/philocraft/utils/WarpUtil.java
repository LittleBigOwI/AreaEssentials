package net.philocraft.utils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Location;

import dev.littlebigowl.api.constants.Worlds;
import net.philocraft.AreaEssentials;
import net.philocraft.models.Warp;

public class WarpUtil {

    private static final HashMap<UUID, Warp> warps = new HashMap<>();
    
    public static void loadWarps() throws SQLException {

        AreaEssentials.api.database.create(
            "CREATE TABLE IF NOT EXISTS Warps(" +
            "id int NOT NULL UNIQUE AUTO_INCREMENT, " +
            "uuid TEXT NOT NULL, " +
            "name TEXT NOT NULL, " +
            "x FLOAT NOT NULL, " +
            "y FLOAT NOT NULL, " +
            "z FLOAT NOT NULL, " +
            "yaw FLOAT NOT NULL, " +
            "pitch FLOAT NOT NULL);"
        );

        ResultSet results = AreaEssentials.api.database.fetch("SELECT * FROM Warps;");

        while(results.next()) {
            UUID uuid = UUID.fromString(results.getString("uuid"));
            String name = results.getString("name");
            
            Location location = new Location(
                Worlds.OVERWORLD.getWorld(),
                results.getFloat("x"),
                results.getFloat("y"),
                results.getFloat("z"),
                results.getFloat("yaw"),
                results.getFloat("pitch")
            );

            WarpUtil.warps.put(uuid, new Warp(uuid, name, location));
        }

    }

    public static void saveWarp(Warp warp) throws SQLException {
        if(WarpUtil.warps.get(warp.getUUID()) == null) {
            AreaEssentials.api.database.update(
                "INSERT INTO Warps(uuid, name, x, y, z, yaw, pitch) VALUES('" + 
                warp.getUUID() + "', '" + 
                warp.getName() + "', " +
                warp.getX() + ", " +
                warp.getY() + ", " +
                warp.getZ() + ", " +
                warp.getYaw() + ", " +
                warp.getPitch() + ");"
            );
        
        } else {
            AreaEssentials.api.database.update(
                "UPDATE Warps SET " +  
                "name='" + warp.getName() + "', " +
                "x=" + warp.getX() + ", " +
                "y=" + warp.getY() + ", " +
                "z=" + warp.getZ() + ", " +
                "yaw=" + warp.getYaw() + ", " +
                "pitch=" + warp.getPitch() + " " +
                "WHERE uuid='" + warp.getUUID() + "';"
            );

        }

        WarpUtil.warps.put(warp.getUUID(), warp);
    }

    public static void removeWarp(Warp warp) throws SQLException {
        WarpUtil.warps.remove(warp.getUUID());

        AreaEssentials.api.database.update(
            "DELETE FROM Warps WHERE " +
            "uuid='" + warp.getUUID() + "';"
        );
    }

    public static Warp getWarp(UUID uuid) {
        return WarpUtil.warps.get(uuid);
    }

    public static Warp getWarp(String warpName) {
        for(Warp warp : WarpUtil.warps.values()) {
            if(warp.getName().equals(warpName)) {
                return warp;
            }
        }

        return null;
    }

    public static ArrayList<String> getWarpNames() {
        ArrayList<String> warpNames = new ArrayList<>();
        WarpUtil.warps.values().forEach(warp -> warpNames.add(warp.getName()));

        return warpNames;
    }

}
