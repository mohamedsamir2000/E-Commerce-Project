package com.core.pages.SwagLabs;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

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

    /** Fills one field by its label: First Name, Last Name or Postal Code. */
    public void fillField(String label, String value) {
        switch (label.trim().toLowerCase()) {
            case "first name" -> type(firstNameInput, value);
            case "last name" -> type(lastNameInput, value);
            case "postal code", "zip/postal code" -> type(postalCodeInput, value);
            default -> throw new IllegalArgumentException("Unknown checkout field: " + label);
        }
    }

    public void clickContinue() {
        click(continueButton);
    }

    public void clickCancel() {
        click(cancelButton);
    }
}
