package net.philocraft.utils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;
import java.awt.Color;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

import net.philocraft.AreaEssentials;
import net.philocraft.models.Area;

public class AreaUtil {
    
    private static final HashMap<UUID, Area> potentialAreas = new HashMap<>();
    private static final HashMap<UUID, ArrayList<Area>> areas = new HashMap<>();

    public static void loadAreas() throws SQLException, NumberFormatException, ParseException {
        AreaEssentials.api.database.create(
            "CREATE TABLE IF NOT EXISTS Areas(" +
            "id int NOT NULL UNIQUE AUTO_INCREMENT, " +
            "uuid TEXT NOT NULL, " + 
            "name TEXT NOT NULL, " +
            "color TEXT NOT NULL, " +
            "creation DATETIME DEFAULT(CURRENT_TIMESTAMP)," +
            "enterMessage TEXT, " + 
            "leaveMessage TEXT, " +
            "groupName TEXT NOT NULL, " +
            "p1 TEXT NOT NULL, " +
            "p2 TEXT NOT NULL, " +
            "mobGriefing BOOLEAN DEFAULT 0, " +
            "doPVP BOOLEAN DEFAULT 0, " +
            "doBuilding BOOLEAN DEFAULT 1, " +
            "doInteracting BOOLEAN DEFAULT 1);"
        );

        ResultSet results = AreaEssentials.api.database.fetch("SELECT * FROM Areas;");

        while(results.next()) {
            Area area = new Area(
                results.getString("name"),
                results.getString("enterMessage"),
                results.getString("leaveMessage"),
                results.getString("groupName"),
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(results.getString("creation")).getTime(),
                Color.decode(results.getString("color")),
                UUID.fromString(results.getString("uuid")),
                
                BoundingBox.of(
                    new Vector(Double.parseDouble(results.getString("p1").split("@")[0]), -64, Double.parseDouble(results.getString("p1").split("@")[1])),
                    new Vector(Double.parseDouble(results.getString("p2").split("@")[0]), 320, Double.parseDouble(results.getString("p2").split("@")[1]))
                ),
                
                results.getBoolean("mobGriefing"),
                results.getBoolean("doPVP"),
                results.getBoolean("doBuilding"),
                results.getBoolean("doInteracting")
            );

            ArrayList<Area> playerAreas = AreaUtil.areas.get(UUID.fromString(results.getString("uuid")));
            if(playerAreas == null) {
                playerAreas = new ArrayList<>();
            }

            playerAreas.add(area);
            AreaUtil.areas.put(UUID.fromString(results.getString("uuid")), playerAreas);

            area.draw();
        }
    }

    public static ArrayList<Area> getAreas() {
        ArrayList<Area> allAreas = new ArrayList<>();

        for(UUID uuid : AreaUtil.areas.keySet()) {
            for(Area playerArea : AreaUtil.areas.get(uuid)) {
                allAreas.add(playerArea);
            }
        }

        return allAreas;
    }

    public static ArrayList<Area> getAreas(Player player) {
        ArrayList<Area> playerAreas = AreaUtil.areas.get(player.getUniqueId());

        if(playerAreas == null) {
            AreaUtil.areas.put(player.getUniqueId(), new ArrayList<>());
            playerAreas = new ArrayList<>();
        }

        return playerAreas;
    }

    public static Area getArea(Player player, String name) {
        ArrayList<Area> playerAreas = AreaUtil.areas.get(player.getUniqueId());

        if(playerAreas == null) {
            AreaUtil.areas.put(player.getUniqueId(), new ArrayList<>());
            return null;
        }

        for(Area area : playerAreas) {
            if(area.getName().equals(name)) {
                return area;
            }
        }
        
        return null;
    }

    public static Area getArea(Location location) {
        for(Area area : AreaUtil.getAreas()) {
            if(area.contains(location)) {
                return area;
            }
        }

        return null;
    }

    public static void addPotentialArea(UUID uuid, Area area) {
        AreaUtil.potentialAreas.put(uuid, area);
    }

    public static void addPotentialArea(Player player, Area area) {
        AreaUtil.potentialAreas.put(player.getUniqueId(), area);
    }

    public static Area getPotentialArea(UUID uuid) {
        return AreaUtil.potentialAreas.get(uuid);
    }

    public static Area getPotentialArea(Player player) {
        return AreaUtil.potentialAreas.get(player.getUniqueId());
    }

    public static void createArea(Area area) throws SQLException{
        AreaUtil.potentialAreas.put(area.getUUID(), null);
        
        ArrayList<Area> playerAreas = AreaUtil.areas.get(area.getUUID());
        if(playerAreas == null) {
            playerAreas = new ArrayList<>();
        }

        playerAreas.add(area);
        AreaUtil.areas.put(area.getUUID(), playerAreas);

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
                "doPVP," +
                "doBuilding," +
                "doInteracting," +
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
                area.getPermission("doPVP") + ", " +
                area.getPermission("doBuilding") + ", " +
                area.getPermission("doInteracting") + "" +
            ");"
        );
    }

    public static void saveArea(Area area, String name) throws SQLException {
        Color areaColor = area.getColor();

        String enterMessage = null;
        if(area.getEnterMessage() != null) {
            enterMessage = "'" + area.getEnterMessage() + "'";
        }

        String leaveMessage = null;
        if(area.getLeaveMessage() != null) {
            leaveMessage = "'" + area.getLeaveMessage() + "'";
        }

        AreaEssentials.api.database.update(
            "UPDATE Areas SET " +
            "name='" + name + "', " + 
            "color='" + String.format("#%02x%02x%02x", areaColor.getRed(), areaColor.getGreen(), areaColor.getBlue()) + "', " +
            "enterMessage=" + enterMessage + ", " +
            "leaveMessage=" + leaveMessage + ", " +
            "groupName='" + area.getGroupName() + "', " +
            "p1='" + area.getPoints()[0].getX() + "@" + area.getPoints()[0].getY() + "', " +
            "p2='" + area.getPoints()[1].getX() + "@" + area.getPoints()[1].getY() + "', " +
            "mobGriefing=" + area.getPermission("mobGriefing") + ", " +
            "doPVP=" + area.getPermission("doPVP") + ", " +
            "doBuilding=" + area.getPermission("doBuilding") + ", " +
            "doInteracting=" + area.getPermission("doInteracting") + " " +
            "WHERE uuid='" + area.getUUID() + "' " + 
            "AND name='" + area.getName() + "';"
        );
    }

    public static void saveArea(Area area) throws SQLException {
        AreaUtil.saveArea(area, area.getName());
    }

    public static void removeArea(Area area) throws SQLException {
        
        ArrayList<Area> playerAreas = AreaUtil.areas.get(area.getUUID());
        for(int i = 0; i < playerAreas.size(); i++) {
            if(playerAreas.get(i).getName().equals(area.getName())) {
                playerAreas.remove(i);
            }
        }

        AreaUtil.areas.put(area.getUUID(), playerAreas);
        AreaEssentials.api.database.update("DELETE FROM Areas WHERE uuid='" + area.getUUID() + "' AND name='" + area.getName() + "';");
    }

    public static ArrayList<String> getPermissionKeys() {
        return new ArrayList<>(Arrays.asList("mobGriefing", "doPVP", "doBuilding", "doInteracting"));
    }

    public static void loadTrusted() throws SQLException {
        AreaEssentials.api.database.create(
            "CREATE TABLE IF NOT EXISTS Trusted(" +
            "id int NOT NULL UNIQUE AUTO_INCREMENT, " +
            "uuid TEXT NOT NULL, " + 
            "name TEXT NOT NULL, " +
            "trusted TEXT NOT NULL, " +
            "permission TEXT NOT NULL, " +
            "value BOOLEAN NOT NULL);"
        );

        ResultSet results = AreaEssentials.api.database.fetch("SELECT * FROM Trusted;");

        while(results.next()) {
            UUID owner = UUID.fromString(results.getString("uuid"));
            String name = results.getString("name");
            UUID trusted = UUID.fromString(results.getString("trusted"));
            String permission = results.getString("permission");
            Boolean value = results.getBoolean("value");

            for(Area area : AreaUtil.getAreas()) {
                if(area.getUUID().equals(owner) && area.getName().equals(name)) {
                    area.setPlayerPermission(permission, trusted, value);
                }
            }
        }
    }

    public static void trustPlayer(Area area, String permission, UUID trusted, boolean value) throws SQLException {
        if(area.getPlayerPermission(permission, trusted) != null) {
            AreaEssentials.api.database.update(
                "UPDATE Trusted SET value=" + value + " " + 
                "WHERE uuid='" + area.getUUID() + "' AND name='" + area.getName() + "' AND trusted='" + trusted + "' AND permission='" + permission + "';"
            );
        
        } else {
            AreaEssentials.api.database.create(
                "INSERT INTO Trusted(uuid, name, trusted, permission, value) " +
                "VALUES('" + area.getUUID() + "', '" + area.getName() + "', '" + trusted + "', '" + permission + "', " + value + ");"
            );
        }
        
        area.setPlayerPermission(permission, trusted, value);
    }

    public static void distrustPlayer(Area area, String permission, UUID trusted) throws SQLException {
        AreaEssentials.api.database.update("DELETE FROM Trusted WHERE uuid='" + area.getUUID() + "' AND name='" + area.getName() + "' AND permission='" + permission + "' AND trusted='" + trusted + "';");
        area.removePlayerPermission(permission, trusted);
    }
}
