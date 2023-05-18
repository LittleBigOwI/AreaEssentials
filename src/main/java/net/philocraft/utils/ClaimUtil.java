package net.philocraft.utils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import dev.littlebigowl.api.constants.Colors;
import dev.littlebigowl.api.models.EssentialsScoreboard;
import net.philocraft.AreaEssentials;

public class ClaimUtil {
    
    private static HashMap<UUID, Integer> playerBlocks = new HashMap<>();
    private static HashMap<Player, Integer> playtimeBlocks = new HashMap<>();
    private static HashMap<Player, String> playtimeTeamBlocks = new HashMap<>();
    private static HashMap<Player, Boolean> wantsToBuy = new HashMap<>();

    private static final HashMap<Player, Boolean> claims = new HashMap<>();
    private static final int claimAmount = AreaEssentials.api.areas.getPassiveClaimAmount();
    private static final int claimInterval = AreaEssentials.api.areas.getPassiveClaimInterval();

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
            ClaimUtil.setClaimModeOn(player);
        }
    }

    public static boolean getClaimMode(Player player) {
        return (claims.containsKey(player) && claims.get(player));
    }

    public static String getFormatedClaimMode(Player player) {
        
        if(ClaimUtil.getClaimMode(player)) {
            return "on";
        }
        return "off";
    }

    public static void loadClaims() throws SQLException {
        AreaEssentials.api.database.create(
            "CREATE TABLE IF NOT EXISTS Claims(" +
            "id int NOT NULL UNIQUE AUTO_INCREMENT, " +
            "uuid TEXT NOT NULL, " +
            "claims int NOT NULL);"
        );

        ResultSet results = AreaEssentials.api.database.fetch("SELECT * FROM Claims;");

        while(results.next()) {
            ClaimUtil.playerBlocks.put(UUID.fromString(results.getString("uuid")), results.getInt("claims"));
        }
    }

    public static void checkPlaytimeBlocks() {

        AreaEssentials.getPlugin().getServer().getScheduler().runTaskTimer(AreaEssentials.getPlugin(), () -> {
            for(Player player : Bukkit.getOnlinePlayers()) {
                
                String team = AreaEssentials.api.scoreboard.getEssentialsTeam(player).getName();
                int bonus = AreaEssentials.api.scoreboard.getEssentialsTeam(player).getClaimBonus();
                int playtime = EssentialsScoreboard.getPlaytime(player);
                
                if(playerBlocks.get(player.getUniqueId()) == null) {
                    ClaimUtil.addClaimBlocks(player, bonus);
                }

                if(playtimeBlocks.get(player) == null) {
                    playtimeBlocks.put(player, playtime);
                }

                if(playtimeTeamBlocks.get(player) == null) {
                    playtimeTeamBlocks.put(player, team);
                }

                if(playtime%claimInterval == 0 && playtimeBlocks.get(player) != playtime) {
                    ClaimUtil.addClaimBlocks(player, claimAmount);

                    player.playSound(player.getEyeLocation(), Sound.ENTITY_PLAYER_LEVELUP, 2f, 2f);
                    player.sendMessage(
                        Colors.WARNING.getChatColor() + "You earned " + 
                        Colors.POPUP.getChatColor() + claimAmount + 
                        Colors.WARNING.getChatColor() + " claim blocks for playing " + 
                        Colors.POPUP.getChatColor() + claimInterval + 
                        Colors.WARNING.getChatColor() + " minutes!"
                    );
                }

                if(!playtimeTeamBlocks.get(player).equals(AreaEssentials.api.scoreboard.getEssentialsTeam(player).getName()) && bonus != 0) {                    
                    ClaimUtil.addClaimBlocks(player, bonus);

                    player.playSound(player.getEyeLocation(), Sound.ENTITY_PLAYER_LEVELUP, 2f, 2f);
                    player.sendMessage(
                        Colors.WARNING.getChatColor() + "You earned " + 
                        Colors.POPUP.getChatColor() + bonus + 
                        Colors.WARNING.getChatColor() + " claim blocks for moving up to the next rank!"
                    );
                }

                ClaimUtil.playtimeBlocks.put(player, playtime);
                ClaimUtil.playtimeTeamBlocks.put(player, team);
            }
        }, 0, 20);

    }

    public static void addClaimBlocks(Player player, int amount) {
        Integer playerClaimBlocks = playerBlocks.get(player.getUniqueId());
        
        try {
            if(playerClaimBlocks != null) {
                AreaEssentials.api.database.update("UPDATE Claims SET claims=" + (playerClaimBlocks + amount) + " WHERE uuid='" + player.getUniqueId() + "';");
                playerBlocks.put(player.getUniqueId(), playerClaimBlocks + amount);
    
            } else {
                AreaEssentials.api.database.create("INSERT INTO Claims(uuid, claims) VALUES('" + player.getUniqueId() + "', " + amount + ");");
                playerBlocks.put(player.getUniqueId(), amount);
    
            }
        } catch (SQLException e) {
            AreaEssentials.getPlugin().getLogger().severe("Couldn't update claims : " + e.getMessage());
        }
    }

    public static int getClaimBlocks(Player player) {
        Integer playerClaimBlocks = playerBlocks.get(player.getUniqueId());

        if(playerClaimBlocks == null) {
            return 0;
        }
        return playerClaimBlocks;
    }

    public static void setClaimBlocks(UUID uuid, int amount) {
        Integer playerClaimBlocks = playerBlocks.get(uuid);
        
        try {
            if(playerClaimBlocks != null) {
                AreaEssentials.api.database.update("UPDATE Claims SET claims=" + amount + " WHERE uuid='" + uuid + "';");
                playerBlocks.put(uuid, amount);
    
            } else {
                AreaEssentials.api.database.create("INSERT INTO Claims(uuid, claims) VALUES('" + uuid + "', " + amount + ");");
                playerBlocks.put(uuid, amount);
    
            }
        } catch (SQLException e) {
            AreaEssentials.getPlugin().getLogger().severe("Couldn't update claims : " + e.getMessage());
        }
    }

    public static void setBuyModeOn(Player player) {
        ClaimUtil.wantsToBuy.put(player, true);
    }

    public static void setBuyModeOff(Player player) {
        ClaimUtil.wantsToBuy.put(player, false);
    }

    public static boolean getBuyMode(Player player) {
        Boolean wantsToBuy = ClaimUtil.wantsToBuy.get(player);

        if(wantsToBuy == null) {
            ClaimUtil.setBuyModeOff(player);
            return false;
        }
        return wantsToBuy;
    }

}
