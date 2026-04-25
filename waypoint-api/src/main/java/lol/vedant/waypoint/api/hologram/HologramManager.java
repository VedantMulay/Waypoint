package lol.vedant.waypoint.api.hologram;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.Display;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.TextDisplay;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Transformation;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;


public class HologramManager {

    private static final AtomicInteger ENTITY_ID_COUNTER = new AtomicInteger(100000);
    private final Plugin plugin;
    private final Map<String, Hologram> holograms = new HashMap<>();


    public HologramManager(Plugin plugin) {
        this.plugin = plugin;
    }

    public void createHologram(Player player, Hologram holo) {
        TextDisplay display = (TextDisplay) player.getWorld()
                .spawnEntity(holo.getLocation(), EntityType.TEXT_DISPLAY);

        display.setVisibleByDefault(false);
        display.setDefaultBackground(false);
        display.setBackgroundColor(Color.fromARGB(0, 0, 0, 0));
        display.setBillboard(Display.Billboard.CENTER);
        display.setSeeThrough(true);
        display.setShadowed(false);
        display.setText(String.join("\n", holo.getContent()));
        display.setTextOpacity((byte) 255);
        display.setInterpolationDelay(0);
        display.setInterpolationDuration(4);

        Transformation transformation = display.getTransformation();
        transformation.getScale().set(2.0f, 2.0f, 2.0f);
        display.setTransformation(transformation);

        player.showEntity(plugin, display);
        holo.setDisplay(display);

        holograms.put(holo.getIdentifier(), holo);
    }

    public void teleportHologram(Player player, String identifier, Location base) {
        Hologram holo = holograms.get(identifier);
        holo.getDisplay().teleport(base);
    }

    public void updateHologramText(Player player, Hologram hologram) {
        holograms.get(hologram.getIdentifier()).setDisplay(hologram.getDisplay());
    }

    public void removeHologram(Hologram holo) {
        holo.getDisplay().remove();

        holograms.remove(holo.getIdentifier());
    }

    private int getEntityId() {
        return ENTITY_ID_COUNTER.incrementAndGet();
    }
}