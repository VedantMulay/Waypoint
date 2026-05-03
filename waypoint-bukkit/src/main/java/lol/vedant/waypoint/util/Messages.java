package lol.vedant.waypoint.util;

import lol.vedant.waypoint.Waypoint;

/**
 * Utility class for reading configurable messages from config.yml.
 * All messages support Bukkit color codes and the %prefix% placeholder.
 */
public class Messages {

    /**
     * Returns the formatted message for the given key, with the prefix substituted
     * and color codes translated.
     *
     * @param key the message key under the "messages" section of config.yml
     * @return the colored, prefix-substituted message string
     */
    public static String get(String key) {
        String prefix = Waypoint.getInstance().getConfiguration()
                .getString("messages.prefix", "&8[&6Waypoint&8] &r");
        String message = Waypoint.getInstance().getConfiguration()
                .getString("messages." + key, "&cMissing message: " + key);
        return Util.cc(message.replace("%prefix%", prefix));
    }

    /**
     * Returns the formatted message for the given key with placeholder replacements
     * applied before color translation.
     *
     * @param key          the message key under the "messages" section of config.yml
     * @param replacements alternating placeholder/value pairs, e.g. "%name%", "Home"
     * @return the colored, substituted message string
     */
    public static String get(String key, String... replacements) {
        String prefix = Waypoint.getInstance().getConfiguration()
                .getString("messages.prefix", "&8[&6Waypoint&8] &r");
        String message = Waypoint.getInstance().getConfiguration()
                .getString("messages." + key, "&cMissing message: " + key);
        message = message.replace("%prefix%", prefix);
        for (int i = 0; i + 1 < replacements.length; i += 2) {
            message = message.replace(replacements[i], replacements[i + 1]);
        }
        return Util.cc(message);
    }
}
