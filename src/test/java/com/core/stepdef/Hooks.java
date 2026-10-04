package com.core.stepdef;

import com.core.TestEnvConfig;
import com.core.utils.DriverFactory;
import com.core.utils.ScreenRecorder;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;

/**
 * Before each scenario: opens a fresh browser on the store and starts the screen recording.
 * After each scenario: screenshot (on failure), screen recording, log line, browser closed.
 */
public class Hooks {

    private static final Logger LOG = LogManager.getLogger(Hooks.class);

    @Before
    public void setUp(Scenario scenario) {
        LOG.info("START  [{}] {} {}", TestEnvConfig.env(), scenario.getName(), scenario.getSourceTagNames());
        WebDriver driver = DriverFactory.initDriver();
        ScreenRecorder.start(driver);
        driver.get(TestEnvConfig.baseUrl());
    }

    @After
    public void tearDown(Scenario scenario) {
        WebDriver driver = DriverFactory.getDriver();
        LOG.info("{} {}", scenario.getStatus(), scenario.getName());
        try {
            attachScreenshot(scenario, driver);
            attachVideo(scenario);
        } finally {
            DriverFactory.quitDriver();
        }
    }

    /** Screenshot of the final page: on failure, or for every scenario with -Dscreenshot=all. */
    private void attachScreenshot(Scenario scenario, WebDriver driver) {
        boolean wanted = scenario.isFailed() || "all".equals(TestEnvConfig.screenshotMode());
        if (!wanted || driver == null) {
            return;
        }
        try {
            byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            scenario.attach(screenshot, "image/png", "Screenshot - " + scenario.getName());
        } catch (Exception e) {
            System.out.println("Could not take the screenshot: " + e.getClass().getSimpleName());
        }
    }

    /** Screen recording of the scenario (see ScreenRecorder): -Dvideo=all | failed | off. */
    private void attachVideo(Scenario scenario) {
        String mode = TestEnvConfig.videoMode();
        boolean keep = "all".equals(mode) || ("failed".equals(mode) && scenario.isFailed());
        byte[] video = ScreenRecorder.stop(scenario.getName(), keep);
        if (video != null) {
            scenario.attach(video, "video/mp4", "Screen recording - " + scenario.getName());
        }
    }
}
