package Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

/**
 * Common driver helpers shared by all page objects.
 */
public abstract class BasePage {

    protected final WebDriver driver;
    protected final WebDriverWait wait;

    protected BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    protected WebElement waitForVisible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    protected WebElement waitForClickable(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    protected void click(By locator) {
        waitForClickable(locator).click();
    }

    protected void type(By locator, String text) {
        WebElement element = waitForVisible(locator);
        element.clear();
        element.sendKeys(text);
    }

    protected String getText(By locator) {
        return waitForVisible(locator).getText();
    }

    protected boolean isDisplayed(By locator) {
        List<WebElement> elements = driver.findElements(locator);
        return !elements.isEmpty() && elements.get(0).isDisplayed();
    }

    protected static double parsePrice(String text) {
        return Double.parseDouble(text.replaceAll("[^0-9.]", ""));
    }

    /**
     * Converts a product name to the id used in the site's data-test attributes,
     * e.g. "Sauce Labs Backpack" -> "sauce-labs-backpack".
     */
    protected static String toDataTestId(String productName) {
        return productName.trim().toLowerCase().replace(' ', '-');
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    public boolean waitForUrlToBe(String url) {
        try {
            return wait.until(ExpectedConditions.urlToBe(url));
        } catch (org.openqa.selenium.TimeoutException e) {
            return false;
        }
    }

    public boolean waitForUrlToContain(String fragment) {
        try {
            return wait.until(ExpectedConditions.urlContains(fragment));
        } catch (org.openqa.selenium.TimeoutException e) {
            return false;
        }
    }
}
