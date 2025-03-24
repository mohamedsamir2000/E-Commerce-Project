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


    //Lists of items elements


        WebElement dropdownElement = driver.findElement(By.className("product_sort_container"));



    //for By elements
    public void ClickOn(By Button){

        driver.findElement(Button).click();
    }
    //for WebElements
    public void ClickOn(WebElement button) {
        button.click();
    }
}
