package lol.vedant.waypoint.api;

import lol.vedant.waypoint.api.database.Database;
import org.bukkit.configuration.file.YamlConfiguration;

public interface WaypointAPI {

    YamlConfiguration getConfiguration();

    Database getDatabase();



}
