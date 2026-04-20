package lol.vedant.waypoint.api.database;

import lol.vedant.waypoint.api.waypoint.PWaypoint;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public interface Database {

    void init();

    PWaypoint getPlayerWaypoint(UUID player, String identifier);

    List<PWaypoint> getAllPlayerWaypoints(UUID player);

    void createPlayerWaypoint(UUID player, PWaypoint waypoint);

    void deletePlayerWaypoint(UUID player, String identifier);

    PWaypoint getWaypoint(String identifier);

    void createWaypoint(PWaypoint waypoint);

    void deleteWaypoint(String identifier);

}
