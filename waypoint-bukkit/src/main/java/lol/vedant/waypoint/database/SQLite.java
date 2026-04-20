package lol.vedant.waypoint.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lol.vedant.waypoint.Waypoint;
import lol.vedant.waypoint.api.database.Database;
import lol.vedant.waypoint.api.database.SQLiteUtils;
import lol.vedant.waypoint.api.waypoint.PWaypoint;

import java.nio.file.Path;
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

        //Table create statements here

        dataSource = new HikariDataSource(config);
        this.utils = new SQLiteUtils(dataSource);


    }

    @Override
    public PWaypoint getPlayerWaypoint(UUID player, String identifier) {
        return null;
    }

    @Override
    public void createPlayerWaypoint(UUID player, PWaypoint waypoint) {

    }

    @Override
    public void deletePlayerWaypoint(UUID player, String identifier) {

    }

    @Override
    public PWaypoint getWaypoint(String identifier) {
        return null;
    }

    @Override
    public void createWaypoint(PWaypoint waypoint) {

    }

    @Override
    public void deleteWaypoint(String identifier) {

    }


}
