package Pages;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.UnhandledAlertException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

import java.util.ArrayList;
import java.util.List;

/**
 * The inventory (products) page shown after a successful login,
 * including the header cart icon, the side menu and the footer links.
 */
public class HomePage extends BasePage {

    // Header
    private final By appLogo = By.className("app_logo");
    private final By cartLink = By.className("shopping_cart_link");
    private final By cartBadge = By.className("shopping_cart_badge");

    // Side menu
    private final By menuButton = By.id("react-burger-menu-btn");
    private final By allItemsMenuLink = By.id("inventory_sidebar_link");
    private final By aboutMenuLink = By.id("about_sidebar_link");
    private final By logoutMenuLink = By.id("logout_sidebar_link");
    private final By resetAppStateMenuLink = By.id("reset_sidebar_link");

    // Inventory
    private final By inventoryContainer = By.id("inventory_container");
    private final By inventoryItems = By.className("inventory_item");
    private final By itemNames = By.className("inventory_item_name");
    private final By itemPrices = By.className("inventory_item_price");
    private final By addToCartButtons = By.xpath("//button[text()='Add to cart']");
    private final By removeButtons = By.xpath("//button[text()='Remove']");
    private final By sortDropdown = By.className("product_sort_container");

    public HomePage(WebDriver driver) {
        super(driver);
    }

    private By addToCartButtonFor(String productName) {
        return By.cssSelector(String.format("[data-test=\"add-to-cart-%s\"]", toDataTestId(productName)));
    }

    private By removeButtonFor(String productName) {
        return By.cssSelector(String.format("[data-test=\"remove-%s\"]", toDataTestId(productName)));
    }

    private By productLinkFor(String productName) {
        return By.xpath(String.format("//div[contains(@class,'inventory_item_name') and normalize-space()=\"%s\"]", productName));
    }

    // ---------- Inventory ----------

    public boolean isInventoryDisplayed() {
        return isDisplayed(inventoryContainer) && isDisplayed(appLogo);
    }

    public int getInventoryItemCount() {
        return driver.findElements(inventoryItems).size();
    }

    public List<String> getItemNames() {
        return driver.findElements(itemNames).stream().map(e -> e.getText().trim()).toList();
    }

    public List<Double> getItemPrices() {
        return driver.findElements(itemPrices).stream().map(e -> parsePrice(e.getText())).toList();
    }

    public void addItemToCart(String productName) {
        click(addToCartButtonFor(productName));
    }

    public void removeItemFromCart(String productName) {
        click(removeButtonFor(productName));
    }

    public boolean isRemoveButtonDisplayed(String productName) {
        try {
            waitForVisible(removeButtonFor(productName));
            return true;
        } catch (org.openqa.selenium.TimeoutException e) {
            return false;
        }
    }

    public boolean isAddToCartButtonDisplayed(String productName) {
        try {
            waitForVisible(addToCartButtonFor(productName));
            return true;
        } catch (org.openqa.selenium.TimeoutException e) {
            return false;
        }
    }

    public void clickAllAddToCartButtons() {
        for (WebElement button : driver.findElements(addToCartButtons)) {
            button.click();
        }
    }

    public void clickAllRemoveButtons() {
        for (WebElement button : driver.findElements(removeButtons)) {
            button.click();
        }
    }

    public int getRemoveButtonsCount() {
        return driver.findElements(removeButtons).size();
    }

    public void openProductDetails(String productName) {
        click(productLinkFor(productName));
    }

    // ---------- Header / cart ----------

    public void clickCart() {
        click(cartLink);
    }

    /**
     * @return the number shown on the cart badge, or 0 when no badge is displayed.
     */
    public int getCartBadgeCount() {
        List<WebElement> badges = driver.findElements(cartBadge);
        if (badges.isEmpty() || badges.get(0).getText().isBlank()) {
            return 0;
        }
        return Integer.parseInt(badges.get(0).getText().trim());
    }

    // ---------- Sorting ----------

    /**
     * Selects a sort option by its visible text, e.g. "Price (low to high)".
     * Some users (error_user) get an unexpected alert; it is accepted and the selection is retried once.
     */
    public void sortBy(String optionText) {
        try {
            selectSortOption(optionText);
        } catch (UnhandledAlertException e) {
            dismissAlertIfPresent();
            selectSortOption(optionText);
        }
        dismissAlertIfPresent();
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(itemNames));
    }

    private void selectSortOption(String optionText) {
        new Select(waitForVisible(sortDropdown)).selectByVisibleText(optionText);
    }

    private void dismissAlertIfPresent() {
        try {
            Alert alert = driver.switchTo().alert();
            System.out.println("Unexpected alert found: " + alert.getText());
            alert.accept();
        } catch (NoAlertPresentException ignored) {
            // no alert to dismiss
        }
    }

    // ---------- Side menu ----------

    public void openMenu() {
        click(menuButton);
    }

    public void clickMenuItem(String menuItem) {
        By link = switch (menuItem.trim().toLowerCase()) {
            case "all items" -> allItemsMenuLink;
            case "about" -> aboutMenuLink;
            case "logout" -> logoutMenuLink;
            case "reset app state" -> resetAppStateMenuLink;
            default -> throw new IllegalArgumentException("Unknown menu item: " + menuItem);
        };
        click(link);
    }

    public void logout() {
        openMenu();
        clickMenuItem("Logout");
    }

    // ---------- Footer ----------

    public void clickSocialLink(String network) {
        click(By.linkText(network));
    }

    /**
     * Waits for a second browser tab to open and switches to it.
     */
    public void switchToNewTab() {
        wait.until(ExpectedConditions.numberOfWindowsToBe(2));
        List<String> tabs = new ArrayList<>(driver.getWindowHandles());
        driver.switchTo().window(tabs.get(1));
    }
}
