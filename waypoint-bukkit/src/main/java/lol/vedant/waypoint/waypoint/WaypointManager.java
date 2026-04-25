package lol.vedant.waypoint.waypoint;

import lol.vedant.waypoint.Waypoint;
import lol.vedant.waypoint.api.waypoint.PWaypoint;
import lol.vedant.waypoint.api.waypoint.PWaypointManager;
import lol.vedant.waypoint.util.Util;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class WaypointManager implements PWaypointManager {

    Waypoint plugin = Waypoint.getInstance();
    YamlConfiguration waypointsFile;
    private Map<UUID, PWaypoint> activeWaypoints = new HashMap<>();
    private Map<String, PWaypoint> loadedWaypoints = new HashMap<>();

    public void load() {
        activeWaypoints.clear();

        File waypointsFile = new File(plugin.getDataFolder() + "/waypoints.yml");

        if(!waypointsFile.exists()) {
            try {
                waypointsFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        this.waypointsFile = YamlConfiguration.loadConfiguration(waypointsFile);
        for (String key : this.waypointsFile.getKeys(true)) {
            ConfigurationSection waypoint = this.waypointsFile.getConfigurationSection(key);
            if(waypoint == null) {
                return;
            }
            String id = waypoint.getString("id");
            String name = waypoint.getString("name");
            Location loc = Util.fromString(waypoint.getString("location"));

            PlayerWaypoint pWaypoint = new PlayerWaypoint(id, name, loc);
            loadedWaypoints.put(id, pWaypoint);

            plugin.getLogger().info("Loaded waypoint with ID: " + id);
        }

    }


    @Override
    public void createWaypoint(Player player, String id) {

        if(waypointsFile.contains(id.toLowerCase())) {
            player.sendMessage(ChatColor.RED + "The waypoint with the " + id + " ID already exists!");
            return;
        }
        Location loc = player.getLocation();

        waypointsFile.set(id + "." + "id", id);
        waypointsFile.set(id + "." + "location", Util.fromLocation(loc));


    }

    @Override
    public void deleteWaypoint(String id) {
        if(!waypointsFile.contains(id.toLowerCase())) {
            //ID already exists
            return;
        }

        waypointsFile.set(id, null);
    }

    @Override
    public void startWaypoint(Player player, PWaypoint waypoint) {
        waypoint.start();
        activeWaypoints.put(player.getUniqueId(), waypoint);
    }

    @Override
    public void updateWaypoint(Player player, PWaypoint waypoint) {
        if(activeWaypoints.containsKey(player.getUniqueId())) {
            activeWaypoints.put(player.getUniqueId(), waypoint);
        }
    }


    @Override
    public void stopWaypoint(Player player) {
        activeWaypoints.get(player.getUniqueId()).stop();
        activeWaypoints.remove(player.getUniqueId());
    }
}
