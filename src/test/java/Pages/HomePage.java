package Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

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

    public List<WebElement> getItemsName() {

        return driver.findElements(By.className("inventory_item_name"));
    }

    public List<WebElement> getItemsPrice() {

        return driver.findElements(By.className("inventory_item_price"));
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
    // Click "Add to Cart" button product details
    public void clickAddToCart() {
        wait.until(ExpectedConditions.elementToBeClickable(addToCartButtons)).click();
    }

    // Go back to Home page product details
    public void goBackToHomePage() {
        wait.until(ExpectedConditions.elementToBeClickable(backButton)).click();
    }
    // Get all product links product details
    public List<WebElement> getAllProductLinks() {
        return wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(productLinks));
    }
    // Click on a product by index product details
    public void openProductByIndex(int index) {
        List<WebElement> products = getAllProductLinks();
        if (index < products.size()) {
            products.get(index).click();
        }
    }


    //sort dropdown list
    //Method to select by index
    public String selectSortingOptionByIndex(int index) {
        WebElement dropdownElement = driver.findElement(By.className("product_sort_container"));
        Select dropdown = new Select(dropdownElement);
        dropdown.selectByIndex(index);
        return dropdown.getFirstSelectedOption().getText();
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
