package lol.vedant.waypoint.api.hologram;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.WrappedDataValue;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;


public class HologramManager {

    private static final AtomicInteger ENTITY_ID_COUNTER = new AtomicInteger(100000);
    private final ProtocolManager protocolManager;
    private final Map<String, Hologram> holograms = new HashMap<>();

    public HologramManager(ProtocolManager protocolManager) {
        this.protocolManager = protocolManager;
    }

    public void createHologram(Player player, Hologram holo) {
        Location base = holo.getBaseLocation();
        holo.clearLines();

        int index = 0;

        for (String line : holo.getContent()) {
            int entityId = getEntityId();
            UUID uuid = UUID.randomUUID();

            Location loc = base.clone().add(0, -0.25 * index, 0);

            PacketContainer spawnPacket = new PacketContainer(PacketType.Play.Server.SPAWN_ENTITY);
            spawnPacket.getIntegers().write(0, entityId);
            spawnPacket.getUUIDs().write(0, uuid);
            spawnPacket.getEntityTypeModifier().write(0, EntityType.TEXT_DISPLAY);
            spawnPacket.getDoubles()
                    .write(0, loc.getX())
                    .write(1, loc.getY())
                    .write(2, loc.getZ());

            protocolManager.sendServerPacket(player, spawnPacket);

            PacketContainer metaPacket = new PacketContainer(PacketType.Play.Server.ENTITY_METADATA);
            metaPacket.getIntegers().write(0, entityId);

            List<WrappedDataValue> values = new ArrayList<>();

            values.add(new WrappedDataValue(0, WrappedDataWatcher.Registry.get(Byte.class), (byte) 0));

            values.add(new WrappedDataValue(15, WrappedDataWatcher.Registry.get(Byte.class), (byte) 1));

            values.add(new WrappedDataValue(23, WrappedDataWatcher.Registry.getChatComponentSerializer(false), WrappedChatComponent.fromText(line).getHandle()));

            values.add(new WrappedDataValue(25, WrappedDataWatcher.Registry.get(Integer.class), 0));

            values.add(new WrappedDataValue(26, WrappedDataWatcher.Registry.get(Byte.class), (byte) -1));

            values.add(new WrappedDataValue(27, WrappedDataWatcher.Registry.get(Byte.class), (byte) 0x02));

            metaPacket.getDataValueCollectionModifier().write(0, values);
            protocolManager.sendServerPacket(player, metaPacket);

            holo.setLine(index, entityId, uuid);
            index++;
        }

        holograms.put(holo.getIdentifier(), holo);
    }

//    public void createHologram(Player player, Hologram hologram) {
//        Location base = hologram.getBaseLocation();
//        hologram.clearLines();
//
//        int index = 0;
//
//        for (String line : hologram.getContent()) {
//            int entityId = getEntityId();
//            UUID uuid = UUID.randomUUID();
//
//            Location loc = base.clone().add(0, -0.25 * index, 0);
//
//            PacketContainer spawn = new PacketContainer(PacketType.Play.Server.SPAWN_ENTITY);
//            spawn.getIntegers().write(0, entityId);
//            spawn.getUUIDs().write(0, uuid);
//            spawn.getEntityTypeModifier().write(0, EntityType.ARMOR_STAND);
//            spawn.getDoubles()
//                    .write(0, loc.getX())
//                    .write(1, loc.getY())
//                    .write(2, loc.getZ());
//
//            protocolManager.sendServerPacket(player, spawn);
//
//            PacketContainer meta = new PacketContainer(PacketType.Play.Server.ENTITY_METADATA);
//            meta.getIntegers().write(0, entityId);
//
//            List<WrappedDataValue> values = new ArrayList<>();
//
//            values.add(new WrappedDataValue(
//                    0,
//                    WrappedDataWatcher.Registry.get(Byte.class),
//                    (byte) 0x20
//            ));
//
//            values.add(new WrappedDataValue(
//                    2,
//                    WrappedDataWatcher.Registry.getChatComponentSerializer(true),
//                    Optional.of(WrappedChatComponent.fromText(line).getHandle())
//            ));
//
//            values.add(new WrappedDataValue(
//                    3,
//                    WrappedDataWatcher.Registry.get(Boolean.class),
//                    true
//            ));
//
//            values.add(new WrappedDataValue(
//                    15,
//                    WrappedDataWatcher.Registry.get(Byte.class),
//                    (byte) 0x10
//            ));
//
//            meta.getDataValueCollectionModifier().write(0, values);
//
//            protocolManager.sendServerPacket(player, meta);
//
//            hologram.setLine(index, entityId, uuid);
//
//            index++;
//        }
//
//        holograms.put(hologram.getIdentifier(), hologram);
//    }

    public void teleportHologram(Player player, String identifier, Location base) {
        Hologram hologram = holograms.get(identifier);
        if (hologram == null) return;

        int index = 0;

        for (Hologram.LineData data : hologram.getLines().values()) {
            Location loc = base.clone().add(0, -0.25 * index, 0);

            PacketContainer teleport = new PacketContainer(PacketType.Play.Server.ENTITY_TELEPORT);
            teleport.getIntegers().write(0, data.getEntityId());
            teleport.getDoubles()
                    .write(0, loc.getX())
                    .write(1, loc.getY())
                    .write(2, loc.getZ());
            teleport.getBytes()
                    .write(0, (byte) 0)
                    .write(1, (byte) 0);

            protocolManager.sendServerPacket(player, teleport);

            index++;
        }
    }

    public void updateHologramText(Player player, Hologram hologram) {
        List<String> newContent = hologram.getContent();

        for (Hologram.LineData lineData : hologram.getLines().values()) {
            int lineIndex = lineData.getLineIndex();

            if (lineIndex < 0 || lineIndex >= newContent.size()) {
                continue;
            }

            String newText = newContent.get(lineIndex);

            PacketContainer metaPacket = new PacketContainer(PacketType.Play.Server.ENTITY_METADATA);
            metaPacket.getIntegers().write(0, lineData.getEntityId());

            List<WrappedDataValue> values = new ArrayList<>();

            values.add(new WrappedDataValue(
                    23,
                    WrappedDataWatcher.Registry.getChatComponentSerializer(false),
                    WrappedChatComponent.fromText(newText).getHandle()
            ));

            values.add(new WrappedDataValue(
                    15,
                    WrappedDataWatcher.Registry.get(Byte.class),
                    (byte) 1
            ));

            metaPacket.getDataValueCollectionModifier().write(0, values);
            protocolManager.sendServerPacket(player, metaPacket);
        }
    }

    public void removeHologram(String identifier) {
        Hologram holo = holograms.get(identifier);

        // collect entity IDs
        int[] entityIds = holo.getLines().values().stream()
                .mapToInt(Hologram.LineData::getEntityId)
                .toArray();

        // send packet to remove the holo
        PacketContainer destroyPacket = protocolManager.createPacket(PacketType.Play.Server.ENTITY_DESTROY);
        destroyPacket.getIntegerArrays().write(0, entityIds);

        holograms.remove(identifier);
    }

    private int getEntityId() {
        return ENTITY_ID_COUNTER.incrementAndGet();
    }
}