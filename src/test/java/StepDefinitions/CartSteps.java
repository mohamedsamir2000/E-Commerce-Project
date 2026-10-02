package StepDefinitions;

import Context.TestContext;
import Models.CartLine;
import Models.Product;
import Pages.CartPage;
import Pages.Components.Header;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;

import java.util.List;

public class CartSteps {

    private final TestContext context;
    private final CartPage cartPage;
    private final Header header;

    public CartSteps(TestContext context) {
        this.context = context;
        this.cartPage = context.cartPage();
        this.header = context.header();
    }

    @When("the user opens the cart")
    public void theUserOpensTheCart() {
        header.openCart();
    }

    @When("the user continues shopping")
    public void theUserContinuesShopping() {
        cartPage.continueShopping();
    }

    @When("the user proceeds to checkout")
    public void theUserProceedsToCheckout() {
        cartPage.checkout();
    }

    // ---------- Cart badge ----------

    @Then("the cart badge should show {int}")
    public void theCartBadgeShouldShow(int expectedCount) {
        Assert.assertEquals(header.getCartBadgeCount(), expectedCount, "Cart badge count");
    }

    @Then("the cart badge should not be displayed")
    public void theCartBadgeShouldNotBeDisplayed() {
        Assert.assertFalse(header.isCartBadgeDisplayed(), "Cart badge is displayed with " + header.getCartBadgeCount());
    }

    @Then("the cart badge should match the selected products")
    public void theCartBadgeShouldMatchTheSelectedProducts() {
        Assert.assertEquals(header.getCartBadgeCount(), context.expectedCart().size(), "Cart badge count");
    }

    // ---------- Cart content ----------

    @Then("the cart should contain the selected products")
    public void theCartShouldContainTheSelectedProducts() {
        assertLines(cartPage.getCartLines(), context.expectedCart().products());
    }

    @Then("the cart should contain the following products:")
    public void theCartShouldContainTheFollowingProducts(List<Product> expected) {
        assertLines(cartPage.getCartLines(), expected);
    }

    @Then("the cart should contain {int} product(s)")
    public void theCartShouldContainProducts(int expectedCount) {
        Assert.assertEquals(cartPage.getCartLines().size(), expectedCount, "Number of products in the cart");
    }

    @Then("the cart should be empty")
    public void theCartShouldBeEmpty() {
        Assert.assertTrue(cartPage.getCartLines().isEmpty(), "Cart is not empty: " + cartPage.getProductNames());
    }

    /**
     * Verifies the lines match the expected products (any order), each with quantity 1.
     * Shared with the checkout overview steps.
     */
    static void assertLines(List<CartLine> actual, List<Product> expected) {
        List<String> actualNames = actual.stream().map(CartLine::name).sorted().toList();
        List<String> expectedNames = expected.stream().map(Product::name).sorted().toList();
        Assert.assertEquals(actualNames, expectedNames, "Products listed");
        for (Product product : expected) {
            CartLine line = actual.stream().filter(l -> l.name().equals(product.name())).findFirst().orElseThrow();
            Assert.assertEquals(line.price(), product.price(), 0.001, "Price of " + product.name());
            Assert.assertEquals(line.quantity(), 1, "Quantity of " + product.name());
        }
    }
}
