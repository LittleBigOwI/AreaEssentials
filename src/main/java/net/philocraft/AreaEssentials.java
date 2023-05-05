package net.philocraft;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import de.bluecolored.bluemap.api.BlueMapAPI;
import dev.littlebigowl.api.EssentialsAPI;
import net.philocraft.commands.ClaimCommand;
import net.philocraft.events.OnPlayerInteractEvent;

public class AreaEssentials extends JavaPlugin {

    public static final EssentialsAPI api = (EssentialsAPI)Bukkit.getServer().getPluginManager().getPlugin("EssentialsAPI");
    public static BlueMapAPI blueMap;

    @Override
    public void onEnable() {
        BlueMapAPI.onEnable(bluemap -> {
            blueMap = bluemap;
            this.getLogger().info("Loaded BlueMapAPI.");
        });

        //!REGISTER EVENTS
        this.getServer().getPluginManager().registerEvents(new OnPlayerInteractEvent(), this);

        //!REGISTER COMMANDS
        this.getCommand("claim").setExecutor(new ClaimCommand());

        this.getLogger().info("Plugin enabled.");
    }

    @Override
    public void onDisable() {
        this.getLogger().info("Plugin disabled.");
    }

}