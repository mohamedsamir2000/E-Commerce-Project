package Pages;

import Models.CheckoutInfo;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Checkout step one: "Checkout: Your Information".
 */
public class CheckoutInformationPage extends BasePage {

    private final By firstNameInput = By.id("first-name");
    private final By lastNameInput = By.id("last-name");
    private final By postalCodeInput = By.id("postal-code");
    private final By continueButton = By.id("continue");
    private final By cancelButton = By.id("cancel");

    public CheckoutInformationPage(WebDriver driver) {
        super(driver);
    }

    public void enterInformation(CheckoutInfo info) {
        type(firstNameInput, info.firstName());
        // The last name field is not interactable for some users (e.g. error_user); skip it instead of crashing.
        try {
            WebElement lastNameField = waitForVisible(lastNameInput);
            if (lastNameField.isEnabled()) {
                lastNameField.clear();
                lastNameField.sendKeys(info.lastName());
            } else {
                System.out.println("Last name field is not interactable. Skipping input.");
            }
        } catch (TimeoutException e) {
            System.out.println("Last name field not found. Skipping input.");
        }
        type(postalCodeInput, info.postalCode());
    }

    public void clickContinue() {
        click(continueButton);
    }

    public void clickCancel() {
        click(cancelButton);
    }
}
