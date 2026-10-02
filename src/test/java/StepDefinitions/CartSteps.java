package StepDefinitions;

import Context.TestContext;
import Pages.CartPage;
import Pages.HomePage;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;

import java.util.List;

public class CartSteps {

    private final CartPage cartPage;
    private final HomePage homePage;
    private final TestContext context;

    public CartSteps(TestContext context) {
        this.context = context;
        this.cartPage = context.getCartPage();
        this.homePage = context.getHomePage();
    }

    @Then("each of the following items can be added from the home page and removed from the cart page one at a time:")
    public void eachItemCanBeRemovedFromCartPage(List<String> items) {
        for (String item : items) {
            homePage.addItemToCart(item);
            Assert.assertTrue(homePage.isRemoveButtonDisplayed(item), item + " was not added to the cart.");

            homePage.clickCart();
            cartPage.removeItem(item);
            Assert.assertFalse(cartPage.isItemInCart(item), item + " was not removed from the cart.");

            cartPage.clickContinueShopping();
        }
    }

    @When("the user removes the added items from the cart page")
    public void theUserRemovesAddedItemsFromCartPage() {
        for (String item : context.getSelectedItems()) {
            cartPage.removeItem(item);
            Assert.assertFalse(cartPage.isItemInCart(item), item + " was not removed from the cart.");
        }
    }

    @Then("the cart should be empty")
    public void theCartShouldBeEmpty() {
        Assert.assertTrue(cartPage.isCartEmpty(), "Cart is not empty: " + cartPage.getCartItemNames());
    }

    @When("the user clicks continue shopping")
    public void theUserClicksContinueShopping() {
        cartPage.clickContinueShopping();
    }

    @When("the user proceeds to checkout")
    public void theUserProceedsToCheckout() {
        cartPage.clickCheckout();
    }
}
