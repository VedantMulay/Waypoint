package lol.vedant.waypoint.menu.menus;

import com.cryptomorin.xseries.XMaterial;
import com.cryptomorin.xseries.XSound;
import lol.vedant.waypoint.Waypoint;
import lol.vedant.waypoint.api.item.ItemBuilder;
import lol.vedant.waypoint.menu.ChatInputManager;
import lol.vedant.waypoint.menu.Menu;
import lol.vedant.waypoint.menu.MenuItem;
import lol.vedant.waypoint.util.Messages;
import lol.vedant.waypoint.util.Util;
import lol.vedant.waypoint.waypoint.PlayerWaypoint;
import org.bukkit.Location;


public class CreateWaypointMenu extends Menu {

    Waypoint plugin = Waypoint.getInstance();
    private String name = "";
    private Location location;

    public CreateWaypointMenu(String title, int rows) {
        super(title, 3);

        if(name.isEmpty()) {
            setItem(12, new MenuItem(
                    new ItemBuilder(XMaterial.OAK_SIGN.get())
                            .name(Util.cc("&aSet waypoint name"))
                            .lore(Util.cc("&eClick to change"))
                            .build(),

                    (player, clickType) -> {
                        player.closeInventory();
                        ChatInputManager.waitForInput(player, res -> {
                            this.name = res;
                            open(player);
                            XSound.ENTITY_EXPERIENCE_ORB_PICKUP.play(player);
                        });

                    }
            ));
        } else {
            setItem(12, new MenuItem(
                    new ItemBuilder(XMaterial.OAK_SIGN.get())
                            .name(Util.cc("&a" + name))
                            .lore(Util.cc("&eClick to change"))
                            .build(),
                    (player, clickType) -> {
                        player.closeInventory();
                        ChatInputManager.waitForInput(player, res -> {
                            this.name = res;
                            open(player);
                        });

                    }
            ));
        }

        if(location == null) {
            setItem(14, new MenuItem(
                    new ItemBuilder(XMaterial.RED_WOOL.get())
                            .name(Util.cc("&aSet waypoint location"))
                            .lore(Util.cc("&eClick to set your current location"))
                            .build(),
                    (player, clickType) -> {
                        this.location = player.getLocation();
                        XSound.ENTITY_EXPERIENCE_ORB_PICKUP.play(player);
                        open(player);
                    }
            ));
        } else {
            setItem(14, new MenuItem(
                    new ItemBuilder(XMaterial.GREEN_WOOL.get())
                            .name(Util.cc("&aWaypoint location"))
                            .addLore(Util.cc("&eLocation: &f" + Util.fromLocation(location)))
                            .addLore(Util.cc("&eClick to update to your current location"))
                            .build(),
                    (player, clickType) -> {
                        this.location = player.getLocation();
                        XSound.ENTITY_EXPERIENCE_ORB_PICKUP.play(player);
                        open(player);
                    }
            ));
        }

        setItem(22, new MenuItem(
                new ItemBuilder(XMaterial.LIME_CONCRETE.get())
                        .name(Util.cc("&aSave"))
                        .build(),
                (player, clickType) -> {
                    if(clickType.isLeftClick()) {
                        if (name.isEmpty()) {
                            player.sendMessage(Messages.get("name-required"));
                            return;
                        }
                        if (location == null) {
                            player.sendMessage(Messages.get("location-required"));
                            return;
                        }
                        plugin.getDatabase().createPlayerWaypoint(player.getUniqueId(), new PlayerWaypoint(Util.slugify(name), name, location));
                        player.sendMessage(Messages.get("waypoint-created", "%name%", name));
                        XSound.ENTITY_EXPERIENCE_ORB_PICKUP.play(player);
                        player.closeInventory();
                    }
                }
        ));

    }



}
