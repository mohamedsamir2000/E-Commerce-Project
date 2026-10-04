package com.core.runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

/**
 * Runs every feature file under src/test/java/com/core/features, skipping the journeys that document the
 * site's intentional bugs (@known_issue). Started by src/test/resources/xmlSuites/testng.xml (mvn test).
 * Override the filter with tags, e.g.: mvn test -Dcucumber.filter.tags="@Checkout"
 */
@CucumberOptions(
        features = "src/test/java/com/core/features",
        glue = "com.core.stepdef",
        tags = "not @known_issue",
        plugin = {
                "pretty",
                "html:target/cucumber/report.html",
                "json:target/cucumber/results/cucumber.json",
                "junit:target/cucumber/result.xml",
                "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm",
                "com.core.utils.ScreenRecorder"
        },
        monochrome = true
)
public class MyRunnerTest extends AbstractTestNGCucumberTests {
}
