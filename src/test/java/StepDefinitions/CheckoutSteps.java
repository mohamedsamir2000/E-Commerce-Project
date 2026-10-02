package StepDefinitions;

import Context.TestContext;
import Pages.CheckoutPage;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;

public class CheckoutSteps {

    private static final double DELTA = 0.001;

    private final CheckoutPage checkoutPage;

    public CheckoutSteps(TestContext context) {
        this.checkoutPage = context.getCheckoutPage();
    }

    @When("the user enters checkout information {string} {string} {string}")
    public void theUserEntersCheckoutInformation(String firstName, String lastName, String postalCode) {
        checkoutPage.fillCheckoutInfo(firstName, lastName, postalCode);
    }

    @When("the user continues the checkout")
    public void theUserContinuesTheCheckout() {
        checkoutPage.clickContinue();
    }

    @When("the user finishes the order")
    public void theUserFinishesTheOrder() {
        checkoutPage.clickFinish();
    }

    @Then("the order confirmation message {string} should be displayed")
    public void theOrderConfirmationShouldBeDisplayed(String expectedMessage) {
        Assert.assertEquals(checkoutPage.getSuccessMessage(), expectedMessage);
    }

    @Then("the checkout error {string} should be displayed")
    public void theCheckoutErrorShouldBeDisplayed(String expectedError) {
        Assert.assertEquals(checkoutPage.getErrorMessage(), expectedError);
    }

    @Then("the item total should equal the sum of the item prices")
    public void theItemTotalShouldEqualTheSumOfPrices() {
        double expectedItemTotal = checkoutPage.getItemPrices().stream().mapToDouble(Double::doubleValue).sum();
        Assert.assertEquals(checkoutPage.getItemTotal(), expectedItemTotal, DELTA, "Item total mismatch");
    }

    @Then("the total should equal the item total plus tax")
    public void theTotalShouldEqualItemTotalPlusTax() {
        double expectedTotal = checkoutPage.getItemTotal() + checkoutPage.getTax();
        Assert.assertEquals(checkoutPage.getTotal(), expectedTotal, DELTA, "Total does not match item total + tax");
    }
}
