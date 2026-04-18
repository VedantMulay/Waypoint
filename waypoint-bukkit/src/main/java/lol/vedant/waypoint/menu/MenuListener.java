package lol.vedant.waypoint.menu;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class MenuListener implements Listener {

    @EventHandler
    public void onInventoryOpen(InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player player)) return;

        Menu openMenu = MenuManager.getOpenMenu(player);
        if (openMenu != null && openMenu.isThisMenu(e.getInventory())) {
            e.setCancelled(true); // prevent taking/moving items
            openMenu.handleClick(player, e.getSlot(), e.getClick());
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        if (e.getPlayer() instanceof Player player) {
            MenuManager.closeMenu(player);
        }
    }

}
