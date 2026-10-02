package StepDefinitions;

import Context.TestContext;
import Pages.AppPage;
import Utility.ConfigReader;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;

public class LoginSteps {

    private final TestContext context;
    private final NavigationSteps navigation;

    public LoginSteps(TestContext context, NavigationSteps navigation) {
        this.context = context;
        this.navigation = navigation;
    }

    @Given("the user is logged in as {string}")
    public void theUserIsLoggedInAs(String username) {
        theUserIsLoggedInAs(username, ConfigReader.defaultPassword());
    }

    @Given("the user is logged in as {string} with password {string}")
    public void theUserIsLoggedInAs(String username, String password) {
        navigation.theUserIsOnThePage(AppPage.LOGIN);
        theUserLogsInAs(username, password);
        navigation.theUserShouldBeOnThePage(AppPage.PRODUCTS);
    }

    @When("the user logs in as {string}")
    public void theUserLogsInAs(String username) {
        theUserLogsInAs(username, ConfigReader.defaultPassword());
    }

    @When("the user logs in as {string} with password {string}")
    public void theUserLogsInAs(String username, String password) {
        context.loginPage().login(username, password);
    }

    @When("the user logs out")
    public void theUserLogsOut() {
        if (!context.sideMenu().isOpen()) {
            context.sideMenu().open();
        }
        context.sideMenu().select("Logout");
    }

    // ---------- Error messages (login and checkout forms share the same error banner) ----------

    @Then("the error message {string} should be displayed")
    public void theErrorMessageShouldBeDisplayed(String expectedMessage) {
        Assert.assertEquals(context.loginPage().getErrorMessage(), expectedMessage);
    }

    @When("the user dismisses the error message")
    public void theUserDismissesTheErrorMessage() {
        context.loginPage().dismissErrorMessage();
    }

    @Then("no error message should be displayed")
    public void noErrorMessageShouldBeDisplayed() {
        Assert.assertFalse(context.loginPage().isErrorDisplayed(), "An error message is still displayed");
    }
}
