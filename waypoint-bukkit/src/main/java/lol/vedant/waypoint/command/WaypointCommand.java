package lol.vedant.waypoint.command;

import lol.vedant.waypoint.Waypoint;
import lol.vedant.waypoint.api.waypoint.PWaypoint;
import lol.vedant.waypoint.menu.menus.CreateWaypointMenu;
import lol.vedant.waypoint.menu.menus.GlobalWaypointsMenu;
import lol.vedant.waypoint.menu.menus.WaypointsMenu;
import lol.vedant.waypoint.util.Messages;
import lol.vedant.waypoint.util.Util;
import lol.vedant.waypoint.waypoint.PlayerWaypoint;
import me.despical.commandframework.CommandArguments;
import me.despical.commandframework.annotations.Command;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;

public class WaypointCommand {

    Waypoint plugin = Waypoint.getInstance();

    @Command(
            name = "waypoint.start",
            senderType = Command.SenderType.BOTH
    )
    public void waypointStart(CommandArguments args) {

        if (args.getArguments().length < 3) {
            args.getSender().sendMessage(Util.cc("&cUsage: /waypoint start <x> <y> <z> [player]"));
            return;
        }

        try {
            int x = Integer.parseInt(args.getArgument(0));
            int y = Integer.parseInt(args.getArgument(1));
            int z = Integer.parseInt(args.getArgument(2));

            if (args.isSenderPlayer()) {
                Player player = args.getSender();
                PlayerWaypoint waypoint = new PlayerWaypoint(
                        "player_waypoint_" + player.getUniqueId().toString(),
                        player,
                        "Waypoint",
                        new Location(player.getWorld(), x, y, z)
                );
                plugin.getWaypointManager().startWaypoint(player, waypoint);
                player.sendMessage(Messages.get("waypoint-started", "%name%", "Waypoint"));
                return;
            }

            if (args.getArguments().length < 4) {
                args.getSender().sendMessage(Util.cc("&cUsage: /waypoint start <x> <y> <z> <player>"));
                return;
            }

            Player target = Bukkit.getPlayer(args.getArgument(3));
            if (target == null) {
                args.getSender().sendMessage(Messages.get("player-not-found", "%player%", args.getArgument(3)));
                return;
            }

            PlayerWaypoint waypoint = new PlayerWaypoint(
                    "player_waypoint_" + target.getUniqueId(),
                    target,
                    "Waypoint",
                    new Location(target.getWorld(), x, y, z)
            );
            plugin.getWaypointManager().startWaypoint(target, waypoint);
            args.getSender().sendMessage(Messages.get("waypoint-started-for", "%player%", target.getName()));
        } catch (NumberFormatException e) {
            args.getSender().sendMessage(Messages.get("invalid-coordinates"));
        }
    }

    @Command(
            name = "waypoint.create",
            senderType = Command.SenderType.PLAYER
    )
    public void createWaypoint(CommandArguments args) {
        Player player = args.getSender();
        new CreateWaypointMenu("Create Waypoint", 3).open(player);
    }

    @Command(
            name = "waypoint.delete",
            senderType = Command.SenderType.PLAYER
    )
    public void deleteWaypoint(CommandArguments args) {
        Player player = args.getSender();
        if (args.getLength() < 1) {
            player.sendMessage(Util.cc("&cUsage: /waypoint delete <identifier>"));
            return;
        }
        String identifier = args.getArgument(0);
        PWaypoint wp = plugin.getDatabase().getPlayerWaypoint(player.getUniqueId(), identifier);
        if (wp == null) {
            player.sendMessage(Messages.get("waypoint-not-found", "%id%", identifier));
            return;
        }
        plugin.getDatabase().deletePlayerWaypoint(player.getUniqueId(), identifier);
        player.sendMessage(Messages.get("waypoint-deleted", "%name%", identifier));
    }

    @Command(
            name = "waypoint.stop",
            senderType = Command.SenderType.PLAYER
    )
    public void stopWaypoint(CommandArguments args) {
        Player player = args.getSender();
        if (plugin.getWaypointManager().hasActiveWaypoint(player)) {
            plugin.getWaypointManager().stopWaypoint(player);
            player.sendMessage(Messages.get("waypoint-stopped"));
        } else {
            player.sendMessage(Messages.get("no-active-waypoint"));
        }
    }

    @Command(
            name = "waypoint.global",
            senderType = Command.SenderType.PLAYER
    )
    public void globalWaypoints(CommandArguments args) {
        Player player = args.getSender();
        List<PWaypoint> global = plugin.getDatabase().getAllWaypoints();
        new GlobalWaypointsMenu(global, 0).open(player);
    }

    @Command(
            name = "waypoints",
            senderType = Command.SenderType.PLAYER
    )
    public void waypoints(CommandArguments args) {
        Player player = args.getSender();
        List<PWaypoint> waypoints = plugin.getDatabase().getAllPlayerWaypoints(player.getUniqueId());
        new WaypointsMenu(waypoints, 0).open(player);
    }
}
