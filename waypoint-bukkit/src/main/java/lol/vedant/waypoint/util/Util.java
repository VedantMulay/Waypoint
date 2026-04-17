package lol.vedant.waypoint.util;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class Util {

    public static Location fromString(String loc) {
        String[] locs = loc.split(", ");
        return new Location(Bukkit.getWorld(locs[0]), Double.parseDouble(locs[1]), Double.parseDouble(locs[2]), Double.parseDouble(locs[3]));
    }

    public static String fromLocation(Location loc) {
        return loc.getWorld().getName() + ", " + loc.getX() + ", " + loc.getY() + ", " + loc.getZ();
    }

}
