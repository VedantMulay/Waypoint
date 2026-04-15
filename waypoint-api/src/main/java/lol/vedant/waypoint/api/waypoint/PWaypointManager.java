package lol.vedant.waypoint.api.waypoint;

import org.bukkit.entity.Player;

public interface PWaypointManager {

    void startWaypoint(Player player, PWaypoint waypoint);

    void stopWaypoint(Player player);



}
