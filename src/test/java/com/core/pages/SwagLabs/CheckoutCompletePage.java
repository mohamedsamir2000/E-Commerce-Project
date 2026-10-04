package com.core.pages.SwagLabs;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * "Checkout: Complete!" page shown after the order is placed.
 */
public class CheckoutCompletePage extends BasePage {

    private final By completeHeader = By.className("complete-header");
    private final By completeText = By.className("complete-text");
    private final By backHomeButton = By.id("back-to-products");

    public CheckoutCompletePage(WebDriver driver) {
        super(driver);
    }

    public String getConfirmationHeader() {
        return getText(completeHeader);
    }

    public String getConfirmationText() {
        return getText(completeText);
    }

    /** Clicks Back Home; a click that lands while the page is still rendering is lost, so it is retried once. */
    public void backHome() {
        waitForVisible(completeHeader);
        click(backHomeButton);
        if (!waitForUrlToContain("/inventory.html") && isDisplayed(backHomeButton)) {
            click(backHomeButton);
        }
    }
}
