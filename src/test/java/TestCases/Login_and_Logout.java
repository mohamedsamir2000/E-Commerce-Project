package TestCases;

import Pages.LoginPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import java.time.Duration;

public class Login_and_Logout extends TestBase{

    LoginPage loginPage;

    @Test(dataProvider = "login_TestCases")
    public void TestLogin(String username, String password, String ExpectedUrl){

        loginPage = new LoginPage(base_driver);

        System.out.println("Testing with: " + username + " | " + password);

        loginPage.setUsername(username);
        loginPage.setPassword(password);
        loginPage.clickonlogin();

        String Actual = base_driver.getCurrentUrl();
        Assert.assertEquals(Actual, ExpectedUrl, "Login failed!");

    }


    @Test(dataProvider = "loginData")
    public void TestLogout(String username, String password) {

        loginPage = new LoginPage(base_driver);

        // Login with valid credentials
        loginPage.setUsername(username);
        loginPage.setPassword(password);
        loginPage.clickonlogin();
        // Verify login success
        Assert.assertTrue(base_driver.getCurrentUrl().contains("inventory.html"), "Login failed!");

        //Click on the menu button
        WebElement menuButton = base_driver.findElement(By.id("react-burger-menu-btn"));
        menuButton.click();

        // Wait for the menu to appear
        Wait<WebDriver> wait = new WebDriverWait(base_driver, Duration.ofSeconds(2));
        wait.until(d -> base_driver.findElement(By.id("logout_sidebar_link")).isDisplayed());

        //Click on the Logout button
        WebElement logoutButton = base_driver.findElement(By.id("logout_sidebar_link"));
        logoutButton.click();

        //Verify that we are redirected to the login page
        Assert.assertEquals(base_driver.getCurrentUrl(), "https://www.saucedemo.com/","Logout failed!");


        System.out.println("Logout test passed successfully!");
    }

}



