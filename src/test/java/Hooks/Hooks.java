package Hooks;

import Utility.ConfigReader;
import Utility.DriverFactory;
import Utility.ScreenRecorder;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

public class Hooks {

    @Before
    public void setUp() {
        WebDriver driver = DriverFactory.initDriver();
        ScreenRecorder.start(driver);
        driver.get(ConfigReader.baseUrl());
    }

    @After
    public void tearDown(Scenario scenario) {
        WebDriver driver = DriverFactory.getDriver();
        try {
            attachScreenshot(scenario, driver);
            attachVideo(scenario);
        } finally {
            DriverFactory.quitDriver();
        }
    }

    /** Screenshot of the final page: on failure, or for every scenario with -Dscreenshot=all. */
    private void attachScreenshot(Scenario scenario, WebDriver driver) {
        boolean wanted = scenario.isFailed() || "all".equals(ConfigReader.screenshotMode());
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
        String mode = ConfigReader.videoMode();
        boolean keep = "all".equals(mode) || ("failed".equals(mode) && scenario.isFailed());
        byte[] video = ScreenRecorder.stop(scenario.getName(), keep);
        if (video != null) {
            scenario.attach(video, "video/mp4", "Screen recording - " + scenario.getName());
        }
    }
}
