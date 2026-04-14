package lol.vedant.waypoint.api.hologram;

import org.bukkit.Location;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Hologram {

    private String identifier;
    private List<String> content;
    private Location location;
    private double offSet;


    private final Map<Integer, LineData> lines = new HashMap<>();

    public Hologram(String identifier, Location location, List<String> content, double offSet) {
        this.identifier = identifier;
        this.location = location;
        this.content = content;
        this.offSet = offSet;
    }

    public static class LineData {
        private final int entityId;
        private final UUID uuid;

        public LineData(int entityId, UUID uuid) {
            this.entityId = entityId;
            this.uuid = uuid;
        }

        public int getEntityId() {
            return entityId;
        }

        public UUID getUuid() {
            return uuid;
        }
    }

    public void setLine(int index, int entityId, UUID uuid) {
        lines.put(index, new LineData(entityId, uuid));
    }

    public LineData getLine(int index) {
        return lines.get(index);
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
        this.content = content;
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

    public void setOffSet(double offSet) {
        this.offSet = offSet;
    }

    public Location getBaseLocation() {
        return location.clone().add(0, offSet, 0);
    }
}