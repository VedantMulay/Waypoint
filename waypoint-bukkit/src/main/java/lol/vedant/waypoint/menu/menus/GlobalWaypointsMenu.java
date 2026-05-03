package lol.vedant.waypoint.menu.menus;

import com.cryptomorin.xseries.XMaterial;
import lol.vedant.waypoint.Waypoint;
import lol.vedant.waypoint.api.item.ItemBuilder;
import lol.vedant.waypoint.api.waypoint.PWaypoint;
import lol.vedant.waypoint.menu.Menu;
import lol.vedant.waypoint.menu.MenuItem;
import lol.vedant.waypoint.util.Util;

import java.util.ArrayList;
import java.util.List;

public class GlobalWaypointsMenu extends Menu {

    private static final int ITEMS_PER_PAGE = 27;

    Waypoint plugin = Waypoint.getInstance();

    public GlobalWaypointsMenu(List<PWaypoint> waypoints, int page) {
        super(buildTitle(waypoints.size(), page), 4);

        int totalPages = Math.max(1, (int) Math.ceil((double) waypoints.size() / ITEMS_PER_PAGE));
        int start = page * ITEMS_PER_PAGE;
        int end = Math.min(start + ITEMS_PER_PAGE, waypoints.size());
        List<PWaypoint> pageWaypoints = waypoints.subList(start, end);

        for (int i = 0; i < pageWaypoints.size(); i++) {
            PWaypoint wp = pageWaypoints.get(i);
            setItem(i, new MenuItem(
                    new ItemBuilder(XMaterial.NETHER_STAR.get())
                            .name(Util.cc("&b" + wp.getName()))
                            .lore(buildLore(wp))
                            .build(),
                    (player, clickType) -> {
                        if (clickType.isLeftClick()) {
                            wp.setPlayer(player);
                            plugin.getWaypointManager().startWaypoint(player, wp);
                            player.closeInventory();
                        } else if (clickType.isRightClick() && player.hasPermission("waypoints.admin")) {
                            plugin.getDatabase().deleteWaypoint(wp.getIdentifier());
                            player.sendMessage(Util.cc("&cGlobal waypoint &f" + wp.getName() + " &cdeleted!"));
                            List<PWaypoint> updated = plugin.getDatabase().getAllWaypoints();
                            int targetPage = Math.min(page, Math.max(0, (int) Math.ceil((double) updated.size() / ITEMS_PER_PAGE) - 1));
                            new GlobalWaypointsMenu(updated, targetPage).open(player);
                        }
                    }
            ));
        }

        // Navigation row (slots 27-35)
        if (page > 0) {
            setItem(27, new MenuItem(
                    new ItemBuilder(XMaterial.ARROW.get())
                            .name(Util.cc("&ePrevious Page"))
                            .build(),
                    (player, clickType) -> new GlobalWaypointsMenu(waypoints, page - 1).open(player)
            ));
        } else {
            setItem(27, new MenuItem(
                    new ItemBuilder(XMaterial.GRAY_STAINED_GLASS_PANE.get())
                            .name(Util.cc("&7Previous Page"))
                            .build()
            ));
        }

        // Back to personal waypoints
        setItem(31, new MenuItem(
                new ItemBuilder(XMaterial.COMPASS.get())
                        .name(Util.cc("&aPersonal Waypoints"))
                        .lore(Util.cc("&7Click to view your personal waypoints"))
                        .build(),
                (player, clickType) -> {
                    List<PWaypoint> personal = plugin.getDatabase().getAllPlayerWaypoints(player.getUniqueId());
                    new WaypointsMenu(personal, 0).open(player);
                }
        ));

        if (page < totalPages - 1) {
            setItem(35, new MenuItem(
                    new ItemBuilder(XMaterial.ARROW.get())
                            .name(Util.cc("&eNext Page"))
                            .build(),
                    (player, clickType) -> new GlobalWaypointsMenu(waypoints, page + 1).open(player)
            ));
        } else {
            setItem(35, new MenuItem(
                    new ItemBuilder(XMaterial.GRAY_STAINED_GLASS_PANE.get())
                            .name(Util.cc("&7Next Page"))
                            .build()
            ));
        }
    }

    private static String buildTitle(int total, int page) {
        int totalPages = Math.max(1, (int) Math.ceil((double) total / ITEMS_PER_PAGE));
        return Util.cc("&bGlobal Waypoints &7(" + (page + 1) + "/" + totalPages + ")");
    }

    private List<String> buildLore(PWaypoint wp) {
        List<String> lore = new ArrayList<>();
        if (wp.getLocation() != null && wp.getLocation().getWorld() != null) {
            lore.add(Util.cc("&7Location: &f" + Util.fromLocation(wp.getLocation())));
        }
        lore.add(Util.cc("&7Left-click: &aNavigate"));
        lore.add(Util.cc("&7Right-click: &cDelete &7(admin only)"));
        return lore;
    }
}
