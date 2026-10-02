package StepDefinitions;

import Context.TestContext;
import Pages.LoginPage;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import org.testng.Assert;

public class CommonSteps {

    private final TestContext context;

    public CommonSteps(TestContext context) {
        this.context = context;
    }

    @Given("the user is logged in as {string} with password {string}")
    public void theUserIsLoggedInAs(String username, String password) {
        LoginPage loginPage = context.getLoginPage();
        loginPage.login(username, password);
        context.setCurrentUsername(username);
        Assert.assertTrue(loginPage.waitForUrlToContain("inventory.html"),
                "Login failed for user: " + username);
    }

    @Then("the current URL should be {string}")
    public void theCurrentUrlShouldBe(String expectedUrl) {
        context.getLoginPage().waitForUrlToBe(expectedUrl);
        Assert.assertEquals(context.getDriver().getCurrentUrl(), expectedUrl);
    }

    @Then("the current URL should contain {string}")
    public void theCurrentUrlShouldContain(String expectedFragment) {
        Assert.assertTrue(context.getLoginPage().waitForUrlToContain(expectedFragment),
                "Expected URL to contain '" + expectedFragment + "' but was: " + context.getDriver().getCurrentUrl());
    }
}
