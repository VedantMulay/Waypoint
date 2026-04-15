package lol.vedant.waypoint;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import lol.vedant.waypoint.api.WaypointAPI;
import lol.vedant.waypoint.api.database.Database;
import lol.vedant.waypoint.api.hologram.HologramManager;
import lol.vedant.waypoint.api.waypoint.PWaypointManager;
import lol.vedant.waypoint.command.WaypointCommand;
import lol.vedant.waypoint.waypoint.WaypointManager;
import me.despical.commandframework.CommandFramework;
import org.bstats.bukkit.Metrics;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class Waypoint extends JavaPlugin implements WaypointAPI {

    static Waypoint instance;
    private final int pluginId = 30764; //bStats plugin id

    private ProtocolManager protocolManager ;
    private HologramManager hologramManager;
    private PWaypointManager waypointManager;
    private CommandFramework commandFramework;


    @Override
    public void onEnable() {
        instance = this;
        //Metrics metrics = new Metrics(this, pluginId);

        protocolManager = ProtocolLibrary.getProtocolManager();
        hologramManager = new HologramManager(protocolManager);
        waypointManager = new WaypointManager();
        commandFramework = new CommandFramework(this);

        commandFramework.registerCommands(new WaypointCommand());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @Override
    public YamlConfiguration getConfiguration() {
        return null;
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
