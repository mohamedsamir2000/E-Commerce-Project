package Pages;

import Models.Product;
import Models.SortOption;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.UnhandledAlertException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

import java.util.List;

/**
 * The products (inventory) page shown after login.
 */
public class InventoryPage extends BasePage {

    private final By inventoryList = By.className("inventory_list");
    private final By inventoryItems = By.className("inventory_item");
    private final By itemName = By.className("inventory_item_name");
    private final By itemDescription = By.className("inventory_item_desc");
    private final By itemPrice = By.className("inventory_item_price");
    private final By itemImage = By.cssSelector("img.inventory_item_img");
    private final By itemImageLink = By.cssSelector(".inventory_item_img a");
    private final By itemButton = By.tagName("button");
    private final By sortDropdown = By.className("product_sort_container");
    private final By activeSortOption = By.className("active_option");

    public InventoryPage(WebDriver driver) {
        super(driver);
    }

    // ---------- Product list ----------

    public boolean isDisplayed() {
        return isDisplayedWithin(inventoryList);
    }

    private List<WebElement> items() {
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(inventoryItems));
        return driver.findElements(inventoryItems);
    }

    private WebElement item(String productName) {
        return items().stream()
                .filter(i -> i.findElement(itemName).getText().trim().equals(productName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Product not found on the products page: " + productName));
    }

    public int getProductCount() {
        return items().size();
    }

    public List<String> getProductNames() {
        return items().stream().map(i -> i.findElement(itemName).getText().trim()).toList();
    }

    public List<Double> getProductPrices() {
        return items().stream().map(i -> Product.parsePrice(i.findElement(itemPrice).getText())).toList();
    }

    public List<Product> getProducts() {
        return items().stream()
                .map(i -> new Product(i.findElement(itemName).getText().trim(),
                        Product.parsePrice(i.findElement(itemPrice).getText())))
                .toList();
    }

    public Product getProduct(String productName) {
        WebElement item = item(productName);
        return new Product(productName, Product.parsePrice(item.findElement(itemPrice).getText()));
    }

    public String getProductDescription(String productName) {
        return item(productName).findElement(itemDescription).getText().trim();
    }

    public boolean isProductImageDisplayed(String productName) {
        WebElement image = item(productName).findElement(itemImage);
        return image.isDisplayed() && image.getAttribute("src") != null && !image.getAttribute("src").isBlank();
    }

    // ---------- Cart buttons ----------

    public String getButtonText(String productName) {
        return item(productName).findElement(itemButton).getText().trim();
    }

    public void addToCart(String productName) {
        WebElement button = item(productName).findElement(itemButton);
        if (!button.getText().trim().equalsIgnoreCase("Add to cart")) {
            throw new IllegalStateException(productName + " is already in the cart (button shows '" + button.getText() + "')");
        }
        button.click();
    }

    public void removeFromCart(String productName) {
        WebElement button = item(productName).findElement(itemButton);
        if (!button.getText().trim().equalsIgnoreCase("Remove")) {
            throw new IllegalStateException(productName + " is not in the cart (button shows '" + button.getText() + "')");
        }
        button.click();
    }

    // ---------- Navigation to details ----------

    public void openProductByName(String productName) {
        item(productName).findElement(itemName).click();
    }

    public void openProductByImage(String productName) {
        item(productName).findElement(itemImageLink).click();
    }

    // ---------- Sorting ----------

    /**
     * Some users (error_user) get an unexpected alert when sorting; it is accepted and the selection retried once.
     */
    public void sortBy(SortOption option) {
        try {
            new Select(waitForVisible(sortDropdown)).selectByVisibleText(option.label());
        } catch (UnhandledAlertException e) {
            acceptAlertIfPresent();
            new Select(waitForVisible(sortDropdown)).selectByVisibleText(option.label());
        }
        acceptAlertIfPresent();
    }

    public String getActiveSortOption() {
        return getText(activeSortOption);
    }

    private void acceptAlertIfPresent() {
        try {
            Alert alert = driver.switchTo().alert();
            System.out.println("Unexpected alert: " + alert.getText());
            alert.accept();
        } catch (NoAlertPresentException ignored) {
            // nothing to accept
        }
    }
}
