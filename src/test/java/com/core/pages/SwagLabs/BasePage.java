package com.core.pages.SwagLabs;

import com.core.TestEnvConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;

/**
 * Common driver helpers shared by all page objects and components.
 */
public abstract class BasePage {

    private final By pageTitle = By.cssSelector("span.title");
    private final By errorMessage = By.cssSelector("h3[data-test='error']");
    private final By errorCloseButton = By.cssSelector(".error-button");

    protected final Logger log = LogManager.getLogger(getClass());
    protected final WebDriver driver;
    protected final WebDriverWait wait;

    protected BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(TestEnvConfig.timeoutSeconds()));
    }

    // ---------- Element helpers ----------

    protected WebElement waitForVisible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    protected WebElement waitForClickable(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    protected void click(By locator) {
        log.info("Click {}", locator);
        waitForClickable(locator).click();
    }

    protected void type(By locator, String text) {
        log.info("Type '{}' into {}", text, locator);
        WebElement element = waitForVisible(locator);
        // clear() empties the field without firing React's change event, so the app would keep the old value
        String current = element.getAttribute("value");
        if (current != null && !current.isEmpty()) {
            element.sendKeys(Keys.END);
            element.sendKeys(Keys.BACK_SPACE.toString().repeat(current.length()));
        }
        element.sendKeys(text == null ? "" : text);
    }

    protected String getText(By locator) {
        return waitForVisible(locator).getText().trim();
    }

    protected boolean isDisplayed(By locator) {
        List<WebElement> elements = driver.findElements(locator);
        return !elements.isEmpty() && elements.get(0).isDisplayed();
    }

    protected boolean isDisplayedWithin(By locator) {
        try {
            waitForVisible(locator);
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    // ---------- Shared page parts ----------

    /** The secondary header title, e.g. "Products" or "Your Cart". */
    public String getPageTitle() {
        return getText(pageTitle);
    }

    public boolean isErrorDisplayed() {
        return isDisplayed(errorMessage);
    }

    public String getErrorMessage() {
        return getText(errorMessage);
    }

    public void dismissErrorMessage() {
        click(errorCloseButton);
    }

    // ---------- Browser ----------

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    public void open(String url) {
        driver.get(url);
    }

    public void refresh() {
        driver.navigate().refresh();
    }

    public void navigateBack() {
        driver.navigate().back();
    }

    public boolean waitForUrlToBe(String url) {
        try {
            return wait.until(ExpectedConditions.urlToBe(url));
        } catch (TimeoutException e) {
            return false;
        }
    }

    public boolean waitForUrlToContain(String fragment) {
        try {
            return wait.until(ExpectedConditions.urlContains(fragment));
        } catch (TimeoutException e) {
            return false;
        }
    }

    /**
     * Like {@link #waitForUrlToContain(String)} but also matches the URL-decoded URL, so a redirect such as
     * facebook.com/login/?next=https%3A%2F%2Fwww.facebook.com%2Fsaucelabs still counts as the expected page.
     */
    public boolean waitForDecodedUrlToContain(String fragment) {
        try {
            return wait.until(d -> URLDecoder.decode(d.getCurrentUrl(), StandardCharsets.UTF_8).contains(fragment));
        } catch (TimeoutException e) {
            return false;
        }
    }
}
