package lol.vedant.waypoint.waypoint;

import lol.vedant.waypoint.Waypoint;
import lol.vedant.waypoint.api.event.PlayerWaypointReachEvent;
import lol.vedant.waypoint.api.event.PlayerWaypointStartEvent;
import lol.vedant.waypoint.api.hologram.Hologram;
import lol.vedant.waypoint.api.hologram.HologramManager;
import lol.vedant.waypoint.api.waypoint.PWaypoint;
import lol.vedant.waypoint.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.Arrays;

public class PlayerWaypoint implements PWaypoint {

    private final String identifier;
    private final String name;
    private Player player;
    private final String yUp = "&a(↑)";
    private final String yDown = "&a(↓)";
    private String requiredPermission = null;
    private final Location location;
    private String yIndicator = "";


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

    public void setPlayer(Player player) {
        this.player = player;
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
                Util.cc(Arrays.asList(
                        "&6✦ &e" + name + " &6✦",
                        "&7⬛ &cDistance: &f" + Math.round(player.getLocation().distance(location)) + " &7blocks"
                )),
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

                    //show the Y arrow indicator
                    if(distance < 100) {
                        Location loc = player.getLocation();
                        if(loc.getY() < location.getY()) {
                            yIndicator = yUp;
                        } else {
                            yIndicator = yDown;
                        }
                    }

                    if (distance < 2) {
                        stop();
                        Bukkit.getServer().getPluginManager().callEvent(new PlayerWaypointReachEvent(player, this));

                        return;
                    }

                    hologram.getDisplay().setText(String.join("\n", Util.cc(Arrays.asList(
                            "&6✦ &e" + name + yIndicator + " &6✦",
                            "&7⬛ &cDistance: &f" + Math.round(player.getLocation().distance(location)) + " &7blocks"
                    ))));
                    hologramManager.updateHologramText(player, hologram);

                    Location newLoc = getHologramLocation();
                    hologramManager.teleportHologram(player, hologram.getIdentifier(), newLoc);
                },
                0L,
                1L
        );
    }

    public void stop() {

        if (task != null) {
            task.cancel();
            task = null;
        }

        hologramManager.removeHologram(hologram);

    }

    private Location getHologramLocation() {
        Location playerLoc = player.getLocation();

        Vector direction = location.toVector()
                .subtract(playerLoc.toVector())
                .setY(0)
                .normalize();


        return playerLoc.clone()
                .add(direction.multiply(10))
                .add(0, 2.2, 0);
    }
}