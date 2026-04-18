package lol.vedant.waypoint.menu;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.Map;

public class Menu {

    private final Inventory inventory;
    private final int rows;
    private final String title;
    private final Map<Integer, MenuItem> items = new HashMap<>();

    public Menu(String title, int rows) {
        this.title = title;
        this.rows = rows;
        this.inventory = Bukkit.createInventory(null, rows * 9, title);
    }

    public void setItem(int slot, MenuItem item) {
        items.put(slot, item);
        inventory.setItem(slot, item.getItem());
    }

    public void open(Player player) {
        player.openInventory(inventory);
        MenuManager.setOpenMenu(player, this);
    }

    public void handleClick(Player player, int slot, ClickType clickType) {
        MenuItem item = items.get(slot);
        if (item != null && item.getAction() != null) {
            item.getAction().accept(player, clickType);
        }
    }

    public boolean isThisMenu(Inventory inv) {
        return inv.equals(this.inventory);
    }




}
