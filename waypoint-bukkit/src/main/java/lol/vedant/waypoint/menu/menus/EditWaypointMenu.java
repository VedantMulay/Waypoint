package lol.vedant.waypoint.menu.menus;

import com.cryptomorin.xseries.XMaterial;
import com.cryptomorin.xseries.XSound;
import lol.vedant.waypoint.Waypoint;
import lol.vedant.waypoint.api.item.ItemBuilder;
import lol.vedant.waypoint.api.waypoint.PWaypoint;
import lol.vedant.waypoint.menu.Menu;
import lol.vedant.waypoint.menu.MenuItem;
import lol.vedant.waypoint.util.Util;

import java.util.ArrayList;
import java.util.List;

public class EditWaypointMenu extends Menu {

    Waypoint plugin = Waypoint.getInstance();

    public EditWaypointMenu(PWaypoint waypoint, List<PWaypoint> allWaypoints, int page) {
        super(Util.cc("&6Edit: &f" + waypoint.getName()), 3);

        // Navigate to waypoint
        setItem(10, new MenuItem(
                new ItemBuilder(XMaterial.COMPASS.get())
                        .name(Util.cc("&aNavigate"))
                        .lore(Util.cc("&7Click to navigate to this waypoint"))
                        .build(),
                (player, clickType) -> {
                    waypoint.setPlayer(player);
                    plugin.getWaypointManager().startWaypoint(player, waypoint);
                    player.closeInventory();
                }
        ));

        // Waypoint info
        List<String> infoLore = new ArrayList<>();
        infoLore.add(Util.cc("&7ID: &f" + waypoint.getIdentifier()));
        if (waypoint.getLocation() != null && waypoint.getLocation().getWorld() != null) {
            infoLore.add(Util.cc("&7Location: &f" + Util.fromLocation(waypoint.getLocation())));
        }
        setItem(13, new MenuItem(
                new ItemBuilder(XMaterial.OAK_SIGN.get())
                        .name(Util.cc("&e" + waypoint.getName()))
                        .lore(infoLore)
                        .build()
        ));

        // Delete waypoint
        setItem(16, new MenuItem(
                new ItemBuilder(XMaterial.BARRIER.get())
                        .name(Util.cc("&cDelete Waypoint"))
                        .lore(Util.cc("&7Click to permanently delete this waypoint"))
                        .build(),
                (player, clickType) -> {
                    plugin.getDatabase().deletePlayerWaypoint(player.getUniqueId(), waypoint.getIdentifier());
                    player.sendMessage(Util.cc("&aWaypoint &f" + waypoint.getName() + " &adeleted!"));
                    XSound.ENTITY_ITEM_BREAK.play(player);
                    List<PWaypoint> updated = plugin.getDatabase().getAllPlayerWaypoints(player.getUniqueId());
                    int targetPage = Math.min(page, Math.max(0, (int) Math.ceil((double) updated.size() / 27) - 1));
                    new WaypointsMenu(updated, targetPage).open(player);
                }
        ));

        // Back to waypoints list
        setItem(22, new MenuItem(
                new ItemBuilder(XMaterial.ARROW.get())
                        .name(Util.cc("&eBack"))
                        .lore(Util.cc("&7Return to your waypoints list"))
                        .build(),
                (player, clickType) -> new WaypointsMenu(allWaypoints, page).open(player)
        ));
    }
}

