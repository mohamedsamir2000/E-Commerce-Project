package com.core.stepdef;

import com.core.pages.SwagLabs.AppPage;
import com.core.pages.SwagLabs.LoginPage;
import com.core.utils.TestDataReader;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;

import java.util.Map;

/**
 * Login, logout and the error banner (shared by the login and checkout forms).
 * Users are referred to by their role in testData/&lt;env&gt;/Users.json, e.g. "StandardUser".
 */
public class LoginStepDef {

    private final TestContext context;
    private final ReusableStepDef reusable;
    private final LoginPage loginPage;

    public LoginStepDef(TestContext context, ReusableStepDef reusable) {
        this.context = context;
        this.reusable = reusable;
        this.loginPage = context.loginPage();
    }

    @Given("Customer Open the store")
    public void customerOpenTheStore() {
        reusable.customerOpenScreen("Login");
    }

    /** Opens the store, logs in with the user of this role and checks the products screen is shown. */
    @Given("Customer Login as a {string}")
    public void customerLoginAsA(String role) {
        if (!context.isOn(AppPage.LOGIN)) {
            customerOpenTheStore();
        }
        customerTryToLoginAsA(role);
        reusable.verifyScreenIsDisplayed("Products");
    }

    /** Logs in from the login screen without checking the result (e.g. a locked out user). */
    @When("Customer try to login as a {string}")
    public void customerTryToLoginAsA(String role) {
        Map<String, String> user = TestDataReader.getEntry("Users", role);
        loginPage.login(user.get("username"), user.get("password"));
    }

    @When("Customer try to login with username {string} and password {string}")
    public void customerTryToLoginWith(String username, String password) {
        loginPage.login(TestDataReader.resolve(username), TestDataReader.resolve(password));
    }

    @When("Customer Logout")
    public void customerLogout() {
        context.sideMenu().select("Logout");
        reusable.verifyScreenIsDisplayed("Login");
    }

    // ---------- Error banner ----------

    @Then("Verify error message {string}")
    public void verifyErrorMessage(String expected) {
        Assert.assertEquals(loginPage.getErrorMessage(), TestDataReader.resolve(expected), "Error message");
    }

    @When("Close the error message")
    public void closeTheErrorMessage() {
        loginPage.dismissErrorMessage();
    }

    @Then("Verify no error message is displayed")
    public void verifyNoErrorMessageIsDisplayed() {
        Assert.assertFalse(loginPage.isErrorDisplayed(), "An error message is still displayed");
    }
}
