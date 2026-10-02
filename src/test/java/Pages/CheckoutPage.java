package Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Covers the three checkout steps: your information, overview and complete.
 */
public class CheckoutPage extends BasePage {

    // Step one: information
    private final By firstNameInput = By.id("first-name");
    private final By lastNameInput = By.id("last-name");
    private final By postalCodeInput = By.id("postal-code");
    private final By continueButton = By.id("continue");
    private final By cancelButton = By.id("cancel");
    private final By errorMessage = By.cssSelector("h3[data-test='error']");

    // Step two: overview
    private final By itemPrices = By.className("inventory_item_price");
    private final By itemTotalLabel = By.className("summary_subtotal_label");
    private final By taxLabel = By.className("summary_tax_label");
    private final By totalLabel = By.className("summary_total_label");
    private final By finishButton = By.id("finish");

    // Complete
    private final By successMessage = By.className("complete-header");

    public CheckoutPage(WebDriver driver) {
        super(driver);
    }

    public void fillCheckoutInfo(String firstName, String lastName, String postalCode) {
        type(firstNameInput, firstName);
        // The last name field is not interactable for some users (e.g. error_user); skip it instead of crashing.
        try {
            WebElement lastNameField = waitForVisible(lastNameInput);
            if (lastNameField.isEnabled()) {
                lastNameField.clear();
                lastNameField.sendKeys(lastName);
            } else {
                System.out.println("Last name field is not interactable. Skipping input.");
            }
        } catch (TimeoutException e) {
            System.out.println("Last name field not found. Skipping input.");
        }
        type(postalCodeInput, postalCode);
    }

    public void clickContinue() {
        click(continueButton);
    }

    public void clickCancel() {
        click(cancelButton);
    }

    public String getErrorMessage() {
        return getText(errorMessage);
    }

    public List<Double> getItemPrices() {
        return driver.findElements(itemPrices).stream().map(e -> parsePrice(e.getText())).toList();
    }

    public double getItemTotal() {
        return parsePrice(getText(itemTotalLabel)); // "Item total: $39.98"
    }

    public double getTax() {
        return parsePrice(getText(taxLabel)); // "Tax: $3.20"
    }

    public double getTotal() {
        return parsePrice(getText(totalLabel)); // "Total: $43.18"
    }

    public void clickFinish() {
        click(finishButton);
    }

    public String getSuccessMessage() {
        return getText(successMessage);
    }
}
