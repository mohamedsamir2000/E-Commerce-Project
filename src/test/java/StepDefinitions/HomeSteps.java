package StepDefinitions;

import Context.TestContext;
import Pages.HomePage;
import Pages.ProductDetailsPage;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;

import java.util.List;
import java.util.regex.Pattern;

public class HomeSteps {

    private static final Pattern DIGIT = Pattern.compile("[0-9]");
    private static final Pattern SPECIAL_CHARACTER = Pattern.compile("[!@#$%&*()+=|<>?{}\\[\\]~]");

    private final TestContext context;
    private final HomePage homePage;

    public HomeSteps(TestContext context) {
        this.context = context;
        this.homePage = context.getHomePage();
    }

    // ---------- Inventory ----------

    @Then("the inventory page should be displayed")
    public void theInventoryPageShouldBeDisplayed() {
        Assert.assertTrue(homePage.isInventoryDisplayed(), "Inventory page is not displayed");
    }

    @Then("all {int} item names should not contain digits or special characters")
    public void allItemNamesShouldBeValid(int expectedCount) {
        List<String> names = homePage.getItemNames();
        int validCount = 0;
        for (String name : names) {
            if (!DIGIT.matcher(name).find() && !SPECIAL_CHARACTER.matcher(name).find()) {
                validCount++;
            } else {
                System.out.println("Product name is not correct: " + name);
            }
        }
        Assert.assertEquals(validCount, expectedCount, "There is an error in the names of items: " + names);
    }

    // ---------- Add / remove from home page ----------

    @When("the user adds the following items to the cart from the home page:")
    public void theUserAddsItemsFromHomePage(List<String> items) {
        context.setSelectedItems(items);
        for (String item : items) {
            homePage.addItemToCart(item);
        }
    }

    @Then("each added item should show the {string} button on the home page")
    public void eachAddedItemShouldShowButton(String buttonText) {
        for (String item : context.getSelectedItems()) {
            boolean displayed = buttonText.equalsIgnoreCase("Remove")
                    ? homePage.isRemoveButtonDisplayed(item)
                    : homePage.isAddToCartButtonDisplayed(item);
            Assert.assertTrue(displayed, "'" + buttonText + "' button is not displayed for: " + item);
        }
    }

    @When("the user removes the added items from the home page")
    public void theUserRemovesAddedItemsFromHomePage() {
        for (String item : context.getSelectedItems()) {
            homePage.removeItemFromCart(item);
        }
    }

    @When("the user adds all items to the cart from the home page")
    public void theUserAddsAllItemsFromHomePage() {
        homePage.clickAllAddToCartButtons();
    }

    @When("the user removes all items from the home page")
    public void theUserRemovesAllItemsFromHomePage() {
        homePage.clickAllRemoveButtons();
    }

    @Then("every item on the page should show a Remove button")
    public void everyItemShouldShowRemoveButton() {
        Assert.assertEquals(homePage.getRemoveButtonsCount(), homePage.getInventoryItemCount(), "Not all items were added");
    }

    @Then("the cart badge count should equal the number of items on the page")
    public void cartBadgeShouldEqualItemCount() {
        Assert.assertEquals(homePage.getCartBadgeCount(), homePage.getInventoryItemCount(), "Cart count is not correct");
    }

    @Then("the cart badge count should be {int}")
    public void cartBadgeCountShouldBe(int expectedCount) {
        Assert.assertEquals(homePage.getCartBadgeCount(), expectedCount, "Cart count is not correct");
    }

    // ---------- Product details ----------

    @When("the user adds each of the following items from its product details page:")
    public void theUserAddsItemsFromProductDetails(List<String> items) {
        context.setSelectedItems(items);
        ProductDetailsPage detailsPage = context.getProductDetailsPage();
        for (String item : items) {
            homePage.openProductDetails(item);
            detailsPage.clickAddToCart();
            detailsPage.clickBackToProducts();
        }
    }

    @When("the user removes each added item from its product details page")
    public void theUserRemovesItemsFromProductDetails() {
        ProductDetailsPage detailsPage = context.getProductDetailsPage();
        for (String item : context.getSelectedItems()) {
            homePage.openProductDetails(item);
            detailsPage.clickRemove();
            detailsPage.clickBackToProducts();
        }
    }

    // ---------- Header / menu / footer ----------

    @When("the user clicks the cart icon")
    public void theUserClicksTheCartIcon() {
        homePage.clickCart();
    }

    @When("the user opens the side menu")
    public void theUserOpensTheSideMenu() {
        homePage.openMenu();
    }

    @When("the user clicks {string} in the side menu")
    public void theUserClicksMenuItem(String menuItem) {
        homePage.clickMenuItem(menuItem);
    }

    @When("the user clicks the {string} icon in the footer")
    public void theUserClicksSocialIcon(String network) {
        homePage.clickSocialLink(network);
    }

    @Then("a new tab should open with a URL containing {string}")
    public void aNewTabShouldOpenWithUrl(String expectedUrl) {
        homePage.switchToNewTab();
        Assert.assertTrue(homePage.waitForUrlToContain(expectedUrl),
                "Failed to switch to the expected tab! Current URL: " + homePage.getCurrentUrl());
    }
}
