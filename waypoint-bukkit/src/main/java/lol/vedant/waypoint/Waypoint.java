package lol.vedant.waypoint;

import lol.vedant.waypoint.api.WaypointAPI;
import lol.vedant.waypoint.api.database.Database;
import lol.vedant.waypoint.api.database.DatabaseSettings;
import lol.vedant.waypoint.api.hologram.HologramManager;
import lol.vedant.waypoint.api.waypoint.PWaypointManager;
import lol.vedant.waypoint.command.WaypointCommand;
import lol.vedant.waypoint.config.ConfigManager;
import lol.vedant.waypoint.database.MySQL;
import lol.vedant.waypoint.database.SQLite;
import lol.vedant.waypoint.listener.WaypointListener;
import lol.vedant.waypoint.menu.ChatInputManager;
import lol.vedant.waypoint.menu.MenuListener;
import lol.vedant.waypoint.waypoint.WaypointManager;
import me.despical.commandframework.CommandFramework;
import org.bstats.bukkit.Metrics;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class Waypoint extends JavaPlugin implements WaypointAPI {

    static Waypoint instance;
    private final int pluginId = 30764; //bStats plugin id

    private Database database;
    private ConfigManager configManager;
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

        if(getConfiguration().getBoolean("database.enabled")) {
            getLogger().info("Using MySQL Database");
            database = new MySQL(
                    new DatabaseSettings(
                            getConfig().getString("database.host"),
                            getConfig().getString("database.database"),
                            getConfig().getInt("database.port"),
                            getConfig().getString("database.user"),
                            getConfig().getString("database.pass"),
                            getConfig().getBoolean("database.ssl"),
                            getConfig().getBoolean("database.verify-certificate"),
                            getConfig().getInt("database.pool-size"),
                            getConfig().getInt("database.max-lifetime")
                    )
            );
            database.init();
        } else {
            getLogger().info("Using SQLite Database");
            database = new SQLite();
            database.init();
        }

        ChatInputManager.init(this);

        hologramManager = new HologramManager(this);

        waypointManager = new WaypointManager();
        waypointManager.load();

        commandFramework = new CommandFramework(this);
        commandFramework.registerCommands(new WaypointCommand());

        getServer().getPluginManager().registerEvents(new WaypointListener(), this);
        getServer().getPluginManager().registerEvents(new MenuListener(), this);
    }

    @Override
    public void onDisable() {
        getLogger().info("Disabling Waypoint Plugin...");
    }

    @Override
    public FileConfiguration getConfiguration() {
        return configManager.getConfig();
    }

    @Override
    public Database getDatabase() {
        return database;
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
