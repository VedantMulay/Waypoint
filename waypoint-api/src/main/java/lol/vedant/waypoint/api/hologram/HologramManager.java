package lol.vedant.waypoint.api.hologram;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.WrappedDataValue;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.TextDisplay;

import java.lang.reflect.Type;
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

            values.add(new WrappedDataValue(
                    23,
                    WrappedDataWatcher.Registry.getChatComponentSerializer(true),
                    Optional.of(WrappedChatComponent.fromText(line).getHandle())));


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

    private int getEntityId() {
        return ENTITY_ID_COUNTER.incrementAndGet();
    }
}