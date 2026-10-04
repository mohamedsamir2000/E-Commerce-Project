package com.core.pages.SwagLabs.components;

import com.core.pages.SwagLabs.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.ArrayList;
import java.util.List;

/**
 * The footer: social media links and copyright text.
 */
public class Footer extends BasePage {

    private final By copyright = By.className("footer_copy");

    public Footer(WebDriver driver) {
        super(driver);
    }

    private By socialLink(String network) {
        return switch (network.trim().toLowerCase()) {
            case "x", "twitter" -> By.cssSelector("footer a[href*='x.com/saucelabs'], footer a[href*='twitter.com/saucelabs']");
            case "facebook" -> By.cssSelector("footer a[href*='facebook.com/saucelabs']");
            case "linkedin" -> By.cssSelector("footer a[href*='linkedin.com/company/sauce-labs']");
            default -> throw new IllegalArgumentException("Unknown social network: " + network);
        };
    }

    public boolean isSocialLinkDisplayed(String network) {
        return isDisplayed(socialLink(network));
    }

    public void clickSocialLink(String network) {
        click(socialLink(network));
    }

    public String getCopyrightText() {
        return getText(copyright);
    }

    /** Waits for a second browser tab to open and switches to it. */
    public void switchToNewTab() {
        wait.until(ExpectedConditions.numberOfWindowsToBe(2));
        List<String> tabs = new ArrayList<>(driver.getWindowHandles());
        driver.switchTo().window(tabs.get(1));
    }

    /** Closes the current (new) tab and switches back to the first tab. */
    public void closeNewTabAndReturn() {
        List<String> tabs = new ArrayList<>(driver.getWindowHandles());
        driver.close();
        driver.switchTo().window(tabs.get(0));
    }
}
