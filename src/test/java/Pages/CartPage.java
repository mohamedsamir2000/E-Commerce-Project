package Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.List;

public class CartPage extends BasePage {

    private final By cartItems = By.className("cart_item");
    private final By cartItemNames = By.className("inventory_item_name");
    private final By checkoutButton = By.id("checkout");
    private final By continueShoppingButton = By.id("continue-shopping");

    public CartPage(WebDriver driver) {
        super(driver);
    }

    private By removeButtonFor(String productName) {
        return By.cssSelector(String.format("[data-test=\"remove-%s\"]", toDataTestId(productName)));
    }

    public int getCartItemsCount() {
        return driver.findElements(cartItems).size();
    }

    public boolean isCartEmpty() {
        return getCartItemsCount() == 0;
    }

    public List<String> getCartItemNames() {
        return driver.findElements(cartItemNames).stream().map(e -> e.getText().trim()).toList();
    }

    public void removeItem(String productName) {
        click(removeButtonFor(productName));
    }

    public boolean isItemInCart(String productName) {
        return !driver.findElements(removeButtonFor(productName)).isEmpty();
    }

    public void clickCheckout() {
        click(checkoutButton);
    }

    public void clickContinueShopping() {
        click(continueShoppingButton);
    }
}
