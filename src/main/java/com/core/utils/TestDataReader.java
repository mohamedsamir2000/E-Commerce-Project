package com.core.utils;

import com.core.TestEnvConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.json.Json;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

/**
 * Test data of the current environment, from src/test/resources/testData/&lt;env&gt;/&lt;File&gt;.json.
 * <p>
 * Feature files point to a value with a {@code "File//Key"} reference, e.g. {@code "StoreData//OrderTotal"}:
 * <ul>
 *   <li>{@link #save(String, String)} keeps a value found during the run (an order total, a reference...)
 *       so later steps can use it; saved values are also written to target/testData/&lt;env&gt;/&lt;File&gt;.json</li>
 *   <li>{@link #resolve(String)} turns a reference into its value (saved during the run first, then the
 *       JSON file); any other text is returned unchanged</li>
 * </ul>
 */
public final class TestDataReader {

    private static final Logger LOG = LogManager.getLogger(TestDataReader.class);
    private static final Pattern REFERENCE = Pattern.compile("^[A-Za-z]\\w*//\\w+$");
    private static final Json JSON = new Json();

    /** Values saved during the run: file -> key -> value. */
    private static final Map<String, Map<String, String>> SAVED = new ConcurrentHashMap<>();

    private TestDataReader() {
    }

    /** True when the text is a "File//Key" reference. */
    public static boolean isReference(String text) {
        return text != null && REFERENCE.matcher(text.trim()).matches();
    }

    /** The value of a "File//Key" reference, or the text itself when it is not a reference. */
    public static String resolve(String text) {
        if (!isReference(text)) {
            return text == null ? "" : text;
        }
        String[] parts = text.trim().split("//");
        String saved = SAVED.getOrDefault(parts[0], Map.of()).get(parts[1]);
        if (saved != null) {
            return saved;
        }
        Object value = readFile(parts[0]).get(parts[1]);
        if (value == null) {
            throw new IllegalArgumentException("No test data for " + text + " in " + location(parts[0]));
        }
        return String.valueOf(value);
    }

    /** Saves a value under a "File//Key" reference for the rest of the run. */
    public static void save(String reference, String value) {
        if (!isReference(reference)) {
            throw new IllegalArgumentException("Expected a \"File//Key\" reference but got: " + reference);
        }
        String[] parts = reference.trim().split("//");
        Map<String, String> file = SAVED.computeIfAbsent(parts[0], f -> new ConcurrentHashMap<>());
        file.put(parts[1], value);
        LOG.info("Saved {} = {}", reference, value);
        writeSavedFile(parts[0], file);
    }

    /** One entry of a JSON file whose values are objects, e.g. the "StandardUser" entry of Users.json. */
    @SuppressWarnings("unchecked")
    public static Map<String, String> getEntry(String file, String name) {
        Object entry = readFile(file).get(name);
        if (!(entry instanceof Map)) {
            throw new IllegalArgumentException("No entry '" + name + "' in " + location(file));
        }
        Map<String, String> values = new LinkedHashMap<>();
        ((Map<String, Object>) entry).forEach((k, v) -> values.put(k, String.valueOf(v)));
        return values;
    }

    private static Map<String, Object> readFile(String file) {
        String resource = location(file);
        try (InputStream in = TestDataReader.class.getClassLoader().getResourceAsStream(resource)) {
            if (in == null) {
                return Map.of();
            }
            return JSON.toType(new String(in.readAllBytes(), StandardCharsets.UTF_8), Json.MAP_TYPE);
        } catch (IOException e) {
            throw new UncheckedIOException("Could not read " + resource, e);
        }
    }

    private static String location(String file) {
        return "testData/" + TestEnvConfig.env() + "/" + file + ".json";
    }

    private static void writeSavedFile(String file, Map<String, String> values) {
        try {
            Path path = Path.of("target", location(file));
            Files.createDirectories(path.getParent());
            Files.writeString(path, JSON.toJson(new LinkedHashMap<>(values)));
        } catch (IOException e) {
            LOG.warn("Could not write saved test data: {}", e.getMessage());
        }
    }
}
