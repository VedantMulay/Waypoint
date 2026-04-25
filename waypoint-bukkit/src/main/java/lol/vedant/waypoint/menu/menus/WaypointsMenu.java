package lol.vedant.waypoint.menu.menus;

import com.cryptomorin.xseries.XMaterial;
import lol.vedant.waypoint.Waypoint;
import lol.vedant.waypoint.api.item.ItemBuilder;
import lol.vedant.waypoint.api.waypoint.PWaypoint;
import lol.vedant.waypoint.menu.Menu;
import lol.vedant.waypoint.menu.MenuItem;

import java.util.List;

public class WaypointsMenu extends Menu {

    Waypoint plugin = Waypoint.getInstance();

    public WaypointsMenu(String title, int rows, List<PWaypoint> waypoints) {
        super(title, 3);

        int totalPages = (int) Math.ceil((double) waypoints.size() / 18);

        int pageSlotIndex = 0;

        for (PWaypoint wp : waypoints) {

            //TODO: change the lore of the object if the waypoint is running

            setItem(pageSlotIndex, new MenuItem(
                    new ItemBuilder(XMaterial.COMPASS.get())
                            .name(wp.getName())
                            .amount(1)
                            .build(),
                    (player, clickType) -> {
                        if(clickType.isLeftClick()) {
                            //add uninitialized player variable
                            wp.setPlayer(player);
                            plugin.getWaypointManager().startWaypoint(player, wp);
                            //TODO: Add refresh menu after click
                        }
            }
            ));
            pageSlotIndex++;
        }
    }


}
