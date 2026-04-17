package lol.vedant.waypoint.api.waypoint;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public interface PWaypoint {

    String getIdentifier();

    Player getPlayer();

    String getPermission();

    Location getLocation();

    String getName();

    void start();

    void stop();


}
