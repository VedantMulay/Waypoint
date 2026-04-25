package lol.vedant.waypoint.api.hologram;

import org.bukkit.Location;
import org.bukkit.entity.TextDisplay;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Hologram {

    private final String identifier;
    private List<String> content;
    private Location location;
    private final double offSet;
    private TextDisplay display;

    // index (line number) -> LineData
    private final Map<Integer, LineData> lines = new HashMap<>();

    public Hologram(String identifier, Location location, List<String> content, double offSet) {
        this.identifier = identifier;
        this.location = location;
        this.content = new ArrayList<>(content); // defensive copy
        this.offSet = offSet;
    }

    public static class LineData {
        private final int entityId;
        private final UUID uuid;
        private final int lineIndex;        // <-- This was missing!

        public LineData(int entityId, UUID uuid, int lineIndex) {
            this.entityId = entityId;
            this.uuid = uuid;
            this.lineIndex = lineIndex;
        }

        public int getEntityId() {
            return entityId;
        }

        public UUID getUuid() {
            return uuid;
        }

        public int getLineIndex() {
            return lineIndex;
        }
    }

    public TextDisplay getDisplay() {
        return display;
    }

    public void setDisplay(TextDisplay display) {
        this.display = display;
    }

    public void setLine(int index, int entityId, UUID uuid) {
        lines.put(index, new LineData(entityId, uuid, index));
    }

    public Map<Integer, LineData> getLines() {
        return lines;
    }

    public void clearLines() {
        lines.clear();
    }

    public String getIdentifier() {
        return identifier;
    }

    public List<String> getContent() {
        return content;
    }

    public void setContent(List<String> content) {
        this.content = new ArrayList<>(content); // defensive copy
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public double getOffSet() {
        return offSet;
    }

    public Location getBaseLocation() {
        return location.clone().add(0, offSet, 0);
    }
}