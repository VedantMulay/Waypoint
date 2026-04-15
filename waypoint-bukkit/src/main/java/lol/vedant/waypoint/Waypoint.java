package lol.vedant.waypoint;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import lol.vedant.waypoint.api.WaypointAPI;
import lol.vedant.waypoint.api.database.Database;
import lol.vedant.waypoint.api.hologram.HologramManager;
import lol.vedant.waypoint.command.WaypointCommand;
import me.despical.commandframework.CommandFramework;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class Waypoint extends JavaPlugin implements WaypointAPI {

    static Waypoint instance;
    private ProtocolManager protocolManager ;
    private HologramManager hologramManager;
    private CommandFramework commandFramework;

    @Override
    public void onEnable() {
        instance = this;

        protocolManager = ProtocolLibrary.getProtocolManager();
        hologramManager = new HologramManager(protocolManager);
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

    public static Waypoint getInstance() {
        return instance;
    }

    public HologramManager getHologramManager() {
        return hologramManager;
    }
}
