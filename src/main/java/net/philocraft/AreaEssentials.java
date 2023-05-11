package net.philocraft;

import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import de.bluecolored.bluemap.api.BlueMapAPI;
import dev.littlebigowl.api.EssentialsAPI;
import net.philocraft.commands.AreaCommand;
import net.philocraft.commands.ClaimCommand;
import net.philocraft.events.OnEntityChangeBlockEvent;
import net.philocraft.events.OnEntityDamageEvent;
import net.philocraft.events.OnEntityExplodeEvent;
import net.philocraft.events.OnPlayerInteractEvent;
import net.philocraft.events.OnPlayerMoveEvent;
import net.philocraft.utils.ClaimUtil;
import net.philocraft.utils.AreaUtil;

public class AreaEssentials extends JavaPlugin {

    public static final EssentialsAPI api = (EssentialsAPI)Bukkit.getServer().getPluginManager().getPlugin("EssentialsAPI");
    public static BlueMapAPI blueMap;

    private static AreaEssentials plugin;

    public static AreaEssentials getPlugin() {
        return plugin;
    }

    @Override
    public void onEnable() {
        BlueMapAPI.onEnable(bluemap -> {
            blueMap = bluemap;
            this.getLogger().info("Loaded BlueMapAPI.");

            try {
                AreaUtil.loadAreas();
                this.getLogger().info("Loaded " + AreaUtil.getAreas().size() + " areas.");
            } catch (SQLException e) {
                this.getLogger().severe("Couldn't load areas : " + e.getMessage());
            }
        });

        plugin = this;

        try {
            ClaimUtil.loadClaims();
            this.getLogger().info("Loaded claims.");
        } catch (SQLException e) {
            this.getLogger().severe("Couldn't load claims : " + e.getMessage());
        }

        ClaimUtil.checkPlaytimeBlocks();

        //!REGISTER EVENTS
        this.getServer().getPluginManager().registerEvents(new OnEntityChangeBlockEvent(), this);
        this.getServer().getPluginManager().registerEvents(new OnEntityDamageEvent(), this);
        this.getServer().getPluginManager().registerEvents(new OnEntityExplodeEvent(), this);
        this.getServer().getPluginManager().registerEvents(new OnPlayerInteractEvent(), this);
        this.getServer().getPluginManager().registerEvents(new OnPlayerMoveEvent(), this);

        //!REGISTER COMMANDS
        this.getCommand("claim").setExecutor(new ClaimCommand());
        this.getCommand("area").setExecutor(new AreaCommand());

        this.getLogger().info("Plugin enabled.");
    }

    @Override
    public void onDisable() {
        this.getLogger().info("Plugin disabled.");
    }

}