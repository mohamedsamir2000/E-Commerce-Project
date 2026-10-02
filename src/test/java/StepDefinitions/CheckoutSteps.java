package StepDefinitions;

import Context.TestContext;
import Models.CheckoutInfo;
import Models.Product;
import Pages.AppPage;
import Pages.CheckoutCompletePage;
import Pages.CheckoutInformationPage;
import Pages.CheckoutOverviewPage;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class CheckoutSteps {

    private static final double CENT = 0.01;

    private final TestContext context;
    private final CartSteps cartSteps;
    private final CheckoutInformationPage informationPage;
    private final CheckoutOverviewPage overviewPage;
    private final CheckoutCompletePage completePage;

    public CheckoutSteps(TestContext context, CartSteps cartSteps) {
        this.context = context;
        this.cartSteps = cartSteps;
        this.informationPage = context.checkoutInformationPage();
        this.overviewPage = context.checkoutOverviewPage();
        this.completePage = context.checkoutCompletePage();
    }

    // ---------- Step one: information ----------

    @When("the user enters the checkout information:")
    public void theUserEntersTheCheckoutInformation(List<CheckoutInfo> info) {
        informationPage.enterInformation(info.get(0));
    }

    @When("the user enters first name {string}, last name {string} and postal code {string}")
    public void theUserEntersCheckoutInformation(String firstName, String lastName, String postalCode) {
        informationPage.enterInformation(new CheckoutInfo(firstName, lastName, postalCode));
    }

    @When("the user continues the checkout")
    public void theUserContinuesTheCheckout() {
        informationPage.clickContinue();
    }

    /** Shortcut for the whole path from any page to the checkout overview. */
    @When("the user checks out with first name {string}, last name {string} and postal code {string}")
    public void theUserChecksOutWith(String firstName, String lastName, String postalCode) {
        cartSteps.theUserOpensTheCart();
        cartSteps.theUserProceedsToCheckout();
        theUserEntersCheckoutInformation(firstName, lastName, postalCode);
        theUserContinuesTheCheckout();
    }

    /** Works on both checkout steps: information goes back to the cart, overview back to the products. */
    @When("the user cancels the checkout")
    public void theUserCancelsTheCheckout() {
        if (context.isOn(AppPage.CHECKOUT_OVERVIEW)) {
            overviewPage.clickCancel();
        } else {
            informationPage.clickCancel();
        }
    }

    // ---------- Step two: overview ----------

    @Then("the checkout overview should list the selected products")
    public void theCheckoutOverviewShouldListTheSelectedProducts() {
        CartSteps.assertLines(overviewPage.getOrderLines(), context.expectedCart().products());
    }

    @Then("the checkout overview should list the following products:")
    public void theCheckoutOverviewShouldListTheFollowingProducts(List<Product> expected) {
        CartSteps.assertLines(overviewPage.getOrderLines(), expected);
    }

    @Then("the payment information should be {string}")
    public void thePaymentInformationShouldBe(String expected) {
        Assert.assertEquals(overviewPage.getPaymentInformation(), expected);
    }

    @Then("the shipping information should be {string}")
    public void theShippingInformationShouldBe(String expected) {
        Assert.assertEquals(overviewPage.getShippingInformation(), expected);
    }

    @Then("the item total should equal the sum of the product prices")
    public void theItemTotalShouldEqualTheSumOfPrices() {
        double sum = overviewPage.getOrderLines().stream().mapToDouble(l -> l.price() * l.quantity()).sum();
        Assert.assertEquals(overviewPage.getItemTotal(), sum, CENT, "Item total");
    }

    @Then("the item total should match the selected products")
    public void theItemTotalShouldMatchTheSelectedProducts() {
        Assert.assertEquals(overviewPage.getItemTotal(), context.expectedCart().total(), CENT, "Item total");
    }

    @Then("the item total should be {string}")
    public void theItemTotalShouldBe(String expected) {
        Assert.assertEquals(overviewPage.getItemTotal(), Product.parsePrice(expected), CENT, "Item total");
    }

    @Then("the tax should be {int}% of the item total")
    public void theTaxShouldBePercentOfTheItemTotal(int percent) {
        double expectedTax = BigDecimal.valueOf(overviewPage.getItemTotal())
                .multiply(BigDecimal.valueOf(percent)).divide(BigDecimal.valueOf(100))
                .setScale(2, RoundingMode.HALF_UP).doubleValue();
        Assert.assertEquals(overviewPage.getTax(), expectedTax, CENT, "Tax");
    }

    @Then("the total should equal the item total plus tax")
    public void theTotalShouldEqualItemTotalPlusTax() {
        Assert.assertEquals(overviewPage.getTotal(), overviewPage.getItemTotal() + overviewPage.getTax(), CENT, "Total");
    }

    @When("the user finishes the order")
    public void theUserFinishesTheOrder() {
        overviewPage.clickFinish();
        context.expectedCart().clear();
    }

    // ---------- Complete ----------

    @Then("the order confirmation should show {string}")
    public void theOrderConfirmationShouldShow(String expectedHeader) {
        Assert.assertEquals(completePage.getConfirmationHeader(), expectedHeader);
    }

    @Then("the order confirmation text should be {string}")
    public void theOrderConfirmationTextShouldBe(String expectedText) {
        Assert.assertEquals(completePage.getConfirmationText(), expectedText);
    }

    @When("the user goes back home")
    public void theUserGoesBackHome() {
        completePage.backHome();
    }
}
