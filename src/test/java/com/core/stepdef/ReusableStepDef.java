package com.core.stepdef;

import com.core.TestEnvConfig;
import com.core.pages.SwagLabs.AppPage;
import com.core.pages.SwagLabs.LoginPage;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;

/**
 * Steps used by every feature: open a screen directly, check which screen is shown, refresh, URL checks.
 * Screen names: Login, Products, Product Details, Cart, Checkout Information, Checkout Overview,
 * Checkout Complete.
 */
public class ReusableStepDef {

    private final TestContext context;

    public ReusableStepDef(TestContext context) {
        this.context = context;
    }

    /** Opens the screen by its URL, like typing it in the address bar. */
    @When("Customer Open screen {string}")
    public void customerOpenScreen(String screen) {
        context.loginPage().open(TestEnvConfig.baseUrl() + AppPage.fromName(screen).path());
    }

    @Then("Verify screen {string} is displayed")
    public void verifyScreenIsDisplayed(String screen) {
        AppPage page = AppPage.fromName(screen);
        LoginPage anyPage = context.loginPage();
        if (page == AppPage.LOGIN) {
            Assert.assertTrue(anyPage.waitForUrlToBe(TestEnvConfig.baseUrl()),
                    "Expected the Login screen but URL was: " + anyPage.getCurrentUrl());
            Assert.assertTrue(anyPage.isDisplayed(), "Login form is not displayed");
            return;
        }
        Assert.assertTrue(anyPage.waitForUrlToContain("/" + page.path()),
                "Expected the " + screen + " screen but URL was: " + anyPage.getCurrentUrl());
        if (page.title() != null) {
            Assert.assertEquals(anyPage.getPageTitle(), page.title(), "Screen title");
        }
    }

    @When("Refresh the page")
    public void refreshThePage() {
        context.loginPage().refresh();
    }

    @Then("Verify the URL contains {string}")
    public void verifyTheUrlContains(String fragment) {
        Assert.assertTrue(context.loginPage().waitForUrlToContain(fragment),
                "Expected URL to contain '" + fragment + "' but was: " + context.getDriver().getCurrentUrl());
    }
}
