package com.core.stepdef;

import com.core.pages.SwagLabs.components.Footer;
import com.core.pages.SwagLabs.components.Header;
import com.core.pages.SwagLabs.components.SideMenu;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;

import java.util.List;
import java.util.Map;

/**
 * Side menu (All Items, About, Logout, Reset App State), header logo and footer links.
 */
public class MenuAndFooterStepDef {

    private final TestContext context;
    private final SideMenu sideMenu;
    private final Footer footer;
    private final Header header;

    public MenuAndFooterStepDef(TestContext context) {
        this.context = context;
        this.sideMenu = context.sideMenu();
        this.footer = context.footer();
        this.header = context.header();
    }

    // ---------- Side menu ----------

    @When("Open the menu")
    public void openTheMenu() {
        sideMenu.open();
    }

    @When("Close the menu")
    public void closeTheMenu() {
        sideMenu.close();
    }

    @When("Select {string} from the menu")
    public void selectFromTheMenu(String item) {
        sideMenu.select(item);
        if (item.equalsIgnoreCase("Reset App State")) {
            context.expectedCart().clear();
            // Reset does not navigate, so the menu stays open and its overlay would block the next click
            sideMenu.close();
        }
    }

    @When("Reset app state")
    public void resetAppState() {
        selectFromTheMenu("Reset App State");
    }

    @Then("Verify the menu is open")
    public void verifyTheMenuIsOpen() {
        Assert.assertTrue(sideMenu.isOpen(), "Menu is not open");
    }

    @Then("Verify the menu is closed")
    public void verifyTheMenuIsClosed() {
        Assert.assertFalse(sideMenu.isOpen(), "Menu is still open");
    }

    /** Table with an "Option" header. */
    @Then("Verify the menu options:")
    public void verifyTheMenuOptions(DataTable options) {
        List<String> expected = column(options, "Option");
        List<String> actual = sideMenu.getItems();
        Assert.assertTrue(actual.containsAll(expected), "Menu options " + actual + " do not include " + expected);
    }

    // ---------- Header ----------

    @Then("Verify the header logo is {string}")
    public void verifyTheHeaderLogoIs(String expected) {
        Assert.assertEquals(header.getLogoText(), expected, "Header logo");
    }

    // ---------- Footer ----------

    /** Table with a "Link" header: X, Facebook, LinkedIn. */
    @Then("Verify the footer links:")
    public void verifyTheFooterLinks(DataTable links) {
        for (String network : column(links, "Link")) {
            Assert.assertTrue(footer.isSocialLinkDisplayed(network), network + " link is not displayed");
        }
    }

    @Then("Verify the footer text contains {string}")
    public void verifyTheFooterTextContains(String text) {
        Assert.assertTrue(footer.getCopyrightText().contains(text), "Footer text was: " + footer.getCopyrightText());
    }

    @When("Click on {string} footer link")
    public void clickOnFooterLink(String network) {
        footer.clickSocialLink(network);
    }

    @Then("Verify a new tab opens with URL containing {string}")
    public void verifyANewTabOpensWithUrlContaining(String expectedUrl) {
        footer.switchToNewTab();
        // Social sites may redirect logged-out visitors to a login page that carries the target in a query parameter
        Assert.assertTrue(footer.waitForDecodedUrlToContain(expectedUrl), "New tab URL was: " + footer.getCurrentUrl());
    }

    @When("Close the new tab")
    public void closeTheNewTab() {
        footer.closeNewTabAndReturn();
    }

    private static List<String> column(DataTable table, String header) {
        return table.asMaps().stream().map(Map::values).map(v -> v.iterator().next().trim()).toList();
    }
}
