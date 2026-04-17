package lol.vedant.waypoint.config;

import lol.vedant.waypoint.Waypoint;
import lol.vedant.waypoint.api.config.Config;
import org.bukkit.configuration.file.FileConfiguration;

public class ConfigManager {

    public Config config;

    public ConfigManager(Waypoint plugin) {
        config = new Config(plugin, "config.yml", true);
    }

    public FileConfiguration getConfig() {
        return config.getConfig();
    }
}
