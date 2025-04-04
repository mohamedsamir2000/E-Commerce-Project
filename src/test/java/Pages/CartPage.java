package Pages;

import org.apache.xmlbeans.impl.xb.xsdschema.Public;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

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




}
