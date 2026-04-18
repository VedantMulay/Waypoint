package lol.vedant.waypoint.menu;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public class ChatInputManager implements Listener {


    private static final Map<UUID, ChatInputContext> inputMap = new ConcurrentHashMap<>();

    private static JavaPlugin plugin;

    public static void init(JavaPlugin mainPlugin) {
        plugin = mainPlugin;
        plugin.getServer().getPluginManager().registerEvents(new ChatInputManager(), plugin);
    }

    public static void waitForInput(Player player, Consumer<String> response) {
        waitForInput(player, response, null);
    }

    public static void waitForInput(Player player, Consumer<String> response, Runnable onCancel) {
        inputMap.put(player.getUniqueId(), new ChatInputContext(response, onCancel));
        player.sendMessage("§7Please type your input in chat. Type §ccancel §7to abort.");
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();
        if (!inputMap.containsKey(uuid)) return;

        event.setCancelled(true);
        String message = event.getMessage();
        Player player = event.getPlayer();

        ChatInputContext context = inputMap.remove(uuid);

        Bukkit.getScheduler().runTask(plugin, () -> {
            if (message.equalsIgnoreCase("cancel")) {
                player.sendMessage("§cInput cancelled.");
                if (context.onCancel != null) context.onCancel.run();
            } else {
                context.response.accept(message);
            }
        });
    }

    private static class ChatInputContext {
        final Consumer<String> response;
        final Runnable onCancel;

        ChatInputContext(Consumer<String> response, Runnable onCancel) {
            this.response = response;
            this.onCancel = onCancel;
        }
    }

}
