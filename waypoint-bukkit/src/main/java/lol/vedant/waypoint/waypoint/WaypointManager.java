package lol.vedant.waypoint.waypoint;

import lol.vedant.waypoint.api.waypoint.PWaypoint;
import lol.vedant.waypoint.api.waypoint.PWaypointManager;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class WaypointManager implements PWaypointManager {

    private Map<UUID, PWaypoint> activeWaypoints = new HashMap<>();

    @Override
    public void startWaypoint(Player player, PWaypoint waypoint) {
        activeWaypoints.put(player.getUniqueId(), waypoint);
        activeWaypoints.get(player.getUniqueId()).start();


    }

    @Override
    public void stopWaypoint(Player player) {
        activeWaypoints.get(player.getUniqueId()).stop();
        activeWaypoints.remove(player.getUniqueId());
    }
}
