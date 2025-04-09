package Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class CartPage {
    WebDriver driver;

    public CartPage(WebDriver driver) {
        this.driver = driver;
    }

    // Locator for the cart item
    public By cart_item_locator = By.className("cart_item");

    // Method to check if the cart contains any items
    public boolean isItemInCart() {
        List<WebElement> cartItems = driver.findElements(cart_item_locator);
        return cartItems.size() > 0;
    }

    //for By elements
    public void ClickOn(By Button) {

        driver.findElement(Button).click();
    }


    //for WebElements
    public void ClickOn(WebElement button) {

        button.click();
    }





    // Method to remove a single item from the cart
    public boolean removeSingleItemFromCart() {
        List<WebElement> cartItemsBefore = driver.findElements(cart_item_locator);
        int itemsBefore = cartItemsBefore.size();

        if (itemsBefore == 0) return false; // Return false if the cart is empty

        WebElement firstItem = cartItemsBefore.get(0);  // Get the first item in the cart
        firstItem.findElement(By.tagName("button")).click();  // Click the 'Remove' button

        // Verify if the item was removed
        List<WebElement> cartItemsAfter = driver.findElements(cart_item_locator);
        return cartItemsAfter.size() == itemsBefore - 1;


    }
}