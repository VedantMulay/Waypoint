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

import java.util.Arrays;

public class PlayerWaypoint implements PWaypoint {

    private final String identifier;
    private final String name;
    private final Player player;
    private final String yUp = "";
    private final String yDown = "";
    private String requiredPermission = null;
    private final Location location;


    private Waypoint plugin = Waypoint.getInstance();
    private final HologramManager hologramManager = Waypoint.getInstance().getHologramManager();
    private Hologram hologram;
    private BukkitTask task;

    public PlayerWaypoint(String identifier, Player player, String name, Location location) {
        this.identifier = identifier;
        this.player = player;
        this.location = location;
        this.name = name;
    }

    public PlayerWaypoint(String identifier, Player player, String name, Location location, String requiredPermission) {
        this.identifier = identifier;
        this.player = player;
        this.location = location;
        this.name = name;
        this.requiredPermission = requiredPermission;
    }

    public PlayerWaypoint(String identifier, String name, Location location, String requiredPermission) {
        this.identifier = identifier;
        this.player = null;
        this.location = location;
        this.requiredPermission = requiredPermission;
        this.name = name;
    }

    public PlayerWaypoint(String identifier, String name, Location location) {
        this.identifier = identifier;
        this.player = null;
        this.location = location;
        this.name = name;
    }

    public String getIdentifier() {
        return identifier;
    }


    public Player getPlayer() {
        return player;
    }

    public String getPermission() {
        return requiredPermission;
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
                Arrays.asList("§aWaypoint", "§cRemaining Blocks: §f" + 0),
                0
        );

        Location holoLoc = getHologramLocation();
        hologram.setLocation(holoLoc);

        hologramManager.createHologram(player, hologram);

        Bukkit.getServer().getPluginManager().callEvent(new PlayerWaypointStartEvent(player, this));

        plugin.getWaypointManager().startWaypoint(player, this);

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

                    hologram.setContent(Arrays.asList("§aWaypoint", "§cRemaining Blocks: §f" + Math.round(distance)));
                    hologramManager.updateHologramText(player, hologram);

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

        plugin.getWaypointManager().stopWaypoint(player);
        hologramManager.removeHologram(identifier);

    }

    private Location getHologramLocation() {
        Location playerLoc = player.getLocation();

        Vector direction = location.toVector()
                .subtract(playerLoc.toVector())
                .setY(0)
                .normalize();

        Location newLoc = playerLoc.clone()
                .add(direction.multiply(7.5))
                .add(0, 2.2, 0);


        return newLoc;
    }
}