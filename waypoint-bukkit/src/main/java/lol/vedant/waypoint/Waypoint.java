package lol.vedant.waypoint;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import lol.vedant.waypoint.api.WaypointAPI;
import lol.vedant.waypoint.api.database.Database;
import lol.vedant.waypoint.api.hologram.HologramManager;
import lol.vedant.waypoint.api.waypoint.PWaypointManager;
import lol.vedant.waypoint.command.WaypointCommand;
import lol.vedant.waypoint.config.ConfigManager;
import lol.vedant.waypoint.menu.ChatInputManager;
import lol.vedant.waypoint.waypoint.WaypointManager;
import me.despical.commandframework.CommandFramework;
import org.bstats.bukkit.Metrics;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class Waypoint extends JavaPlugin implements WaypointAPI {

    static Waypoint instance;
    private final int pluginId = 30764; //bStats plugin id

    private ConfigManager configManager;
    private ProtocolManager protocolManager ;
    private HologramManager hologramManager;
    private PWaypointManager waypointManager;
    private CommandFramework commandFramework;


    @Override
    public void onEnable() {
        instance = this;

        configManager = new ConfigManager(this);

        if(getConfiguration().getBoolean("enable-bstats")) {
            new Metrics(this, pluginId);
        }

        ChatInputManager.init(this);

        protocolManager = ProtocolLibrary.getProtocolManager();
        hologramManager = new HologramManager(protocolManager);

        waypointManager = new WaypointManager();
        waypointManager.load();

        commandFramework = new CommandFramework(this);
        commandFramework.registerCommands(new WaypointCommand());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @Override
    public FileConfiguration getConfiguration() {
        return configManager.getConfig();
    }

    @Override
    public Database getDatabase() {
        return null;
    }

    @Override
    public PWaypointManager getWaypointManager() {
        return waypointManager;
    }

    public static Waypoint getInstance() {
        return instance;
    }

    public HologramManager getHologramManager() {
        return hologramManager;
    }

}
