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
    public void TC_14_Verify_a_single_item_can_be_removed_from_the_cart_page(String username, String password) {
        loginPage = new LoginPage(base_driver);
        homePage = new HomePage(base_driver);
        cartPage = new CartPage(base_driver);
        WebDriverWait wait = new WebDriverWait(base_driver, Duration.ofSeconds(10));

        // Step 1: Login
        loginPage.setUsername(username);
        loginPage.setPassword(password);
        loginPage.clickonlogin();

        // Step 2: List of all item identifiers
        String[] itemDataTests = {
                "sauce-labs-backpack",
                "sauce-labs-bike-light",
                "sauce-labs-bolt-t-shirt",
                "sauce-labs-fleece-jacket",
                "sauce-labs-onesie",
                "test.allthethings()-t-shirt-(red)"
        };

        // Step 3: Iterate through items
        for (String item : itemDataTests) {
            try {
                // Add item from home page
                String addButtonSelector = String.format("*[data-test=\"add-to-cart-%s\"]", item);
                WebElement addButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(addButtonSelector)));
                addButton.click();

                // Confirm item added (check for remove button)
                String removeButtonSelector = String.format("*[data-test=\"remove-%s\"]", item);
                WebElement removeBtn = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(removeButtonSelector)));
                Assert.assertTrue(removeBtn.isDisplayed(), item + " was not added to the cart.");

                // Navigate to cart
                base_driver.findElement(By.className("shopping_cart_link")).click();

                // Remove item from cart page
                WebElement removeFromCart = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(removeButtonSelector)));
                removeFromCart.click();

                // Confirm removal (remove button disappears)
                List<WebElement> remaining = base_driver.findElements(By.cssSelector(removeButtonSelector));
                Assert.assertTrue(remaining.isEmpty(), item + " was not removed from the cart.");

                // Go back to home page for next item
                base_driver.findElement(By.id("continue-shopping")).click();

            } catch (Exception e) {
                Assert.fail("Error while processing item: " + item + ". Error: " + e.getMessage());
            }
        }
    }

    @Test(dataProvider = "loginData")
    public void TC_15_Verify_a_single_item_can_be_removed_from_the_main_page(String username, String password) {
        loginPage = new LoginPage(base_driver);
        homePage = new HomePage(base_driver);

        // Step 1: Login
        loginPage.setUsername(username);
        loginPage.setPassword(password);
        loginPage.clickonlogin();

        // Step 2: List of all item identifiers
        String[] itemDataTests = {
                "sauce-labs-backpack",
                "sauce-labs-bike-light",
                "sauce-labs-bolt-t-shirt",
                "sauce-labs-fleece-jacket",
                "sauce-labs-onesie",
                "test.allthethings()-t-shirt-(red)"
        };

        WebDriverWait wait = new WebDriverWait(base_driver, Duration.ofSeconds(10));

        // Step 3: Add all items to the cart and verify
        for (String item : itemDataTests) {
            try {
                String addButtonSelector = String.format("*[data-test=\"add-to-cart-%s\"]", item);
                WebElement addButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(addButtonSelector)));
                addButton.click();

                String removeButtonSelector = String.format("*[data-test=\"remove-%s\"]", item);
                WebElement removeButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(removeButtonSelector)));
                Assert.assertTrue(removeButton.isDisplayed(), item + " was not added to the cart.");
            } catch (Exception e) {
                Assert.fail("Failed to add item: " + item + ". Error: " + e.getMessage());
            }
        }

        // Step 4: Remove all items from the cart and verify
        for (String item : itemDataTests) {
            try {
                String removeButtonSelector = String.format("*[data-test=\"remove-%s\"]", item);
                WebElement removeButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(removeButtonSelector)));
                removeButton.click();

                String addButtonSelector = String.format("*[data-test=\"add-to-cart-%s\"]", item);
                WebElement addButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(addButtonSelector)));
                Assert.assertTrue(addButton.isDisplayed(), item + " was not removed from the cart.");
            } catch (Exception e) {
                Assert.fail("Failed to remove item: " + item + ". Error: " + e.getMessage());
            }
        }
    }

    @Test(dataProvider = "loginData")
    public void TC_16_Verify_all_items_can_be_removed_from_the_cart_page(String username, String password) {
        loginPage = new LoginPage(base_driver);
        homePage = new HomePage(base_driver);
        cartPage = new CartPage(base_driver);
        WebDriverWait wait = new WebDriverWait(base_driver, Duration.ofSeconds(10));

        // Step 1: Login
        loginPage.setUsername(username);
        loginPage.setPassword(password);
        loginPage.clickonlogin();

        // Step 2: List of all item identifiers
        String[] itemDataTests = {
                "sauce-labs-backpack",
                "sauce-labs-bike-light",
                "sauce-labs-bolt-t-shirt",
                "sauce-labs-fleece-jacket",
                "sauce-labs-onesie",
                "test.allthethings()-t-shirt-(red)"
        };

        // Step 3: Add all items to the cart from Home Page
        for (String item : itemDataTests) {
            try {
                String addButtonSelector = String.format("*[data-test=\"add-to-cart-%s\"]", item);
                WebElement addButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(addButtonSelector)));
                addButton.click();

                // Confirm item is added
                String removeButtonSelector = String.format("*[data-test=\"remove-%s\"]", item);
                WebElement removeBtn = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(removeButtonSelector)));
                Assert.assertTrue(removeBtn.isDisplayed(), item + " was not added to the cart.");

            } catch (Exception e) {
                Assert.fail("Failed to add item: " + item + ". Error: " + e.getMessage());
            }
        }

        // Step 4: Navigate to Cart Page once
        base_driver.findElement(By.className("shopping_cart_link")).click();

        // Step 5: Remove all items from Cart Page
        for (String item : itemDataTests) {
            try {
                String removeButtonSelector = String.format("*[data-test=\"remove-%s\"]", item);
                WebElement removeFromCart = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(removeButtonSelector)));
                removeFromCart.click();

                // Confirm removal
                List<WebElement> remaining = base_driver.findElements(By.cssSelector(removeButtonSelector));
                Assert.assertTrue(remaining.isEmpty(), item + " was not removed from the cart.");

            } catch (Exception e) {
                Assert.fail("Failed to remove item from cart: " + item + ". Error: " + e.getMessage());
            }
        }

        // Step 6: Final check – cart should be empty
        Assert.assertFalse(cartPage.isItemInCart(), "Cart is not empty after removing all items.");
    }
    @Test(dataProvider = "loginData")
    public void TC_17_Verify_all_items_can_be_removed_from_the_main_page(String username, String password) {
        loginPage = new LoginPage(base_driver);
        homePage = new HomePage(base_driver);
        cartPage = new CartPage(base_driver);
        WebDriverWait wait = new WebDriverWait(base_driver, Duration.ofSeconds(10));

        // Step 1: Login
        loginPage.setUsername(username);
        loginPage.setPassword(password);
        loginPage.clickonlogin();

        // Step 2: List of all item identifiers
        String[] itemDataTests = {
                "sauce-labs-backpack",
                "sauce-labs-bike-light",
                "sauce-labs-bolt-t-shirt",
                "sauce-labs-fleece-jacket",
                "sauce-labs-onesie",
                "test.allthethings()-t-shirt-(red)"
        };

        // Step 3: Add all items from Home Page
        for (String item : itemDataTests) {
            try {
                String addButtonSelector = String.format("*[data-test=\"add-to-cart-%s\"]", item);
                WebElement addButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(addButtonSelector)));
                addButton.click();

                String removeButtonSelector = String.format("*[data-test=\"remove-%s\"]", item);
                WebElement removeBtn = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(removeButtonSelector)));
                Assert.assertTrue(removeBtn.isDisplayed(), item + " was not added properly from home page.");

            } catch (Exception e) {
                Assert.fail("Failed to add item from home page: " + item + ". Error: " + e.getMessage());
            }
        }

        // Step 4: Remove all items from Home Page
        for (String item : itemDataTests) {
            try {
                String removeButtonSelector = String.format("*[data-test=\"remove-%s\"]", item);
                WebElement removeBtn = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(removeButtonSelector)));
                removeBtn.click();

                List<WebElement> remaining = base_driver.findElements(By.cssSelector(removeButtonSelector));
                Assert.assertTrue(remaining.isEmpty(), item + " was not removed from the home page.");

            } catch (Exception e) {
                Assert.fail("Failed to remove item from home page: " + item + ". Error: " + e.getMessage());
            }
        }

        // Step 5: Final check – cart icon should show no count
        String cartBadge = "shopping_cart_badge";
        List<WebElement> cartItems = base_driver.findElements(By.className(cartBadge));
        Assert.assertTrue(cartItems.isEmpty(), "Cart is not empty after removing all items from home page.");
    }
    //checkout process is successfule
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
    //checkout with empty fields
    @Test(dataProvider = "loginData")
    public void testCheckoutWithEmptyFields(String username, String password) {
        WebDriverWait wait = new WebDriverWait(base_driver, Duration.ofSeconds(5));
        loginPage = new LoginPage(base_driver);
        homePage = new HomePage(base_driver);
        cartPage = new CartPage(base_driver);

        // Login
        loginPage.setUsername(username);
        loginPage.setPassword(password);
        loginPage.clickonlogin();
        // add some items to checkout
        cartPage.ClickOn(By.id("add-to-cart-sauce-labs-backpack"));
        cartPage.ClickOn(By.id("add-to-cart-sauce-labs-bike-light"));
        cartPage.ClickOn(By.className("shopping_cart_link"));
        cartPage.clickCheckout();

        // Leave fields empty and click Continue
        cartPage.fillCheckoutInfo("", "", "");
        cartPage.clickContinue();

        // Verify error message
        String error = cartPage.getErrorMessage();
        Assert.assertEquals(error, "Error: First Name is required", "Expected error for empty first name");
    }
    //check total price for user order is correct calculated
    @Test(dataProvider = "loginData")
    public void testCheckoutTotalIsCorrect(String username, String password) {
        WebDriverWait wait = new WebDriverWait(base_driver, Duration.ofSeconds(5));
        loginPage = new LoginPage(base_driver);
        homePage = new HomePage(base_driver);
        cartPage = new CartPage(base_driver);

        // Login
        loginPage.setUsername(username);
        loginPage.setPassword(password);
        loginPage.clickonlogin();
        // add some items to checkout
        cartPage.ClickOn(By.id("add-to-cart-sauce-labs-backpack"));
        cartPage.ClickOn(By.id("add-to-cart-sauce-labs-bike-light"));
        cartPage.ClickOn(By.className("shopping_cart_link"));
        cartPage.clickCheckout();

        // Enter valid data to continue
        cartPage.fillCheckoutInfo("Test", "Total", "12345");
        cartPage.clickContinue();
        // Validate prices and check the total price is correct
        List<Double> itemPrices = cartPage.getItemPrices();
        double expectedItemTotal = itemPrices.stream().mapToDouble(Double::doubleValue).sum();
        double displayedItemTotal = cartPage.getItemTotal();
        double displayedTax = cartPage.getTax();
        double displayedTotal = cartPage.getTotal();
        Assert.assertEquals(displayedItemTotal, expectedItemTotal, "Item total mismatch");
        Assert.assertEquals(displayedItemTotal + displayedTax, displayedTotal, "Total does not match item total + tax");

    }
    //check user can back to home page from cart page
    @Test(dataProvider = "loginData")
    public void testBackToInventoryFromCart(String username, String password) {
        WebDriverWait wait = new WebDriverWait(base_driver, Duration.ofSeconds(5));
        loginPage = new LoginPage(base_driver);
        homePage = new HomePage(base_driver);
        cartPage = new CartPage(base_driver);

        // Login
        loginPage.setUsername(username);
        loginPage.setPassword(password);
        loginPage.clickonlogin();
        // add some items to checkout
        cartPage.ClickOn(By.id("add-to-cart-sauce-labs-backpack"));
        cartPage.ClickOn(By.id("add-to-cart-sauce-labs-bike-light"));
        cartPage.ClickOn(By.className("shopping_cart_link"));
        // Click Continue Shopping
        cartPage.clickContinueShopping();

        // Verify we're back on the inventory page
        String currentUrl = base_driver.getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("inventory"), "User is not back on the inventory page");
    }

}

