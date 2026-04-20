package lol.vedant.waypoint.listener;

import lol.vedant.waypoint.api.event.PlayerWaypointReachEvent;
import lol.vedant.waypoint.api.event.PlayerWaypointStartEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class WaypointListener implements Listener {

    @EventHandler
    public void onWaypointStart(PlayerWaypointStartEvent e) {
        //TODO: Start waypoint event
    }

    @EventHandler
    public void onPlayerWaypointReach(PlayerWaypointReachEvent e) {
        //TODO: Way point reach listener, stop the waypoint here...
    }

}
