package net.philocraft.utils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.awt.Color;

import org.bukkit.entity.Player;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

import net.philocraft.AreaEssentials;
import net.philocraft.models.Area;

public class DatabaseUtil {
    
    private static final HashMap<Player, Boolean> claims = new HashMap<>();
    private static final HashMap<UUID, Area> potentialAreas = new HashMap<>();
    private static final HashMap<UUID, ArrayList<Area>> areas = new HashMap<>();

    public static void setClaimModeOn(Player player) {
        claims.put(player, true);
    }

    public static void setClaimModeOff(Player player) {
        claims.put(player, false);
    }

    public static void toggleClaimMode(Player player) {
        
        if(claims.containsKey(player)) {
            claims.put(player, !claims.get(player));
        } else {
            DatabaseUtil.setClaimModeOn(player);
        }
    }

    public static boolean getClaimMode(Player player) {
        return (claims.containsKey(player) && claims.get(player));
    }

    public static String getFormatedClaimMode(Player player) {
        
        if(DatabaseUtil.getClaimMode(player)) {
            return "on";
        }
        return "off";
    }

    public static void loadAreas() throws SQLException {
        AreaEssentials.api.database.create(
            "CREATE TABLE IF NOT EXISTS Areas(" +
            "id int NOT NULL UNIQUE AUTO_INCREMENT, " +
            "uuid TEXT NOT NULL, " + 
            "name TEXT NOT NULL, " +
            "color TEXT NOT NULL, " +
            "enterMessage TEXT, " + 
            "leaveMessage TEXT, " +
            "groupName TEXT NOT NULL, " +
            "p1 TEXT NOT NULL, " +
            "p2 TEXT NOT NULL, " +
            "mobGriefing BOOLEAN DEFAULT 0, " +
            "doPVP BOOLEAN DEFAULT 0);"
        );

        ResultSet results = AreaEssentials.api.database.fetch("SELECT * FROM Areas;");

        while(results.next()) {
            Area area = new Area(
                results.getString("name"),
                results.getString("enterMessage"),
                results.getString("leaveMessage"),
                results.getString("groupName"),
                results.getDate("creation").getTime(),
                Color.decode(results.getString("color")),
                UUID.fromString(results.getString("uuid")),
                
                BoundingBox.of(
                    new Vector(Double.parseDouble(results.getString("p1").split("@")[0]), -64, Double.parseDouble(results.getString("p1").split("@")[1])),
                    new Vector(Double.parseDouble(results.getString("p2").split("@")[0]), 320, Double.parseDouble(results.getString("p2").split("@")[1]))
                ),
                
                results.getBoolean("mobGriefing"),
                results.getBoolean("doPVP")
            );

            ArrayList<Area> playerAreas = DatabaseUtil.areas.get(UUID.fromString(results.getString("uuid")));
            if(playerAreas == null) {
                playerAreas = new ArrayList<>();
            }

            playerAreas.add(area);
            DatabaseUtil.areas.put(UUID.fromString(results.getString("uuid")), playerAreas);
        }
    }

    public static ArrayList<Area> getAreas() {
        ArrayList<Area> allAreas = new ArrayList<>();

        for(UUID uuid : DatabaseUtil.areas.keySet()) {
            for(Area playerArea : DatabaseUtil.areas.get(uuid)) {
                allAreas.add(playerArea);
            }
        }

        return allAreas;
    }

    public static ArrayList<Area> getAreas(Player player) {
        ArrayList<Area> playerAreas = DatabaseUtil.areas.get(player.getUniqueId());

        if(playerAreas == null) {
            DatabaseUtil.areas.put(player.getUniqueId(), new ArrayList<>());
            playerAreas = new ArrayList<>();
        }

        return playerAreas;
    }

    public static Area getArea(Player player, String name) {
        ArrayList<Area> playerAreas = DatabaseUtil.areas.get(player.getUniqueId());

        if(playerAreas == null) {
            DatabaseUtil.areas.put(player.getUniqueId(), new ArrayList<>());
            return null;
        }

        for(Area area : playerAreas) {
            if(area.getName().equals(name)) {
                return area;
            }
        }
        
        return null;
    }

    public static void addPotentialArea(UUID uuid, Area area) {
        DatabaseUtil.potentialAreas.put(uuid, area);
    }

    public static void addPotentialArea(Player player, Area area) {
        DatabaseUtil.potentialAreas.put(player.getUniqueId(), area);
    }

    public static Area getPotentialArea(UUID uuid) {
        return DatabaseUtil.potentialAreas.get(uuid);
    }

    public static Area getPotentialArea(Player player) {
        return DatabaseUtil.potentialAreas.get(player.getUniqueId());
    }

    public static void createArea(Area area) throws SQLException{
        DatabaseUtil.potentialAreas.put(area.getUUID(), null);
        
        ArrayList<Area> playerAreas = DatabaseUtil.areas.get(area.getUUID());
        if(playerAreas == null) {
            playerAreas = new ArrayList<>();
        }

        playerAreas.add(area);
        DatabaseUtil.areas.put(area.getUUID(), playerAreas);

        Color areaColor = area.getColor();

        AreaEssentials.api.database.create("INSERT INTO Areas(" +
                "uuid, " +
                "name, " +
                "color, " +
                "enterMessage, " +
                "leaveMessage, " +
                "groupName, " +
                "p1, " +
                "p2, " +
                "mobGriefing, " +
                "doPVP" +
            ") VALUES('" +
                area.getUUID() + "', '" +
                area.getName() + "', '" + 
                String.format("#%02x%02x%02x", areaColor.getRed(), areaColor.getGreen(), areaColor.getBlue()) + "', " +
                area.getEnterMessage() + ", " +
                area.getLeaveMessage() + ", '" +
                area.getGroupName() + "', '" +
                area.getPoints()[0].getX() + "@" + area.getPoints()[0].getY() + "', '" +
                area.getPoints()[1].getX() + "@" + area.getPoints()[1].getY() + "', " +
                area.getPermission("mobGriefing") + ", " +
                area.getPermission("doPVP") + "" +
            ");"
        );
    }

}
