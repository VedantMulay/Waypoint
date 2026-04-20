package lol.vedant.waypoint.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lol.vedant.waypoint.Waypoint;
import lol.vedant.waypoint.api.database.Database;
import lol.vedant.waypoint.api.database.SQLiteUtils;
import lol.vedant.waypoint.api.waypoint.PWaypoint;
import lol.vedant.waypoint.util.Util;
import lol.vedant.waypoint.waypoint.PlayerWaypoint;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.nio.file.Path;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SQLite implements Database {

    Path databaseFile = Waypoint.getInstance().getDataFolder().toPath();
    SQLiteUtils utils;
    HikariDataSource dataSource;

    @Override
    public void init() {
        HikariConfig config = new HikariConfig();

        config.setJdbcUrl("jdbc:sqlite:" + databaseFile.toAbsolutePath() + "/data.db");
        config.setDriverClassName("org.sqlite.JDBC");
        config.setMaximumPoolSize(1);
        config.setPoolName("Waypoint-Pool");

        String player_waypoints = "CREATE TABLE IF NOT EXISTS player_waypoints (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "uuid VARCHAR(36)," +
                "identifier VARCHAR(32)," +
                "name VARCHAR(64)," +
                "location VARCHAR(255)," +
                "create_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP)";

        String global_waypoints = "CREATE TABLE IF NOT EXISTS global_waypoints(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "identifier VARCHAR(32)," +
                "name VARCHAR(64)," +
                "location VARCHAR(255)," +
                "create_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "created_by VARCHAR(16))";

        dataSource = new HikariDataSource(config);
        this.utils = new SQLiteUtils(dataSource);


    }

    @Override
    public PWaypoint getPlayerWaypoint(UUID player, String identifier) {
        String sql = "SELECT * FROM player_waypoints WHERE identifier=? AND uuid=?";
        Player p = (Player) Bukkit.getOfflinePlayer(player);
        utils.query(sql, rs -> {
            try {
                if(rs.next()) {
                    return new PlayerWaypoint(
                            rs.getString("identifier"),
                            p,
                            rs.getString("name"),
                            Util.fromString(rs.getString("location"))
                    );
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        }, identifier);
        return null;
    }

    @Override
    public List<PWaypoint> getAllPlayerWaypoints(UUID player) {
        String sql = "SELECT * FROM player_waypoints WHERE uuid=?";
        List<PWaypoint> waypoints = new ArrayList<>();
        utils.query(sql, rs -> {
            try {
                while (rs.next()) {
                    PlayerWaypoint wp = new PlayerWaypoint(
                            rs.getString("identifier"),
                            rs.getString("name"),
                            Util.fromString(rs.getString("location"))
                    );
                    waypoints.add(wp);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            return waypoints;
        }, player.toString());
        return waypoints;
    }

    @Override
    public void createPlayerWaypoint(UUID player, PWaypoint waypoint) {
        String sql = "INSERT INTO player_waypoints (identifier, name, location, uuid) VALUES (?, ?, ?, ?)";
        utils.update(sql, waypoint.getIdentifier(), waypoint.getName(), Util.fromLocation(waypoint.getLocation()), player.toString());
    }

    @Override
    public void deletePlayerWaypoint(UUID player, String identifier) {
        String sql = "DELETE FROM player_waypoints WHERE identifier=? AND uuid=?";
        utils.update(sql, identifier, player.toString());
    }

    @Override
    public PWaypoint getWaypoint(String identifier) {
        String sql = "SELECT * FROM global_waypoints WHERE identifier=?";
        utils.query(sql, rs -> {
            try {
                if(rs.next()) {
                    return new PlayerWaypoint(
                            identifier,
                            rs.getString("name"),
                            Util.fromString(rs.getString("location"))
                    );
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            return null;
        }, identifier);

        return null;
    }


    @Override
    public void createWaypoint(PWaypoint waypoint) {
        String sql = "INSERT INTO global_waypoints (name, identifier, location) VALUES (?, ?, ?)";
        utils.update(sql, waypoint.getName(), waypoint.getIdentifier(), Util.fromLocation(waypoint.getLocation()));
    }

    @Override
    public void deleteWaypoint(String identifier) {
        String sql = "DELETE FROM global_waypoints WHERE identifier=?";
        utils.update(sql, identifier);
    }


}
