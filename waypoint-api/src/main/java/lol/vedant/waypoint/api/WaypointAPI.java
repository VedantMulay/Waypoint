package lol.vedant.waypoint.api;

import lol.vedant.waypoint.api.database.Database;
import lol.vedant.waypoint.api.waypoint.PWaypointManager;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public interface WaypointAPI {

    FileConfiguration getConfiguration();

    Database getDatabase();

    PWaypointManager getWaypointManager();

}
