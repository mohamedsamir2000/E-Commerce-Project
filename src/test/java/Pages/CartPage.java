package Pages;

import org.apache.xmlbeans.impl.xb.xsdschema.Public;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

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
        driver.findElement(LastNameinput).sendKeys(lastName);
        driver.findElement(PostalCode).sendKeys(zip);
    }


    public String getErrorMessage() {
        return driver.findElement(errorMsg).getText();
    }

    public void clickContinue() {
        driver.findElement(ContinueButton).click();
    }
    public void clickFinish() {
        driver.findElement(FinishButton).click();
    }

    public String getSuccessMessage() {
        return driver.findElement(successMessage).getText();
    }
    public List<Double> getItemPrices() {
        return driver.findElements(itemPrices).stream()
                .map(e -> Double.parseDouble(e.getText().replace("$", "")))
                .toList();
    }

    public double getItemTotal() {
        String text = driver.findElement(itemTotalLabel).getText(); // "Item total: $39.98"
        return Double.parseDouble(text.replace("Item total: $", ""));
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
