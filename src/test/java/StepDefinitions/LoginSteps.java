package StepDefinitions;

import Context.TestContext;
import Pages.LoginPage;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;

public class LoginSteps {

    private final TestContext context;
    private final LoginPage loginPage;

    public LoginSteps(TestContext context) {
        this.context = context;
        this.loginPage = context.getLoginPage();
    }

    @Given("the user is on the login page")
    public void theUserIsOnTheLoginPage() {
        Assert.assertTrue(loginPage.isLoginButtonDisplayed(), "Login page is not displayed");
    }

    @When("the user logs in with username {string} and password {string}")
    public void theUserLogsInWith(String username, String password) {
        System.out.println("Testing with: " + username + " | " + password);
        context.setCurrentUsername(username);
        loginPage.login(username, password);
    }

    @When("the user logs out")
    public void theUserLogsOut() {
        context.getHomePage().logout();
    }

    @Then("the user should be on the login page")
    public void theUserShouldBeOnTheLoginPage() {
        Assert.assertTrue(loginPage.isLoginButtonDisplayed(), "Logout failed! Login page is not displayed");
    }
}
