package lol.vedant.waypoint.menu;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;

public class MenuListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player player)) return;

        Menu menu = MenuManager.getOpenMenu(player);
        if (menu == null) return;

        if (!menu.isThisMenu(e.getView().getTopInventory())) return;

        if (e.getClickedInventory() == null) return;

        if (!e.getClickedInventory().equals(e.getView().getTopInventory())) return;

        e.setCancelled(true);

        menu.handleClick(player, e.getSlot(), e.getClick());
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        if (!(e.getPlayer() instanceof Player player)) return;

        Menu menu = MenuManager.getOpenMenu(player);
        if (menu == null) return;

        if (!menu.isThisMenu(e.getInventory())) return;

        menu.handleClose(player);
        MenuManager.closeMenu(player);
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent e) {
        if (!(e.getWhoClicked() instanceof Player player)) return;

        Menu menu = MenuManager.getOpenMenu(player);
        if (menu == null) return;

        if (!menu.isThisMenu(e.getView().getTopInventory())) return;

        if (menu.isCancelDrag()) {
            e.setCancelled(true);
        }
    }

}
