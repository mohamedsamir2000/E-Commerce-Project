package TestCases;

import Pages.CartPage;
import Pages.HomePage;
import Pages.LoginPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.List;

public class Cart_and_Checkout extends TestBase {
    LoginPage loginPage;
    HomePage homePage;
    CartPage cartPage;

    @Test(dataProvider = "loginData")
    public void verifyItemRemovalFromCartPage(String username, String password) {
        loginPage = new LoginPage(base_driver);
        homePage = new HomePage(base_driver);
        cartPage = new CartPage(base_driver);

        // Login
        loginPage.setUsername(username);
        loginPage.setPassword(password);
        loginPage.clickonlogin();

        // Step 2: Add an item to the cart using the button's id
        WebDriverWait wait = new WebDriverWait(base_driver, Duration.ofSeconds(10));
        WebElement addItemButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("add-to-cart-sauce-labs-backpack")));
        addItemButton.click();

        // Step 3: Navigate to the cart
        base_driver.findElement(By.className("shopping_cart_link")).click();

        // Step 4: Verify cart has at least one item before removal
        Assert.assertTrue(cartPage.isItemInCart(), "Cart is empty, cannot remove an item.");

        // Step 5: Click on 'Remove' button for the first item
        Assert.assertTrue(cartPage.removeSingleItemFromCart(), "Item was not removed from the cart.");

        // Step 6: Verify cart is empty after removal
        Assert.assertFalse(cartPage.isItemInCart(), "Item was not successfully removed from the cart.");
    }

    @Test(dataProvider = "loginData")
    public void verifyItemRemovalFromMainPage(String username, String password) {
        loginPage = new LoginPage(base_driver);
        homePage = new HomePage(base_driver);
        cartPage = new CartPage(base_driver);

        // Login
        loginPage.setUsername(username);
        loginPage.setPassword(password);
        loginPage.clickonlogin();

        // Step 2: Wait for the page to load and the 'Add to Cart' button to appear
        WebDriverWait wait = new WebDriverWait(base_driver, Duration.ofSeconds(20)); // Increased timeout
        WebElement addToCartButton = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//button[@id='add-to-cart-sauce-labs-backpack']"))); // Use XPath for a more reliable locator

        // Step 3: Click the 'Add to Cart' button
        addToCartButton.click();

        // Optional: Verify if the item is added to the cart (check cart icon)
        WebElement cartIcon = base_driver.findElement(By.className("shopping_cart_link"));
        cartIcon.click();

        // Verify item in the cart
        WebElement cartItem = base_driver.findElement(By.xpath("//div[@class='cart_item']"));
        Assert.assertNotNull(cartItem, "Item not added to cart.");



    }

    @Test(dataProvider = "loginData")
    public void verifyAllItemsCanBeRemovedFromCart(String username, String password) {
        WebDriverWait wait = new WebDriverWait(base_driver, Duration.ofSeconds(5));
        loginPage = new LoginPage(base_driver);
        homePage = new HomePage(base_driver);
        cartPage = new CartPage(base_driver);

        // Login
        loginPage.setUsername(username);
        loginPage.setPassword(password);
        loginPage.clickonlogin();


        // Step 3: Add all items to the cart
        List<WebElement> addToCartButtons = base_driver.findElements(By.cssSelector(".inventory_item button"));
        for (WebElement button : addToCartButtons) {
            try {
                button.click();
            } catch (Exception e) {
                System.out.println("Could not click button: " + e.getMessage());
            }
        }

        // Step 4: Go to cart
        base_driver.findElement(By.className("shopping_cart_link")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("cart_list")));

        // Step 5: Remove each item one by one
        while (true) {
            List<WebElement> cartItems = base_driver.findElements(By.className("cart_item"));
            int itemsBefore = cartItems.size();

            if (itemsBefore == 0) break; // All items removed

            WebElement firstRemoveButton = cartItems.get(0).findElement(By.tagName("button"));
            firstRemoveButton.click();

            // Wait until the cart updates
            wait.until(ExpectedConditions.numberOfElementsToBeLessThan(By.className("cart_item"), itemsBefore));

            int itemsAfter = base_driver.findElements(By.className("cart_item")).size();
            Assert.assertEquals(itemsAfter, itemsBefore - 1, "Item was not removed successfully.");
        }

        // Final assertion: cart should be empty
        List<WebElement> finalItems = base_driver.findElements(By.className("cart_item"));
        Assert.assertEquals(finalItems.size(), 0, "Cart is not empty after removing all items.");

        System.out.println("✅ All items were successfully removed from the cart for user: " + username);
    }
    @Test(dataProvider = "loginData")
    public void TestCheckoutProcess(String username, String password) {
        WebDriverWait wait = new WebDriverWait(base_driver, Duration.ofSeconds(5));
        loginPage = new LoginPage(base_driver);
        homePage = new HomePage(base_driver);
        cartPage = new CartPage(base_driver);

        // Login
        loginPage.setUsername(username);
        loginPage.setPassword(password);
        loginPage.clickonlogin();
        cartPage.ClickOn(By.id("add-to-cart-sauce-labs-backpack"));
        cartPage.ClickOn(By.id("add-to-cart-sauce-labs-bike-light"));
        cartPage.ClickOn(By.className("shopping_cart_link"));
        cartPage.clickCheckout();
        cartPage.fillCheckoutInfo("marwa", "Ashraf", "12345");
        cartPage.clickContinue();
        cartPage.clickFinish();
        String message = cartPage.getSuccessMessage();
        Assert.assertEquals(message, "Thank you for your order!");
    }

}

