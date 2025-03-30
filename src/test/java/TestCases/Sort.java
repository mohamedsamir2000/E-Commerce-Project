package TestCases;

import Pages.HomePage;
import Pages.LoginPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Duration;


public class Sort extends TestBase{
    LoginPage loginPage;
    HomePage homePage;

    @Test(dataProvider = "loginData")
    public void NameAtoZ(String username, String password){
        loginPage = new LoginPage(base_driver);
        homePage = new HomePage(base_driver);


        //login
        loginPage.setUsername(username);
        loginPage.setPassword(password);
        loginPage.clickonlogin();

        String selectedOption = homePage.selectSortingOptionByIndex(0);

        Wait<WebDriver> wait = new WebDriverWait(base_driver, Duration.ofSeconds(1));
        wait.until(d -> base_driver.findElement(By.className("inventory_item_name")).isDisplayed());

        System.out.println("Selected Sorting Option: " + selectedOption);


        // Validate if the selected option is correct
        Assert.assertEquals(selectedOption, "Name (A to Z)");
        Assert.assertEquals(homePage.getItemsName(), homePage.ListOfItems_NameAtoZ());

    }

    @Test(dataProvider = "loginData")
    public void NameZtoA(String username, String password){
        loginPage = new LoginPage(base_driver);
        homePage = new HomePage(base_driver);

        // Login
        loginPage.setUsername(username);
        loginPage.setPassword(password);
        loginPage.clickonlogin();

        // Select sorting option
        String selectedOption = homePage.selectSortingOptionByIndex(1);
        System.out.println("Selected Sorting Option: " + selectedOption);

        // Validate selected option
        Assert.assertEquals(selectedOption, "Name (Z to A)");

        // Fetch fresh list after sorting
        Assert.assertEquals(homePage.getItemsName(), homePage.ListOfItems_NameZtoA());

    }

    @Test(dataProvider = "loginData")
    public void PriceLowToHigh(String username, String password){
        loginPage = new LoginPage(base_driver);
        homePage = new HomePage(base_driver);


        //login
        loginPage.setUsername(username);
        loginPage.setPassword(password);
        loginPage.clickonlogin();

        String selectedOption = homePage.selectSortingOptionByIndex(2);
        System.out.println("Selected Sorting Option: " + selectedOption);

        // Validate if the selected option is correct
        Assert.assertEquals(selectedOption, "Price (low to high)");
        Assert.assertEquals(homePage.getItemsPrice(), homePage.ListOfItems_LowToHigh());

    }

    @Test(dataProvider = "loginData")
    public void PriceHighToLow(String username, String password){
        loginPage = new LoginPage(base_driver);
        homePage = new HomePage(base_driver);


        //login
        loginPage.setUsername(username);
        loginPage.setPassword(password);
        loginPage.clickonlogin();

        String selectedOption = homePage.selectSortingOptionByIndex(3);
        System.out.println("Selected Sorting Option: " + selectedOption);

        // Validate if the selected option is correct
        Assert.assertEquals(selectedOption, "Price (high to low)");
        Assert.assertEquals(homePage.getItemsPrice(), homePage.ListOfItems_HighToLow());

    }



}
