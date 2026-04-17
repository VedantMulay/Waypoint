package lol.vedant.waypoint.command;

import lol.vedant.waypoint.waypoint.PlayerWaypoint;
import me.despical.commandframework.CommandArguments;
import me.despical.commandframework.annotations.Command;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class WaypointCommand {

    @Command(
            name = "waypoint",
            senderType = Command.SenderType.BOTH
    )
    public void waypoint(CommandArguments args) {

        if (args.getArguments().length < 3) {
            args.getSender().sendMessage("Invalid arguments");
            return;
        }

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
            waypoint.start();
            player.sendMessage("Waypoint started to " + waypoint.getLocation());
            return;
        }

        if (args.getArguments().length < 4) {
            args.getSender().sendMessage("Usage: /waypoint x y z <player>");
            return;
        }

        Player target = Bukkit.getPlayer(args.getArgument(3));
        if (target == null) {
            args.getSender().sendMessage("Player not found");
            return;
        }

        PlayerWaypoint waypoint = new PlayerWaypoint(
                "player_waypoint_" + target.getUniqueId(),
                target,
                "Waypoint",
                new Location(target.getWorld(), x, y, z)
        );
        waypoint.start();
        args.getSender().sendMessage("Waypoint started for " + target.getName());
    }
}