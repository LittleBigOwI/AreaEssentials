package net.philocraft.utils;

import java.util.HashMap;

import org.bukkit.entity.Player;

public class DatabaseUtil {
    
    private static final HashMap<Player, Boolean> claims = new HashMap<>();

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

}
