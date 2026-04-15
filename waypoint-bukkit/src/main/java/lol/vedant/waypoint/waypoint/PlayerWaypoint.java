package lol.vedant.waypoint.waypoint;

import lol.vedant.waypoint.Waypoint;
import lol.vedant.waypoint.api.event.PlayerWaypointReachEvent;
import lol.vedant.waypoint.api.event.PlayerWaypointStartEvent;
import lol.vedant.waypoint.api.hologram.Hologram;
import lol.vedant.waypoint.api.hologram.HologramManager;
import lol.vedant.waypoint.api.waypoint.PWaypoint;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.Collections;

public class PlayerWaypoint implements PWaypoint {

    private final String name;
    private final Player player;
    private final Location location;

    private final HologramManager hologramManager = Waypoint.getInstance().getHologramManager();
    private Hologram hologram;
    private BukkitTask task;

    public PlayerWaypoint(Player player, String name, Location location) {
        this.player = player;
        this.location = location;
        this.name = name;
    }

    public Player getPlayer() {
        return player;
    }

    public Location getLocation() {
        return location;
    }

    public String getName() {
        return name;
    }

    public void start() {
        hologram = new Hologram(
                "waypoint_" + player.getUniqueId(),
                location,
                Collections.singletonList("§aWaypoint"),
                0
        );

        Location holoLoc = getHologramLocation();
        hologram.setLocation(holoLoc);

        hologramManager.createHologram(player, hologram);

        Bukkit.getServer().getPluginManager().callEvent(new PlayerWaypointStartEvent(player, this));

        task = Bukkit.getScheduler().runTaskTimer(
                Bukkit.getPluginManager().getPlugins()[0],
                () -> {
                    if (!player.isOnline()) {
                        stop();
                        return;
                    }

                    double distance = player.getLocation().distance(location);

                    if (distance < 2) {
                        stop();
                        return;
                    }

                    Location newLoc = getHologramLocation();
                    hologramManager.teleportHologram(player, hologram.getIdentifier(), newLoc);
                },
                0L,
                1L
        );
    }

    public void stop() {
        Bukkit.getServer().getPluginManager().callEvent(new PlayerWaypointReachEvent(player, this));

        if (task != null) {
            task.cancel();
            task = null;
        }

        //remove hologram

    }

    private Location getHologramLocation() {
        Location playerLoc = player.getLocation();

        Vector direction = location.toVector()
                .subtract(playerLoc.toVector())
                .normalize();

        return playerLoc.clone()
                .add(direction.multiply(2.5))
                .add(0, 2.2, 0);
    }
}