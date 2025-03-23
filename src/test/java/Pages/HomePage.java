package Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class HomePage {

    WebDriver driver;
    public HomePage(WebDriver driver){

        this.driver = driver;
    }

    //Page elements

    By SwagLabsLogo = By.className("app_logo");
    By Cart_Btn = By.className("shopping_cart_link");
    By Menu = By.id("react-burger-menu-btn");
    By AllItems_Menu = By.xpath("//*[@id=\"inventory_sidebar_link\"]");
    By About_Menu = By.xpath("//*[@id=\"about_sidebar_link\"]");
    By Logout_Menu = By.xpath("//*[@id=\"logout_sidebar_link\"]");
    By ResetAppState_Menu = By.xpath("//*[@id=\"reset_sidebar_link\"]");
    By Twitter_btn = By.xpath("//*[@id=\"page_wrapper\"]/footer/ul/li[1]/a");
    By Facebook_btn = By.xpath("//*[@id=\"page_wrapper\"]/footer/ul/li[2]/a");
    By Likedin_btn = By.xpath("//*[@id=\"page_wrapper\"]/footer/ul/li[3]/a");

    //Lists of items elements

    List<WebElement> ItemsName = driver.findElements(By.className("inventory_item_name"));
    List<WebElement> ItemsPrice = driver.findElements(By.className("inventory_item_price"));
    List<WebElement> ItemsAddToCart = driver.findElements(By.className("btn btn_primary btn_small btn_inventory"));

    //filter dropdown list
    WebElement dropdownElement = driver.findElement(By.className("product_sort_container"));
    // Create Select object
    //Select dropdown = new Select(dropdownElement);

    // Select option by visible text
    //dropdown.selectByVisibleText("Option 1");


    //for By elements
    public void ClickOn(By Button){

        driver.findElement(Button).click();
    }
    //for WebElements
    public void ClickOn(WebElement button) {
        button.click();
    }
}
