package com.core.stepdef;

import com.core.pages.SwagLabs.CartPage;
import com.core.pages.SwagLabs.components.Header;
import com.core.pages.SwagLabs.models.CartLine;
import com.core.pages.SwagLabs.models.Product;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;

import java.util.List;

/**
 * Cart screen and the cart badge in the header.
 */
public class CartStepDef {

    private final TestContext context;
    private final CartPage cartPage;
    private final Header header;

    public CartStepDef(TestContext context) {
        this.context = context;
        this.cartPage = context.cartPage();
        this.header = context.header();
    }

    @When("Open cart")
    public void openCart() {
        header.openCart();
    }

    @When("Continue shopping")
    public void continueShopping() {
        cartPage.continueShopping();
    }

    @When("Click on checkout")
    public void clickOnCheckout() {
        cartPage.checkout();
    }

    // ---------- Cart badge ----------

    @Then("Verify cart badge is {string}")
    public void verifyCartBadgeIs(String expectedCount) {
        Assert.assertEquals(header.getCartBadgeCount(), Integer.parseInt(expectedCount.trim()), "Cart badge");
    }

    @Then("Verify cart badge is not displayed")
    public void verifyCartBadgeIsNotDisplayed() {
        Assert.assertFalse(header.isCartBadgeDisplayed(), "Cart badge is displayed with " + header.getCartBadgeCount());
    }

    @Then("Verify cart badge matches the selected products")
    public void verifyCartBadgeMatchesTheSelectedProducts() {
        Assert.assertEquals(header.getCartBadgeCount(), context.expectedCart().size(), "Cart badge");
    }

    // ---------- Cart content ----------

    @Then("Verify cart contains the selected products")
    public void verifyCartContainsTheSelectedProducts() {
        assertLines(cartPage.getCartLines(), context.expectedCart().products());
    }

    /** Table with "Product | Price" header. */
    @Then("Verify cart contains the following products:")
    public void verifyCartContainsTheFollowingProducts(List<Product> expected) {
        assertLines(cartPage.getCartLines(), expected);
    }

    @Then("Verify cart is empty")
    public void verifyCartIsEmpty() {
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
