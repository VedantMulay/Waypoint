package lol.vedant.waypoint.util;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

public class Util {

    public static Location fromString(String loc) {
        String[] locs = loc.split(", ");
        return new Location(Bukkit.getWorld(locs[0]), Integer.parseInt(locs[1]), Integer.parseInt(locs[2]), Integer.parseInt(locs[3]));
    }

    public static String fromLocation(Location loc) {
        return loc.getWorld().getName() + ", " + loc.getBlockX() + ", " + loc.getBlockY() + ", " + loc.getBlockZ();
    }

    public static String cc(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    public static List<String> cc(List<String> text) {
        List<String> cc = new ArrayList<>();
        text.forEach(s -> {
            cc.add(ChatColor.translateAlternateColorCodes('&', s));
        });
        return cc;
    }

    public static String slugify(String input) {
        if (input == null) return "";

        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);

        return normalized
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
                .toLowerCase()
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("^-+|-+$", "")
                .replaceAll("-{2,}", "-");
    }


}
