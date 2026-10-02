package Pages;

import Models.CartLine;
import Models.Product;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class CartPage extends BasePage {

    private final By cartList = By.className("cart_list");
    private final By cartItems = By.className("cart_item");
    private final By itemName = By.className("inventory_item_name");
    private final By itemPrice = By.className("inventory_item_price");
    private final By itemQuantity = By.className("cart_quantity");
    private final By itemButton = By.tagName("button");
    private final By continueShoppingButton = By.id("continue-shopping");
    private final By checkoutButton = By.id("checkout");

    public CartPage(WebDriver driver) {
        super(driver);
    }

    public boolean isDisplayed() {
        return isDisplayedWithin(cartList);
    }

    private WebElement item(String productName) {
        return driver.findElements(cartItems).stream()
                .filter(i -> i.findElement(itemName).getText().trim().equals(productName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Product not found in the cart: " + productName));
    }

    public List<CartLine> getCartLines() {
        waitForVisible(cartList);
        return driver.findElements(cartItems).stream()
                .map(i -> new CartLine(
                        i.findElement(itemName).getText().trim(),
                        Product.parsePrice(i.findElement(itemPrice).getText()),
                        Integer.parseInt(i.findElement(itemQuantity).getText().trim())))
                .toList();
    }

    public List<String> getProductNames() {
        return getCartLines().stream().map(CartLine::name).toList();
    }

    public boolean contains(String productName) {
        return getProductNames().contains(productName);
    }

    public void removeFromCart(String productName) {
        item(productName).findElement(itemButton).click();
    }

    public void openProduct(String productName) {
        item(productName).findElement(itemName).click();
    }

    public void continueShopping() {
        click(continueShoppingButton);
    }

    public void checkout() {
        click(checkoutButton);
    }
}
