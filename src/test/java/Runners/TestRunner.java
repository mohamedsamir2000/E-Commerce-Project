package Runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

/**
 * Runs every feature file under src/test/resources/features.
 * Filter scenarios with tags, e.g.: mvn test -Dcucumber.filter.tags="@Sort"
 */
@CucumberOptions(
        features = "src/test/resources/features",
        glue = {"StepDefinitions", "Hooks"},
        plugin = {
                "pretty",
                "html:target/cucumber-reports/cucumber.html",
                "json:target/cucumber-reports/cucumber.json",
                "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm"
        },
        monochrome = true
)
public class TestRunner extends AbstractTestNGCucumberTests {
}
