package lol.vedant.waypoint.api.database;

import java.sql.ResultSet;
import java.util.function.Function;

public interface StorageHelper {

    int update(String sql, Object... params);

    <T> T query(String sql, Function<ResultSet, T> mapper, Object... params);
}