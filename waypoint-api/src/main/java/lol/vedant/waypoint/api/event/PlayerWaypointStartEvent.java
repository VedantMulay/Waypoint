package lol.vedant.waypoint.api.event;

import lol.vedant.waypoint.api.waypoint.PWaypoint;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerWaypointStartEvent extends Event {

    private static final HandlerList handlerList = new HandlerList();
    private final PWaypoint waypoint;
    private final Player player;


    public PlayerWaypointStartEvent(Player player, PWaypoint waypoint) {
        this.waypoint = waypoint;
        this.player = player;
    }

    public PWaypoint getWaypoint() {
        return waypoint;
    }

    public Player getPlayer() {
        return player;
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }
}
