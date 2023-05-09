package net.philocraft;

import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import de.bluecolored.bluemap.api.BlueMapAPI;
import dev.littlebigowl.api.EssentialsAPI;
import net.philocraft.commands.AreaCommandManager;
import net.philocraft.commands.ClaimCommand;
import net.philocraft.events.OnPlayerInteractEvent;
import net.philocraft.utils.DatabaseUtil;

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
                DatabaseUtil.loadAreas();
                this.getLogger().info("Loaded " + DatabaseUtil.getAreas().size() + " areas.");
            } catch (SQLException e) {
                this.getLogger().severe("Couldn't load areas : " + e.getMessage());
            }
        });

        plugin = this;

        //!REGISTER EVENTS
        this.getServer().getPluginManager().registerEvents(new OnPlayerInteractEvent(), this);

        //!REGISTER COMMANDS
        this.getCommand("claim").setExecutor(new ClaimCommand());
        this.getCommand("area").setExecutor(new AreaCommandManager());

        this.getLogger().info("Plugin enabled.");
    }

    @Override
    public void onDisable() {
        this.getLogger().info("Plugin disabled.");
    }

}