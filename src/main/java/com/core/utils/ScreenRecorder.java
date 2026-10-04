package com.core.utils;

import com.core.TestEnvConfig;
import io.cucumber.plugin.ConcurrentEventListener;
import io.cucumber.plugin.event.EventPublisher;
import io.cucumber.plugin.event.PickleStepTestStep;
import io.cucumber.plugin.event.Status;
import io.cucumber.plugin.event.TestStepFinished;
import io.cucumber.plugin.event.TestStepStarted;
import org.jcodec.api.awt.AWTSequenceEncoder;
import org.openqa.selenium.HasCapabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.json.Json;

import javax.imageio.ImageIO;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.WebSocket;
import java.nio.file.Files;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Screen recording of every scenario.
 * <p>
 * While the scenario runs, Chrome streams the browser tab as a live video (Chrome DevTools
 * "Page.startScreencast", over Chrome's own DevTools WebSocket, so it works headless and with any Chrome
 * version). When the scenario ends {@link #stop(String)} turns the stream into an MP4, writing the step
 * that was running in a caption bar at the bottom (green = passed, red = failed).
 * <p>
 * Registered as a Cucumber plugin in the runner to follow the steps; {@code Hooks} starts and stops it.
 * One recording per thread, which matches the one-driver-per-thread {@link DriverFactory}.
 */
public class ScreenRecorder implements ConcurrentEventListener {

    private static final int FRAMES_PER_SECOND = 5;
    private static final int MAX_WIDTH = 1280;
    private static final int CAPTION_HEIGHT = 56;
    /** How long the last frame (the end result) stays on screen. */
    private static final int HOLD_LAST_FRAME_MILLIS = 2000;
    private static final File VIDEO_DIR = new File("target/videos");
    private static final Json JSON = new Json();

    private static final ThreadLocal<Recording> CURRENT = new ThreadLocal<>();

    // ---------- Cucumber plugin: follows the steps for the captions ----------

    @Override
    public void setEventPublisher(EventPublisher publisher) {
        publisher.registerHandlerFor(TestStepStarted.class, event -> {
            Recording recording = CURRENT.get();
            if (recording != null && event.getTestStep() instanceof PickleStepTestStep step) {
                recording.stepStarted(step.getStep().getKeyword().trim() + " " + step.getStep().getText());
            }
        });
        publisher.registerHandlerFor(TestStepFinished.class, event -> {
            Recording recording = CURRENT.get();
            if (recording != null && event.getTestStep() instanceof PickleStepTestStep) {
                recording.stepFinished(event.getResult().getStatus());
            }
        });
    }

    // ---------- Called from Hooks ----------

    /** Starts recording the browser of the current thread. Never fails the test. */
    public static void start(WebDriver driver) {
        CURRENT.remove();
        if ("off".equals(TestEnvConfig.videoMode())) {
            return;
        }
        try {
            CURRENT.set(Recording.start(driver));
        } catch (Exception e) {
            System.out.println("Screen recording could not start: " + e);
        }
    }

    /**
     * Stops the recording and, when {@code keep} is true, encodes it to target/videos/&lt;name&gt;.mp4.
     *
     * @return the MP4 bytes, or null when there is nothing to keep
     */
    public static byte[] stop(String scenarioName, boolean keep) {
        Recording recording = CURRENT.get();
        CURRENT.remove();
        if (recording == null) {
            return null;
        }
        recording.close();
        if (!keep) {
            return null;
        }
        try {
            return recording.encode(scenarioName);
        } catch (Exception e) {
            System.out.println("Screen recording could not be written: " + e);
            return null;
        }
    }

    // ---------- One recording ----------

    private record Frame(long time, byte[] jpeg) {
    }

    private static final class Step {
        final long start;
        final String text;
        volatile Status status;

        Step(long start, String text) {
            this.start = start;
            this.text = text;
        }
    }

    private static final class Recording implements WebSocket.Listener {

        private final List<Frame> frames = new ArrayList<>();
        private final List<Step> steps = new ArrayList<>();
        private final StringBuilder message = new StringBuilder();
        private final AtomicInteger nextId = new AtomicInteger(1);
        private WebSocket socket;

        static Recording start(WebDriver driver) throws Exception {
            String pageSocketUrl = pageWebSocketUrl(driver);
            Recording recording = new Recording();
            recording.socket = HttpClient.newHttpClient().newWebSocketBuilder()
                    .buildAsync(URI.create(pageSocketUrl), recording)
                    .get(10, TimeUnit.SECONDS);
            recording.send("Page.startScreencast",
                    Map.of("format", "jpeg", "quality", 70, "maxWidth", MAX_WIDTH, "maxHeight", MAX_WIDTH));
            return recording;
        }

        /** Asks Chrome's DevTools endpoint (goog:chromeOptions.debuggerAddress) for the tab's WebSocket URL. */
        @SuppressWarnings("unchecked")
        private static String pageWebSocketUrl(WebDriver driver) throws Exception {
            Map<String, Object> chromeOptions = (Map<String, Object>) ((HasCapabilities) driver)
                    .getCapabilities().getCapability("goog:chromeOptions");
            String debuggerAddress = (String) chromeOptions.get("debuggerAddress");
            HttpResponse<String> response = HttpClient.newHttpClient().send(
                    HttpRequest.newBuilder(URI.create("http://" + debuggerAddress + "/json/list"))
                            .timeout(Duration.ofSeconds(10)).build(),
                    HttpResponse.BodyHandlers.ofString());
            List<Map<String, Object>> targets = JSON.toType(response.body(), Json.LIST_OF_MAPS_TYPE);
            return targets.stream()
                    .filter(t -> "page".equals(t.get("type")))
                    .map(t -> (String) t.get("webSocketDebuggerUrl"))
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("No browser tab to record"));
        }

        private void send(String method, Map<String, Object> params) {
            String json = JSON.toJson(Map.of("id", nextId.getAndIncrement(), "method", method, "params", params));
            synchronized (this) {
                socket.sendText(json, true).join();
            }
        }

        @Override
        public CompletionStage<?> onText(WebSocket webSocket, CharSequence data, boolean last) {
            message.append(data);
            if (last) {
                String text = message.toString();
                message.setLength(0);
                if (text.contains("\"Page.screencastFrame\"")) {
                    onFrame(text);
                }
            }
            webSocket.request(1);
            return null;
        }

        @SuppressWarnings("unchecked")
        private void onFrame(String text) {
            Map<String, Object> event = JSON.toType(text, Json.MAP_TYPE);
            Map<String, Object> params = (Map<String, Object>) event.get("params");
            byte[] jpeg = Base64.getDecoder().decode((String) params.get("data"));
            synchronized (frames) {
                frames.add(new Frame(System.currentTimeMillis(), jpeg));
            }
            // Chrome sends the next frame only after this one is acknowledged
            send("Page.screencastFrameAck", Map.of("sessionId", params.get("sessionId")));
        }

        void stepStarted(String text) {
            synchronized (steps) {
                steps.add(new Step(System.currentTimeMillis(), text));
            }
        }

        void stepFinished(Status status) {
            synchronized (steps) {
                if (!steps.isEmpty()) {
                    steps.get(steps.size() - 1).status = status;
                }
            }
        }

        void close() {
            try {
                send("Page.stopScreencast", Map.of());
                socket.sendClose(WebSocket.NORMAL_CLOSURE, "done").get(5, TimeUnit.SECONDS);
            } catch (Exception ignored) {
                // the browser may already be gone
            }
        }

        /** Writes the frames at a fixed frame rate, so the video plays at the real speed of the test. */
        byte[] encode(String scenarioName) throws IOException {
            List<Frame> recorded;
            synchronized (frames) {
                recorded = new ArrayList<>(frames);
            }
            if (recorded.isEmpty()) {
                return null;
            }
            VIDEO_DIR.mkdirs();
            File file = new File(VIDEO_DIR, fileName(scenarioName) + ".mp4");

            BufferedImage first = ImageIO.read(new ByteArrayInputStream(recorded.get(0).jpeg()));
            int width = even(first.getWidth());
            int height = even(first.getHeight());

            AWTSequenceEncoder encoder = AWTSequenceEncoder.createSequenceEncoder(file, FRAMES_PER_SECOND);
            long start = recorded.get(0).time();
            long end = recorded.get(recorded.size() - 1).time() + HOLD_LAST_FRAME_MILLIS;
            long tick = 1000L / FRAMES_PER_SECOND;
            int frameIndex = 0;
            BufferedImage page = null;
            for (long t = start; t <= end; t += tick) {
                int latest = frameIndex;
                while (latest + 1 < recorded.size() && recorded.get(latest + 1).time() <= t) {
                    latest++;
                }
                if (page == null || latest != frameIndex) {
                    frameIndex = latest;
                    page = ImageIO.read(new ByteArrayInputStream(recorded.get(frameIndex).jpeg()));
                }
                encoder.encodeImage(compose(page, width, height, stepAt(t)));
            }
            encoder.finish();
            return Files.readAllBytes(file.toPath());
        }

        private Step stepAt(long time) {
            synchronized (steps) {
                Step current = null;
                for (Step step : steps) {
                    if (step.start <= time) {
                        current = step;
                    }
                }
                return current;
            }
        }

        private List<Step> stepsSnapshot() {
            synchronized (steps) {
                return new ArrayList<>(steps);
            }
        }

        /** Draws the page and, under it, the step that is running (numbered, green/red by its result). */
        private BufferedImage compose(BufferedImage page, int width, int height, Step step) {
            BufferedImage frame = new BufferedImage(width, height + CAPTION_HEIGHT, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = frame.createGraphics();
            try {
                g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                g.setColor(Color.WHITE);
                g.fillRect(0, 0, width, height);
                g.drawImage(page, 0, 0, Math.min(width, page.getWidth()), Math.min(height, page.getHeight()), null);

                boolean failed = step != null && step.status != null
                        && step.status != Status.PASSED && step.status != Status.SKIPPED;
                g.setColor(failed ? new Color(0xB3261E) : new Color(0x1E7B34));
                g.fillRect(0, height, width, CAPTION_HEIGHT);
                if (failed) {
                    g.setStroke(new BasicStroke(6));
                    g.drawRect(3, 3, width - 6, height - 6);
                }

                String caption = step == null ? "Starting..."
                        : (failed ? "FAILED  " : "") + (stepsSnapshot().indexOf(step) + 1) + ". " + step.text;
                g.setColor(Color.WHITE);
                g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
                g.drawString(fit(g.getFontMetrics(), caption, width - 40), 20, height + CAPTION_HEIGHT / 2 + 7);
            } finally {
                g.dispose();
            }
            return frame;
        }
    }

    // ---------- Helpers ----------

    private static String fit(FontMetrics metrics, String text, int maxWidth) {
        if (metrics.stringWidth(text) <= maxWidth) {
            return text;
        }
        String shortened = text;
        while (!shortened.isEmpty() && metrics.stringWidth(shortened + "...") > maxWidth) {
            shortened = shortened.substring(0, shortened.length() - 1);
        }
        return shortened + "...";
    }

    private static int even(int value) {
        return value % 2 == 0 ? value : value - 1; // H.264 needs even dimensions
    }

    private static String fileName(String scenarioName) {
        String safe = scenarioName.replaceAll("[^A-Za-z0-9._-]+", "_").replaceAll("_+", "_");
        if (safe.length() > 100) {
            safe = safe.substring(0, 100);
        }
        return safe + "_" + System.currentTimeMillis();
    }
}
