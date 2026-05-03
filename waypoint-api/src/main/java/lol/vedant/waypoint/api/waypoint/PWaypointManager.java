package lol.vedant.waypoint.api.waypoint;

import org.bukkit.entity.Player;

public interface PWaypointManager {

    void load();

    void createWaypoint(Player player, String id);

    void deleteWaypoint(String id);

    void startWaypoint(Player player, PWaypoint waypoint);

    void updateWaypoint(Player player, PWaypoint waypoint);

    void stopWaypoint(Player player);

    boolean hasActiveWaypoint(Player player);

    PWaypoint getActiveWaypoint(Player player);



}
