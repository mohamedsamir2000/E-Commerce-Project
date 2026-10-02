package Pages;

import Models.CartLine;
import Models.Product;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.List;

/**
 * Checkout step two: "Checkout: Overview".
 */
public class CheckoutOverviewPage extends BasePage {

    private final By cartItems = By.className("cart_item");
    private final By itemName = By.className("inventory_item_name");
    private final By itemPrice = By.className("inventory_item_price");
    private final By itemQuantity = By.className("cart_quantity");
    private final By paymentInformation = By.xpath("//div[contains(@class,'summary_info_label')][contains(.,'Payment')]/following-sibling::div[1]");
    private final By shippingInformation = By.xpath("//div[contains(@class,'summary_info_label')][contains(.,'Shipping')]/following-sibling::div[1]");
    private final By itemTotalLabel = By.className("summary_subtotal_label");
    private final By taxLabel = By.className("summary_tax_label");
    private final By totalLabel = By.className("summary_total_label");
    private final By finishButton = By.id("finish");
    private final By cancelButton = By.id("cancel");

    public CheckoutOverviewPage(WebDriver driver) {
        super(driver);
    }

    public List<CartLine> getOrderLines() {
        waitForVisible(itemTotalLabel);
        return driver.findElements(cartItems).stream()
                .map(i -> new CartLine(
                        i.findElement(itemName).getText().trim(),
                        Product.parsePrice(i.findElement(itemPrice).getText()),
                        Integer.parseInt(i.findElement(itemQuantity).getText().trim())))
                .toList();
    }

    public String getPaymentInformation() {
        return getText(paymentInformation);
    }

    public String getShippingInformation() {
        return getText(shippingInformation);
    }

    /** "Item total: $39.98" -> 39.98 */
    public double getItemTotal() {
        return Product.parsePrice(getText(itemTotalLabel));
    }

    /** "Tax: $3.20" -> 3.20 */
    public double getTax() {
        return Product.parsePrice(getText(taxLabel));
    }

    /** "Total: $43.18" -> 43.18 */
    public double getTotal() {
        return Product.parsePrice(getText(totalLabel));
    }

    public void clickFinish() {
        click(finishButton);
    }

    public void clickCancel() {
        click(cancelButton);
    }
}
