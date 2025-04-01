package Pages;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class HomePage {

    WebDriver driver;
    private WebDriverWait wait;
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

        System.out.println("Total Add to Cart buttons found: " + addButtons.size());

        for (WebElement button : addButtons) {
            button.click();
        }
    }
    public  void clickAllRemoveButtons() {
        List<WebElement> removeButtonsList = driver.findElements(removeButtons);

        System.out.println("Total Add to Cart buttons found: " + removeButtonsList.size());

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
    public String selectSortingOptionByIndex(int index) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        try{

            // Re-fetch dropdown to avoid stale element reference
            WebElement dropdown = wait.until(ExpectedConditions.presenceOfElementLocated(By.className("product_sort_container")));

            Select select = new Select(dropdown);
            select.selectByIndex(index);

            // Re-fetch sorted elements after selection to prevent stale references
            wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.className("inventory_item_name")));

            // Get dropdown again to ensure it is the fresh reference
            dropdown = wait.until(ExpectedConditions.presenceOfElementLocated(By.className("product_sort_container")));
            select = new Select(dropdown);

            return select.getFirstSelectedOption().getText();
        } catch (Exception e){
            WebElement dropdown = wait.until(ExpectedConditions.presenceOfElementLocated(By.className("product_sort_container")));
            Select select = new Select(dropdown);
            select.selectByIndex(index);
            Alert alert = driver.switchTo().alert();
            System.out.println("Unexpected Alert Found: " + alert.getText());
            alert.accept(); // Accept (dismiss) the alert
            System.out.println("Alert dismissed.");


            // Re-fetch sorted elements after selection to prevent stale references
            wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.className("inventory_item_name")));

            // Get dropdown again to ensure it is the fresh reference
            dropdown = wait.until(ExpectedConditions.presenceOfElementLocated(By.className("product_sort_container")));
            select = new Select(dropdown);

            return select.getFirstSelectedOption().getText();
        }
    }

    public List<String> ListOfItems_NameAtoZ() {
        List<String> items = new ArrayList<>();
        items.add("Sauce Labs Backpack");
        items.add("Sauce Labs Bike Light");
        items.add("Sauce Labs Bolt T-Shirt");
        items.add("Sauce Labs Fleece Jacket");
        items.add("Sauce Labs Onesie");
        items.add("Test.allTheThings() T-Shirt (Red)");

        return items;
    }

    public List<String> ListOfItems_NameZtoA() {
        List<String> items = new ArrayList<>();
        items.add("Test.allTheThings() T-Shirt (Red)");
        items.add("Sauce Labs Onesie");
        items.add("Sauce Labs Fleece Jacket");
        items.add("Sauce Labs Bolt T-Shirt");
        items.add("Sauce Labs Bike Light");
        items.add("Sauce Labs Backpack");

        return items;
    }

    public List<String> ListOfItems_LowToHigh() {
        List<String> items = new ArrayList<>();
        items.add("$7.99");
        items.add("$9.99");
        items.add("$15.99");
        items.add("$15.99");
        items.add("$29.99");
        items.add("$49.99");

        return items;
    }

    public List<String> ListOfItems_HighToLow() {
        List<String> items = new ArrayList<>();

        items.add("$49.99");
        items.add("$29.99");
        items.add("$15.99");
        items.add("$15.99");
        items.add("$9.99");
        items.add("$7.99");

        return items;
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
