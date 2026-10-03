package Runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

/**
 * Runs every feature file under src/test/resources/features, skipping the journeys that document the
 * site's intentional bugs (@known_issue).
 * Override the filter with tags, e.g.: mvn test -Dcucumber.filter.tags="@known_issue"
 */
@CucumberOptions(
        features = "src/test/resources/features",
        glue = {"StepDefinitions", "Hooks"},
        tags = "not @known_issue",
        plugin = {
                "pretty",
                "html:target/cucumber-reports/cucumber.html",
                "json:target/cucumber-reports/cucumber.json",
                "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm",
                "Utility.ScreenRecorder"
        },
        monochrome = true
)
public class TestRunner extends AbstractTestNGCucumberTests {
}
