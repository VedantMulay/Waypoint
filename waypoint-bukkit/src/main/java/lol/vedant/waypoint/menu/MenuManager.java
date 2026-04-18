package lol.vedant.waypoint.menu;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MenuManager {

    private static final Map<UUID, Menu> openMenus = new HashMap<>();

    public static void setOpenMenu(Player player, Menu menu) {
        openMenus.put(player.getUniqueId(), menu);
    }

    public static Menu getOpenMenu(Player player) {
        return openMenus.get(player.getUniqueId());
    }

    public static void closeMenu(Player player) {
        openMenus.remove(player.getUniqueId());
    }

}
