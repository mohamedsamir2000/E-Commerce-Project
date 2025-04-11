package Pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class HomePage {

    WebDriver driver;
    public HomePage(WebDriver driver){

        this.driver = driver;
    }

    //Page elements

    public By SwagLabsLogo = By.className("app_logo");
    public By Cart_Btn = By.className("shopping_cart_link");
    public By Menu = By.id("react-burger-menu-btn");
    public By AllItems_Menu = By.xpath("//*[@id=\"inventory_sidebar_link\"]");
    public By About_Menu = By.xpath("//*[@id=\"about_sidebar_link\"]");
    public By Logout_Menu = By.xpath("//*[@id=\"logout_sidebar_link\"]");
    public By ResetAppState_Menu = By.xpath("//*[@id=\"reset_sidebar_link\"]");
    public By Twitter_btn = By.xpath("//*[@id=\"page_wrapper\"]/footer/ul/li[1]/a");
    public By Facebook_btn = By.xpath("//*[@id=\"page_wrapper\"]/footer/ul/li[2]/a");
    public By Likedin_btn = By.xpath("//*[@id=\"page_wrapper\"]/footer/ul/li[3]/a");
    public By removeButtons = By.xpath("//button[text()='Remove']");
    public By addToCartButtons=By.xpath("//button[text()='Add to cart']");
    public By inventoryContainer = By.id("inventory_container");
    //product details Locators
    private By backButton = By.id("back-to-products");
    private By productLinks = By.className("inventory_item_name");

    // Locator for the shopping cart icon
    public By cartIcon = By.className("shopping_cart_link");

    // Method to click on the cart icon to navigate to the cart page
    public void clickOnCart() {
        driver.findElement(cartIcon).click();
    }

    //Lists of items elements

    public List<String> getItemsName() {
        return driver.findElements(By.className("inventory_item_name")).stream().map(WebElement::getText).toList();  // Collect as List<String>

    }

    public List<String> getItemsPrice() {

        return driver.findElements(By.className("inventory_item_price")).stream().map(WebElement::getText).toList();
    }

//    public List<WebElement> getItemsAddToCart() {
//        return driver.findElements(By.className("btn btn_primary btn_small btn_inventory"));
//    }
//    public List<WebElement> GetremovesButtons(){
//        return driver.findElements(By.xpath("//button[text()='Remove']"));
//    }
    public boolean isInventoryDisplayed(){
       return driver.findElement(inventoryContainer).isDisplayed();
    }
   public int getInventoryItemCount(){
        return driver.findElements(By.className("inventory_item")).size();
   }

    public  void clickAllAddToCartButtons() {
        List<WebElement> addButtons = driver.findElements(addToCartButtons);

        //System.out.println("Total Add to Cart buttons found: " + addButtons.size());

        for (WebElement button : addButtons) {
            button.click();
        }
    }
    public  void clickAllRemoveButtons() {
        List<WebElement> removeButtonsList = driver.findElements(removeButtons);

        //System.out.println("Total Add to Cart buttons found: " + removeButtonsList.size());

        for (WebElement button : removeButtonsList) {
            button.click();
        }
    }
    public int getRemoveButtonsCount() {
        return driver.findElements(removeButtons).size();
    }
    //get cart count to check it
    public int getCartItemCount() {
        try {
            WebElement cartBadgeElement = driver.findElement(Cart_Btn);
            return Integer.parseInt(cartBadgeElement.getText());
        } catch (Exception e) {
            return 0; // No badge means no items in the cart
        }
    }
    //for product details page Actions
    public void AddItemtoCart(String Xpath){
        driver.findElement(By.xpath(Xpath)).click();
    }
    public void CLickAddtoCartAndBack(){
        driver.findElement(By.xpath("//button[text()='Add to cart']")).click();
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        //wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[text()='Add to cart']")));
        driver.findElement(backButton).click();
    }
    public void ClickRemoveThenBack(){
        driver.findElement(removeButtons).click();
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        driver.findElement(backButton).click();


    }



    //sort dropdown list
    //Method to select by index
    public boolean verifySortingByName(int sortIndex) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));

        try {
            // Locate dropdown and select sorting option by index
            WebElement dropdown = wait.until(ExpectedConditions.presenceOfElementLocated(By.className("product_sort_container")));
            Select select = new Select(dropdown);
            select.selectByIndex(sortIndex); // 0 = A to Z, 1 = Z to A

            // Wait for items to be sorted
            wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.className("inventory_item_name")));

        } catch (Exception e) {
            System.out.println("Exception occurred while selecting sorting option: " + e.getMessage());

            try {
                Alert alert = driver.switchTo().alert();
                System.out.println("Unexpected Alert Found: " + alert.getText());
                alert.accept(); // Accept (dismiss) the alert
                System.out.println("Alert dismissed.");

                // Retry selecting the sorting option after dismissing the alert
                WebElement dropdown = wait.until(ExpectedConditions.presenceOfElementLocated(By.className("product_sort_container")));
                Select select = new Select(dropdown);
                select.selectByIndex(sortIndex);

                // Wait for sorted elements
                wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.className("inventory_item_name")));

            } catch (NoAlertPresentException noAlert) {
                System.out.println("No alert found after exception.");
            }
        }

        // Extract product names dynamically from the webpage
        List<WebElement> nameElements = driver.findElements(By.className("inventory_item_name"));
        List<String> extractedNames = new ArrayList<>();

        for (WebElement nameElement : nameElements) {
            extractedNames.add(nameElement.getText().trim());
        }

        // Sort a copy of the extracted names to compare
        List<String> expectedSortedNames = new ArrayList<>(extractedNames);

        if (sortIndex == 0) { // Name A to Z
            Collections.sort(expectedSortedNames);
        } else if (sortIndex == 1) { // Name Z to A
            expectedSortedNames.sort(Collections.reverseOrder());
        } else {
            throw new IllegalArgumentException("Invalid index for name sorting: " + sortIndex);
        }

        // Compare the extracted names (from UI) with the expected sorted list
        return extractedNames.equals(expectedSortedNames);
    }


    public boolean verifySortingByPrice(int sortIndex) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));

        try {
            // Locate dropdown and select sorting option by index
            WebElement dropdown = wait.until(ExpectedConditions.presenceOfElementLocated(By.className("product_sort_container")));
            Select select = new Select(dropdown);
            select.selectByIndex(sortIndex); // 2 = Low to High, 3 = High to Low

            // Wait for items to be sorted
            wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.className("inventory_item_price")));

        } catch (Exception e) {
            System.out.println("Exception occurred while selecting sorting option: " + e.getMessage());

            try {
                Alert alert = driver.switchTo().alert();
                System.out.println("Unexpected Alert Found: " + alert.getText());
                alert.accept(); // Accept (dismiss) the alert
                System.out.println("Alert dismissed.");

                // Retry selecting the sorting option after dismissing the alert
                WebElement dropdown = wait.until(ExpectedConditions.presenceOfElementLocated(By.className("product_sort_container")));
                Select select = new Select(dropdown);
                select.selectByIndex(sortIndex);

                // Wait for sorted elements
                wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.className("inventory_item_price")));

            } catch (NoAlertPresentException noAlert) {
                System.out.println("No alert found after exception.");
            }
        }

        // Extract prices dynamically from the webpage
        List<WebElement> priceElements = driver.findElements(By.className("inventory_item_price"));
        List<Double> extractedPrices = new ArrayList<>();

        for (WebElement priceElement : priceElements) {
            String priceText = priceElement.getText().replace("$", "").trim(); // Remove '$' and spaces
            extractedPrices.add(Double.parseDouble(priceText));
        }

        // Sort a copy of the extracted prices to compare
        List<Double> expectedSortedPrices = new ArrayList<>(extractedPrices);

        if (sortIndex == 2) { // Price Low to High
            Collections.sort(expectedSortedPrices);
        } else if (sortIndex == 3) { // Price High to Low
            expectedSortedPrices.sort(Collections.reverseOrder());
        } else {
            throw new IllegalArgumentException("Invalid index for price sorting: " + sortIndex);
        }

        // Compare the extracted prices (from UI) with the expected sorted list
        return extractedPrices.equals(expectedSortedPrices);
    }



    //for By elements
    public void ClickOn(By Button){

        driver.findElement(Button).click();
    }


    //for WebElements
    public void ClickOn(WebElement button) {

        button.click();
    }
}
