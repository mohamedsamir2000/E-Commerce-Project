package com.core.pages.SwagLabs.components;

import com.core.pages.SwagLabs.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * The header shown on every page after login: logo and shopping cart icon.
 */
public class Header extends BasePage {

    private final By appLogo = By.className("app_logo");
    private final By cartLink = By.className("shopping_cart_link");
    private final By cartBadge = By.className("shopping_cart_badge");

    public Header(WebDriver driver) {
        super(driver);
    }

    public String getLogoText() {
        return getText(appLogo);
    }

    public void openCart() {
        click(cartLink);
    }

    public boolean isCartBadgeDisplayed() {
        return isDisplayed(cartBadge);
    }

    /** @return the number on the cart badge, or 0 when no badge is shown. */
    public int getCartBadgeCount() {
        // The page can re-render between finding the badge and reading it (e.g. performance_glitch_user)
        return wait.ignoring(StaleElementReferenceException.class).until(d -> {
            List<WebElement> badges = d.findElements(cartBadge);
            String text = badges.isEmpty() ? "" : badges.get(0).getText().trim();
            return text.isEmpty() ? 0 : Integer.parseInt(text);
        });
    }
}
