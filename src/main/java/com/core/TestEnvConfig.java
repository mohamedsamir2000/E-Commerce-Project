package com.core;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Test environment configuration.
 * <p>
 * General settings come from src/main/resources/config.properties and the URLs of each environment
 * (sit, uat, ...) from src/main/resources/environments.properties. A JVM system property with the same
 * key wins, so any value can be overridden on the command line:
 * <pre>
 *   mvn test -Denv=uat -Dheadless=true -Dvideo=failed
 * </pre>
 */
public final class TestEnvConfig {

    private static final Properties CONFIG = load("config.properties");
    private static final Properties ENVIRONMENTS = load("environments.properties");

    private TestEnvConfig() {
    }

    private static Properties load(String resource) {
        Properties properties = new Properties();
        try (InputStream in = TestEnvConfig.class.getClassLoader().getResourceAsStream(resource)) {
            if (in != null) {
                properties.load(in);
            }
        } catch (IOException e) {
            throw new IllegalStateException("Could not read " + resource, e);
        }
        return properties;
    }

    public static String get(String key) {
        String value = System.getProperty(key, CONFIG.getProperty(key));
        if (value == null) {
            throw new IllegalStateException("Missing configuration value: " + key);
        }
        return value.trim();
    }

    /** The environment to test (sit, uat, ...): selects the URL and the testData/&lt;env&gt; folder. */
    public static String env() {
        return get("env").toLowerCase();
    }

    public static String baseUrl() {
        String key = env() + ".base.url";
        String url = System.getProperty("base.url", ENVIRONMENTS.getProperty(key));
        if (url == null) {
            throw new IllegalStateException("No base URL for environment '" + env() + "' (" + key + ")");
        }
        url = url.trim();
        return url.endsWith("/") ? url : url + "/";
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
