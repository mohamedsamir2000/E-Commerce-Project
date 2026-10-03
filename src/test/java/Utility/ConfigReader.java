package Utility;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Reads src/test/resources/config.properties. A JVM system property with the same key wins,
 * so values can be overridden with -Dkey=value.
 */
public final class ConfigReader {

    private static final Properties PROPERTIES = load();

    private ConfigReader() {
    }

    private static Properties load() {
        Properties properties = new Properties();
        try (InputStream in = ConfigReader.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (in != null) {
                properties.load(in);
            }
        } catch (IOException e) {
            throw new IllegalStateException("Could not read config.properties", e);
        }
        return properties;
    }

    public static String get(String key) {
        String value = System.getProperty(key, PROPERTIES.getProperty(key));
        if (value == null) {
            throw new IllegalStateException("Missing configuration value: " + key);
        }
        return value.trim();
    }

    public static String baseUrl() {
        String url = get("base.url");
        return url.endsWith("/") ? url : url + "/";
    }

    public static String defaultPassword() {
        return get("default.password");
    }

    public static boolean headless() {
        return Boolean.parseBoolean(get("headless"));
    }

    public static int timeoutSeconds() {
        return Integer.parseInt(get("timeout.seconds"));
    }

    /** Screen recording mode: all, failed or off. */
    public static String videoMode() {
        return get("video").toLowerCase();
    }

    /** Screenshot mode: all or failed. */
    public static String screenshotMode() {
        return get("screenshot").toLowerCase();
    }
}
