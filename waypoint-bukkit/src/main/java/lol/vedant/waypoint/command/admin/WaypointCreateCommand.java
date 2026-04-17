package lol.vedant.waypoint.command.admin;

import lol.vedant.waypoint.Waypoint;
import me.despical.commandframework.CommandArguments;
import me.despical.commandframework.annotations.Command;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class WaypointCreateCommand {

    Waypoint plugin = Waypoint.getInstance();

    @Command(
            name="waypoint-admin.admin.create",
            permission = "waypoint.admin.create",
            senderType = Command.SenderType.PLAYER
    )
    public void createWaypoint(CommandArguments args) {
        Player player = args.getSender();

        if(args.getLength() < 1) {
            player.sendMessage("Usage: /waypoint-admin create <waypoint-id>");
            return;
        }

        Location loc = player.getLocation();

        plugin.getWaypointManager().createWaypoint(player, args.getArgument(0));



    }
}
