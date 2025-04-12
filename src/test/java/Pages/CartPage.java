package Pages;

import org.apache.xmlbeans.impl.xb.xsdschema.Public;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.time.Duration;
import java.util.List;
import java.util.NoSuchElementException;

public class CartPage {
    WebDriver driver;
    private WebDriverWait wait;
    public CartPage(WebDriver driver){

        this.driver = driver;
    }
    public By CartIcon=By.xpath("//*[@id=\"shopping_cart_container\"]/a");
    public By CheckoutButton=By.id("checkout");
    public By ContinueShoppingButton = By.id("continue-shopping");
    public By FirstNameinput=By.id("first-name");
    public By LastNameinput=By.id("last-name");
    public By PostalCode=By.id("postal-code");
    public By ContinueButton=By.id("continue");
    public By CancelButton=By.id("cancel");
    public By FinishButton=By.id("finish");
    private By successMessage = By.className("complete-header");
    private By errorMsg = By.cssSelector("h3[data-test='error']");
    private By itemPrices = By.className("inventory_item_price");
    private By itemTotalLabel = By.className("summary_subtotal_label");
    private By taxLabel = By.className("summary_tax_label");
    private By totalLabel = By.className("summary_total_label");


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
    //Click checkout
    public void clickCheckout() {
        driver.findElement(CheckoutButton).click();
    }
    //click continue shopping
    public void clickContinueShopping() {
        driver.findElement(ContinueShoppingButton).click();
    }
    //fill checkout form info
    public void fillCheckoutInfo(String firstName, String lastName, String zip) {
        driver.findElement(FirstNameinput).sendKeys(firstName);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));

        try {
            WebElement lastNameInput = wait.until(ExpectedConditions.visibilityOfElementLocated(LastNameinput));

            if (lastNameInput.isDisplayed() && lastNameInput.isEnabled()) {
                lastNameInput.clear();  // Optional: clear existing value
                lastNameInput.sendKeys(lastName);
            } else {
                System.out.println("Last name field is not interactable. Skipping input.");
            }

        } catch (NoSuchElementException | TimeoutException e) {
            System.out.println(" Last name field not found. Skipping input.");
            e.printStackTrace();
        }

        driver.findElement(PostalCode).sendKeys(zip);
    }


    public String getErrorMessage() {
        return driver.findElement(errorMsg).getText();
    }

    public void clickContinue() {
       // driver.findElement(ContinueButton).click();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.elementToBeClickable(ContinueButton)).click();
    }
    public void clickFinish() {

        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
            WebElement finishBtn = wait.until(ExpectedConditions.elementToBeClickable(FinishButton));
            finishBtn.click();
        } catch (NoSuchElementException | TimeoutException e) {
            System.out.println("Finish button not found or not clickable.");
            e.printStackTrace();
            Assert.fail("Test failed due to missing Finish button.");
        }

    }

    public String getSuccessMessage() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        return wait.until(ExpectedConditions.visibilityOfElementLocated(successMessage)).getText();
       // return driver.findElement(successMessage).getText();
    }
    public List<Double> getItemPrices() {
        return driver.findElements(itemPrices).stream()
                .map(e -> Double.parseDouble(e.getText().replace("$", "")))
                .toList();
    }

    public double getItemTotal() {


        String text2 = driver.findElement(itemTotalLabel).getText(); // "Item total: $39.98"

        return Double.parseDouble(text2.replace("Item total: $", ""));
    }

    public void verifySubtotalForUser(String username) {
        if (username.equals("problem_user")) {
            System.out.println("Skipping subtotal check for 'problem_user' – UI is intentionally broken.");
            Assert.fail("Subtotal label missing when it should be present.");
           // return;
        }

    }

    public double getTax() {
        String text = driver.findElement(taxLabel).getText(); // "Tax: $3.20"
        return Double.parseDouble(text.replace("Tax: $", ""));
    }
    public double getTotal() {
        String text = driver.findElement(totalLabel).getText(); // "Total: $43.18"
        return Double.parseDouble(text.replace("Total: $", ""));
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
