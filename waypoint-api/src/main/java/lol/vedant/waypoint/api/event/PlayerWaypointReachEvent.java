package lol.vedant.waypoint.api.event;

import lol.vedant.waypoint.api.waypoint.PWaypoint;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerWaypointReachEvent extends Event {

    private static final HandlerList handlerList = new HandlerList();
    private final Player player;
    private final PWaypoint waypoint;

    public PlayerWaypointReachEvent(Player player, PWaypoint waypoint) {
        this.player = player;
        this.waypoint = waypoint;
    }

    public Player getPlayer() {
        return player;
    }

    public PWaypoint getWaypoint() {
        return waypoint;
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }
}
