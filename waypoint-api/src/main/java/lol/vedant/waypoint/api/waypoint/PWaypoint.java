package lol.vedant.waypoint.api.waypoint;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public interface PWaypoint {

    Player getPlayer();

    Location getLocation();

    String getName();

    void start();

    void stop();


}
