package lol.vedant.waypoint.listener;

import lol.vedant.waypoint.Waypoint;
import lol.vedant.waypoint.api.event.PlayerWaypointReachEvent;
import lol.vedant.waypoint.api.event.PlayerWaypointStartEvent;
import lol.vedant.waypoint.api.waypoint.PWaypoint;
import lol.vedant.waypoint.util.Util;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class WaypointListener implements Listener {

    Waypoint plugin = Waypoint.getInstance();

    @EventHandler
    public void onWaypointStart(PlayerWaypointStartEvent e) {
        Player player = e.getPlayer();
        PWaypoint waypoint = e.getWaypoint();

        player.sendMessage(Util.cc(
                "&aYou have started waypoint at location: &f" + Util.fromLocation(waypoint.getLocation())
        ));
    }

    @EventHandler
    public void onPlayerWaypointReach(PlayerWaypointReachEvent e) {
        Player player = e.getPlayer();

        player.sendMessage("You have reached your waypoint!");
        plugin.getWaypointManager().stopWaypoint(player);

    }

}
