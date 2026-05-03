package lol.vedant.waypoint.command.admin;

import lol.vedant.waypoint.Waypoint;
import lol.vedant.waypoint.api.waypoint.PWaypoint;
import lol.vedant.waypoint.menu.menus.GlobalWaypointsMenu;
import lol.vedant.waypoint.util.Messages;
import lol.vedant.waypoint.util.Util;
import lol.vedant.waypoint.waypoint.PlayerWaypoint;
import me.despical.commandframework.CommandArguments;
import me.despical.commandframework.annotations.Command;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;

public class WaypointCreateCommand {

    Waypoint plugin = Waypoint.getInstance();

    @Command(
            name = "waypoint-admin.create",
            permission = "waypoint.admin.create",
            senderType = Command.SenderType.PLAYER
    )
    public void createGlobalWaypoint(CommandArguments args) {
        Player player = args.getSender();

        if (args.getLength() < 1) {
            player.sendMessage(Util.cc("&cUsage: /waypoint-admin create <id> [display name...]"));
            return;
        }

        String id = args.getArgument(0);

        String name;
        if (args.getLength() > 1) {
            String[] allArgs = args.getArguments();
            StringBuilder nameBuilder = new StringBuilder();
            for (int i = 1; i < allArgs.length; i++) {
                if (i > 1) nameBuilder.append(" ");
                nameBuilder.append(allArgs[i]);
            }
            name = nameBuilder.toString();
        } else {
            name = id;
        }

        if (plugin.getDatabase().getWaypoint(id) != null) {
            player.sendMessage(Messages.get("global-waypoint-exists", "%id%", id));
            return;
        }

        Location loc = player.getLocation();
        plugin.getDatabase().createWaypoint(new PlayerWaypoint(id, name, loc));
        player.sendMessage(Messages.get("global-waypoint-created", "%name%", name, "%id%", id));
    }

    @Command(
            name = "waypoint-admin.delete",
            permission = "waypoint.admin.delete",
            senderType = Command.SenderType.BOTH
    )
    public void deleteGlobalWaypoint(CommandArguments args) {
        if (args.getLength() < 1) {
            args.getSender().sendMessage(Util.cc("&cUsage: /waypoint-admin delete <id>"));
            return;
        }

        String id = args.getArgument(0);
        if (plugin.getDatabase().getWaypoint(id) == null) {
            args.getSender().sendMessage(Messages.get("global-waypoint-not-found", "%id%", id));
            return;
        }

        plugin.getDatabase().deleteWaypoint(id);
        args.getSender().sendMessage(Messages.get("global-waypoint-deleted", "%id%", id));
    }

    @Command(
            name = "waypoint-admin.list",
            permission = "waypoints.admin",
            senderType = Command.SenderType.BOTH
    )
    public void listGlobalWaypoints(CommandArguments args) {
        List<PWaypoint> waypoints = plugin.getDatabase().getAllWaypoints();

        if (waypoints.isEmpty()) {
            args.getSender().sendMessage(Messages.get("no-global-waypoints"));
            return;
        }

        if (args.isSenderPlayer()) {
            Player player = args.getSender();
            new GlobalWaypointsMenu(waypoints, 0).open(player);
        } else {
            args.getSender().sendMessage(Util.cc("&6Global Waypoints &7(" + waypoints.size() + "):"));
            for (PWaypoint wp : waypoints) {
                args.getSender().sendMessage(Util.cc("  &7- &f" + wp.getName() + " &7(" + wp.getIdentifier() + ")"));
            }
        }
    }

    @Command(
            name = "waypoint-admin.reload",
            permission = "waypoints.admin",
            senderType = Command.SenderType.BOTH
    )
    public void reloadWaypoints(CommandArguments args) {
        plugin.getWaypointManager().load();
        args.getSender().sendMessage(Messages.get("waypoints-reloaded"));
    }
}

