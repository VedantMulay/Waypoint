package lol.vedant.waypoint.menu;


import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.function.BiConsumer;

public class MenuItem {

    private ItemStack item;
    private BiConsumer<Player, ClickType> action;

    public MenuItem(ItemStack item) {
        this.item = item;
    }

    public MenuItem(ItemStack item, BiConsumer<Player, ClickType> clickAction) {
        this.item = item;
        this.action = clickAction;
    }

    public ItemStack getItem() {
        return item;
    }

    public void setItem(ItemStack item) {
        this.item = item;
    }

    public BiConsumer<Player, ClickType> getAction() {
        return action;
    }
}
