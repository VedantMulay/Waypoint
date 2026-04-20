package lol.vedant.waypoint.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lol.vedant.waypoint.api.database.Database;
import lol.vedant.waypoint.api.database.DatabaseSettings;
import lol.vedant.waypoint.api.database.MySQLUtils;
import lol.vedant.waypoint.api.waypoint.PWaypoint;
import lol.vedant.waypoint.util.Util;
import lol.vedant.waypoint.waypoint.PlayerWaypoint;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class MySQL implements Database {

    private HikariDataSource dataSource;
    private final String host;
    private final String database;
    private final String user;
    private final String pass;
    private final int port;
    private final boolean ssl;
    private final boolean certificateVerification;
    private final int poolSize;
    private final int maxLifetime;
    private MySQLUtils utils;

    @SuppressWarnings("ALL")
    public MySQL(DatabaseSettings settings) {
        this.host = settings.getHost();
        this.database = settings.getDatabase();
        this.user = settings.getUser();
        this.pass = settings.getPassword();
        this.port = settings.getPort();
        this.ssl = settings.isSsl();
        this.certificateVerification = settings.isCertificateVerification();
        this.poolSize = settings.getPoolSize();
        this.maxLifetime = settings.getMaxLifetime();
    }

    @Override
    public void init() {
        HikariConfig hikariConfig = new HikariConfig();

        hikariConfig.setPoolName("Waypoint-Pool");

        hikariConfig.setMaximumPoolSize(poolSize);
        hikariConfig.setMaxLifetime(maxLifetime * 1000L);

        hikariConfig.setJdbcUrl("jdbc:mysql://" + host + ":" + port + "/" + database);

        hikariConfig.setUsername(user);
        hikariConfig.setPassword(pass);

        hikariConfig.addDataSourceProperty("useSSL", String.valueOf(ssl));
        if (!certificateVerification) {
            hikariConfig.addDataSourceProperty("verifyServerCertificate", String.valueOf(false));
        }

        hikariConfig.addDataSourceProperty("characterEncoding", "utf8");
        hikariConfig.addDataSourceProperty("encoding", "UTF-8");
        hikariConfig.addDataSourceProperty("useUnicode", "true");

        hikariConfig.addDataSourceProperty("rewriteBatchedStatements", "true");
        hikariConfig.addDataSourceProperty("jdbcCompliantTruncation", "false");

        hikariConfig.addDataSourceProperty("cachePrepStmts", "true");
        hikariConfig.addDataSourceProperty("prepStmtCacheSize", "275");
        hikariConfig.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

        // Recover if connection gets interrupted
        hikariConfig.addDataSourceProperty("socketTimeout", String.valueOf(TimeUnit.SECONDS.toMillis(30)));

        dataSource = new HikariDataSource(hikariConfig);
        this.utils = new MySQLUtils(dataSource);

        String player_waypoints = "CREATE TABLE IF NOT EXISTS player_waypoints (" +
                "id INT AUTO_INCREMENT PRIMARY KEY," +
                "uuid VARCHAR(36)," +
                "identifier VARCHAR(32)," +
                "name VARCHAR(64)," +
                "location VARCHAR(255)," +
                "create_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "UNIQUE KEY unique_player_waypoint (uuid, identifier))";

        String global_waypoints = "CREATE TABLE IF NOT EXISTS global_waypoints(" +
                "id INT AUTO_INCREMENT PRIMARY KEY," +
                "identifier VARCHAR(32)," +
                "name VARCHAR(64)," +
                "location VARCHAR(255)," +
                "create_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "created_by VARCHAR(16))";


        try (Connection connection = dataSource.getConnection()) {
            connection.prepareStatement(player_waypoints).execute();
            connection.prepareStatement(global_waypoints).execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }

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
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            return List.of();
        }, player.toString());
        return List.of();
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
