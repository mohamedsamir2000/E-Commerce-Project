package StepDefinitions;

import Context.TestContext;
import Pages.Components.Footer;
import Pages.Components.Header;
import Pages.Components.SideMenu;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;

import java.util.List;

public class MenuAndFooterSteps {

    private final TestContext context;
    private final SideMenu sideMenu;
    private final Footer footer;
    private final Header header;

    public MenuAndFooterSteps(TestContext context) {
        this.context = context;
        this.sideMenu = context.sideMenu();
        this.footer = context.footer();
        this.header = context.header();
    }

    // ---------- Side menu ----------

    @When("the user opens the menu")
    public void theUserOpensTheMenu() {
        sideMenu.open();
    }

    @When("the user closes the menu")
    public void theUserClosesTheMenu() {
        sideMenu.close();
    }

    @When("the user selects {string} from the menu")
    public void theUserSelectsFromTheMenu(String item) {
        if (!sideMenu.isOpen()) {
            sideMenu.open();
        }
        sideMenu.select(item);
        if (item.equalsIgnoreCase("Reset App State")) {
            context.expectedCart().clear();
        }
    }

    @When("the user resets the app state")
    public void theUserResetsTheAppState() {
        theUserSelectsFromTheMenu("Reset App State");
    }

    @Then("the menu should be open")
    public void theMenuShouldBeOpen() {
        Assert.assertTrue(sideMenu.isOpen(), "Menu is not open");
    }

    @Then("the menu should be closed")
    public void theMenuShouldBeClosed() {
        Assert.assertFalse(sideMenu.isOpen(), "Menu is still open");
    }

    @Then("the menu should contain the following options:")
    public void theMenuShouldContainTheOptions(List<String> expected) {
        List<String> actual = sideMenu.getItems();
        Assert.assertTrue(actual.containsAll(expected), "Menu options " + actual + " do not include " + expected);
    }

    // ---------- Header ----------

    @Then("the header should show the logo {string}")
    public void theHeaderShouldShowTheLogo(String expected) {
        Assert.assertEquals(header.getLogoText(), expected);
    }

    // ---------- Footer ----------

    @Then("the footer should show the {string} link")
    public void theFooterShouldShowTheLink(String network) {
        Assert.assertTrue(footer.isSocialLinkDisplayed(network), network + " link is not displayed");
    }

    @Then("the footer should contain the text {string}")
    public void theFooterShouldContainTheText(String text) {
        Assert.assertTrue(footer.getCopyrightText().contains(text),
                "Footer text was: " + footer.getCopyrightText());
    }

    @When("the user clicks the {string} link in the footer")
    public void theUserClicksTheLinkInTheFooter(String network) {
        footer.clickSocialLink(network);
    }

    @Then("a new tab should open with a URL containing {string}")
    public void aNewTabShouldOpenWithUrlContaining(String expectedUrl) {
        footer.switchToNewTab();
        Assert.assertTrue(footer.waitForUrlToContain(expectedUrl),
                "New tab URL was: " + footer.getCurrentUrl());
    }
}
