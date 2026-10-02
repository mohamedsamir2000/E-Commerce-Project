package StepDefinitions;

import Context.TestContext;
import Pages.AppPage;
import Pages.LoginPage;
import Utility.ConfigReader;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;

public class NavigationSteps {

    private final TestContext context;

    public NavigationSteps(TestContext context) {
        this.context = context;
    }

    @Given("the user is on the {page} page")
    public void theUserIsOnThePage(AppPage page) {
        if (!context.isOn(page)) {
            context.loginPage().open(ConfigReader.baseUrl() + page.path());
        }
        theUserShouldBeOnThePage(page);
    }

    @When("the user opens the {page} page directly")
    public void theUserOpensThePageDirectly(AppPage page) {
        context.loginPage().open(ConfigReader.baseUrl() + page.path());
    }

    @When("the user refreshes the page")
    public void theUserRefreshesThePage() {
        context.loginPage().refresh();
    }

    @When("the user navigates back in the browser")
    public void theUserNavigatesBack() {
        context.loginPage().navigateBack();
    }

    @Then("the user should be on the {page} page")
    public void theUserShouldBeOnThePage(AppPage page) {
        LoginPage anyPage = context.loginPage();
        if (page == AppPage.LOGIN) {
            Assert.assertTrue(anyPage.waitForUrlToBe(ConfigReader.baseUrl()),
                    "Expected the login page but URL was: " + anyPage.getCurrentUrl());
            Assert.assertTrue(anyPage.isDisplayed(), "Login form is not displayed");
            return;
        }
        Assert.assertTrue(anyPage.waitForUrlToContain("/" + page.path()),
                "Expected the " + page + " page but URL was: " + anyPage.getCurrentUrl());
        if (page.title() != null) {
            Assert.assertEquals(anyPage.getPageTitle(), page.title(), "Unexpected page title");
        }
    }

    @Then("the current URL should contain {string}")
    public void theCurrentUrlShouldContain(String fragment) {
        Assert.assertTrue(context.loginPage().waitForUrlToContain(fragment),
                "Expected URL to contain '" + fragment + "' but was: " + context.getDriver().getCurrentUrl());
    }
}
