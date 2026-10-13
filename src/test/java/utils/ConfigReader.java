package utils;
import java.io.*;
import java.util.*;
public class ConfigReader {
    public static String read(String key) {
        String value = System.getProperty(key);
        if (value == null) value = System.getenv("HRMS_" + key.replaceAll("([a-z])([A-Z])", "$1_$2").toUpperCase(Locale.ROOT));
        if (value == null) {
            Properties properties = load(Constants.CONFIG_FILE_PATH);
            File local = new File(Constants.CONFIG_FILE_PATH.replace("config.properties", "config.local.properties"));
            if (local.isFile()) properties.putAll(load(local.getPath()));
            value = properties.getProperty(key);
        }
        if (value == null || value.trim().isEmpty()) throw new IllegalArgumentException("Missing configuration: " + key);
        return value;
    }
    public static String read(String key, String path) {
        String value = load(path).getProperty(key);
        if (value == null || value.trim().isEmpty()) throw new IllegalArgumentException("Missing configuration: " + key);
        return value;
    }
    private static Properties load(String path) {
        Properties properties = new Properties();
        try (InputStream input = new FileInputStream(path)) {
            properties.load(input);
            return properties;
        } catch (IOException e) { throw new IllegalStateException("Cannot read configuration: " + path, e); }
    }
}