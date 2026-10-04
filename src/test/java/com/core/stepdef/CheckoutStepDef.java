package com.core.stepdef;

import com.core.pages.SwagLabs.AppPage;
import com.core.pages.SwagLabs.CheckoutCompletePage;
import com.core.pages.SwagLabs.CheckoutInformationPage;
import com.core.pages.SwagLabs.CheckoutOverviewPage;
import com.core.pages.SwagLabs.models.Product;
import com.core.utils.TestDataReader;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Checkout: information form, overview (products, payment, shipping, totals) and order confirmation.
 * Values in the tables and arguments can be test data references such as "StoreData//OrderTotal".
 */
public class CheckoutStepDef {

    private static final double CENT = 0.01;

    private final TestContext context;
    private final CheckoutInformationPage informationPage;
    private final CheckoutOverviewPage overviewPage;
    private final CheckoutCompletePage completePage;

    public CheckoutStepDef(TestContext context) {
        this.context = context;
        this.informationPage = context.checkoutInformationPage();
        this.overviewPage = context.checkoutOverviewPage();
        this.completePage = context.checkoutCompletePage();
    }

    // ---------- Information ----------

    /** Table with "label | value" header; labels: First Name, Last Name, Postal Code. Empty value = empty field. */
    @When("I fill the following fields:")
    public void iFillTheFollowingFields(DataTable fields) {
        for (Map<String, String> row : fields.asMaps()) {
            informationPage.fillField(row.get("label"), TestDataReader.resolve(row.get("value")));
        }
    }

    @When("Click on continue")
    public void clickOnContinue() {
        informationPage.clickContinue();
    }

    /** Information screen goes back to the cart, overview screen back to the products. */
    @When("Click on cancel")
    public void clickOnCancel() {
        if (context.isOn(AppPage.CHECKOUT_OVERVIEW)) {
            overviewPage.clickCancel();
        } else {
            informationPage.clickCancel();
        }
    }

    // ---------- Overview ----------

    @Then("Verify checkout overview lists the selected products")
    public void verifyCheckoutOverviewListsTheSelectedProducts() {
        CartStepDef.assertLines(overviewPage.getOrderLines(), context.expectedCart().products());
    }

    /** Table with "Product | Price" header. */
    @Then("Verify checkout overview lists the following products:")
    public void verifyCheckoutOverviewListsTheFollowingProducts(List<Product> expected) {
        CartStepDef.assertLines(overviewPage.getOrderLines(), expected);
    }

    /**
     * Rows "label | value" (no header); labels: Payment Information, Shipping Information, Item Total, Tax, Total.
     * Values can be test data references, e.g. | Total | StoreData//OrderTotal |
     */
    @Then("Verify checkout overview:")
    public void verifyCheckoutOverview(DataTable overview) {
        for (Map.Entry<String, String> row : overview.asMap(String.class, String.class).entrySet()) {
            String label = row.getKey().trim();
            String expected = TestDataReader.resolve(row.getValue());
            switch (label.toLowerCase(Locale.ROOT)) {
                case "payment information" -> Assert.assertEquals(overviewPage.getPaymentInformation(), expected, label);
                case "shipping information" -> Assert.assertEquals(overviewPage.getShippingInformation(), expected, label);
                case "item total" -> Assert.assertEquals(overviewPage.getItemTotal(), Product.parsePrice(expected), CENT, label);
                case "tax" -> Assert.assertEquals(overviewPage.getTax(), Product.parsePrice(expected), CENT, label);
                case "total" -> Assert.assertEquals(overviewPage.getTotal(), Product.parsePrice(expected), CENT, label);
                default -> throw new IllegalArgumentException("Unknown checkout overview field: " + label);
            }
        }
    }

    /**
     * Item total = sum of the products the scenario selected, tax = the given percent of it (e.g. "8%" or
     * "StoreData//TaxPercent"), total = item total + tax.
     */
    @Then("Verify order totals with {string} tax")
    public void verifyOrderTotalsWithTax(String taxPercent) {
        double itemTotal = overviewPage.getItemTotal();
        Assert.assertEquals(itemTotal, context.expectedCart().total(), CENT, "Item total");
        Assert.assertEquals(itemTotal,
                overviewPage.getOrderLines().stream().mapToDouble(l -> l.price() * l.quantity()).sum(), CENT,
                "Item total vs. the listed prices");
        double percent = Double.parseDouble(TestDataReader.resolve(taxPercent).replace("%", "").trim());
        double expectedTax = BigDecimal.valueOf(itemTotal).multiply(BigDecimal.valueOf(percent))
                .divide(BigDecimal.valueOf(100)).setScale(2, RoundingMode.HALF_UP).doubleValue();
        Assert.assertEquals(overviewPage.getTax(), expectedTax, CENT, "Tax");
        Assert.assertEquals(overviewPage.getTotal(), itemTotal + overviewPage.getTax(), CENT, "Total");
    }

    @When("Save the item total in {string}")
    public void saveTheItemTotalIn(String reference) {
        TestDataReader.save(reference, money(overviewPage.getItemTotal()));
    }

    @When("Save the order total in {string}")
    public void saveTheOrderTotalIn(String reference) {
        TestDataReader.save(reference, money(overviewPage.getTotal()));
    }

    @When("Click on finish")
    public void clickOnFinish() {
        overviewPage.clickFinish();
        context.expectedCart().clear();
    }

    // ---------- Complete ----------

    @Then("Verify order confirmation {string}")
    public void verifyOrderConfirmation(String expectedHeader) {
        Assert.assertEquals(completePage.getConfirmationHeader(), TestDataReader.resolve(expectedHeader), "Confirmation");
    }

    @Then("Verify order confirmation text {string}")
    public void verifyOrderConfirmationText(String expectedText) {
        Assert.assertEquals(completePage.getConfirmationText(), TestDataReader.resolve(expectedText), "Confirmation text");
    }

    @When("Back home")
    public void backHome() {
        completePage.backHome();
    }

    private static String money(double amount) {
        return String.format(Locale.ROOT, "$%.2f", amount);
    }
}
