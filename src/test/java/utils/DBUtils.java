package utils;
import java.sql.*;
import java.util.*;
public class DBUtils {
    public static List<Map<String, String>> fetch(String query, Object... parameters) {
        try (Connection connection = DriverManager.getConnection(ConfigReader.read("dbURL"), ConfigReader.read("dbUserName"), ConfigReader.read("dbPassword"));
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setQueryTimeout(Integer.parseInt(ConfigReader.read("dbTimeout")));
            for (int i = 0; i < parameters.length; i++) statement.setObject(i + 1, parameters[i]);
            List<Map<String, String>> rows = new ArrayList<>();
            try (ResultSet results = statement.executeQuery()) {
                ResultSetMetaData metadata = results.getMetaData();
                while (results.next()) {
                    Map<String, String> row = new LinkedHashMap<>();
                    for (int i = 1; i <= metadata.getColumnCount(); i++) row.put(metadata.getColumnLabel(i), results.getString(i));
                    rows.add(row);
                }
            }
            return rows;
        } catch (SQLException e) { throw new IllegalStateException("Database query failed", e); }
    }
}