package TestCases;

import Pages.HomePage;
import Pages.LoginPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;


import java.time.Duration;
import java.util.ArrayList;
import java.util.List;


public class Menu_and_Icons extends TestBase {
    LoginPage loginPage;
    HomePage homePage;

    @Test(dataProvider = "loginData")
    public void Test(String username, String password) {
        loginPage = new LoginPage(base_driver);
        homePage = new HomePage(base_driver);

        loginPage.setUsername(username);
        loginPage.setPassword(password);
        loginPage.clickonlogin();
    }
        @Test
        public void ClickAllItems () {
            loginPage = new LoginPage(base_driver);
            homePage = new HomePage(base_driver);
            loginPage.setUsername("standard_user");
            loginPage.setPassword("secret_sauce");
            loginPage.clickonlogin();

            base_driver.findElement(By.id("react-burger-menu-btn")).click();
            Actions a = new Actions(base_driver);
            WebElement n = base_driver.findElement(By.xpath("//a[@id='inventory_sidebar_link']"));
            a.doubleClick().build().perform();
            String actualpage = base_driver.getCurrentUrl();

            String expectedpage = "https://www.saucedemo.com/inventory.html";
            Assert.assertEquals(actualpage, expectedpage);

        }



          @Test
          public void clickAbout () {
            loginPage = new LoginPage(base_driver);
            homePage = new HomePage(base_driver);
            loginPage.setUsername("standard_user");
            loginPage.setPassword("secret_sauce");
            loginPage.clickonlogin();

            base_driver.findElement(By.id("react-burger-menu-btn")).click();
            Actions a = new Actions(base_driver);
            WebElement n = base_driver.findElement(By.xpath("//a[@id='about_sidebar_link']"));
            a.doubleClick().build().perform();

        String expectedUrl = "https://saucelabs.com/";
        WebDriverWait wait = new WebDriverWait(base_driver, Duration.ofSeconds(10));

        try {
            wait.until(ExpectedConditions.urlToBe(expectedUrl));
            System.out.println("URL is now: " + base_driver.getCurrentUrl());
        } catch (org.openqa.selenium.TimeoutException e) {
            System.err.println("Timeout waiting for URL to be: " + expectedUrl);
        }

        }
             @Test
             public void Clickontwitter(){

                 loginPage = new LoginPage(base_driver);
                 homePage = new HomePage(base_driver);
                 loginPage.setUsername("standard_user");
                 loginPage.setPassword("secret_sauce");
                 loginPage.clickonlogin();

        base_driver.findElement(By.linkText("Twitter")).click();
         //base_driver.switchTo().window("https://x.com/saucelabs");
         WebDriverWait wait = new WebDriverWait(base_driver, Duration.ofSeconds(10));
                  wait.until(ExpectedConditions.numberOfWindowsToBe(2));
                 List<String> tabs = new ArrayList<>(base_driver.getWindowHandles());
                  base_driver.switchTo().window(tabs.get(1));
                 wait.until(ExpectedConditions.urlContains("https://x.com/saucelabs"));
                 Assert.assertTrue(base_driver.getCurrentUrl().contains("https://x.com/saucelabs"),
                         "Failed to switch to the expected tab!");
                 System.out.println("Assertion passed: Switched to the correct tab!");
    }

    @Test
    public void Clickonfacebook(){

        loginPage = new LoginPage(base_driver);
        homePage = new HomePage(base_driver);
        loginPage.setUsername("standard_user");
        loginPage.setPassword("secret_sauce");
        loginPage.clickonlogin();

        base_driver.findElement(By.linkText("Facebook")).click();
        WebDriverWait wait = new WebDriverWait(base_driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.numberOfWindowsToBe(2));
        List<String> tabs = new ArrayList<>(base_driver.getWindowHandles());
        base_driver.switchTo().window(tabs.get(1));
        wait.until(ExpectedConditions.urlContains("https://www.facebook.com/saucelabs"));
        Assert.assertTrue(base_driver.getCurrentUrl().contains("https://www.facebook.com/saucelabs"),
                "Failed to switch to the expected tab!");
        System.out.println("Assertion passed: Switched to the correct tab!");
    }

    @Test
    public void ClickonLinkedIn(){

        loginPage = new LoginPage(base_driver);
        homePage = new HomePage(base_driver);
        loginPage.setUsername("standard_user");
        loginPage.setPassword("secret_sauce");
        loginPage.clickonlogin();

        base_driver.findElement(By.linkText("LinkedIn")).click();
        WebDriverWait wait = new WebDriverWait(base_driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.numberOfWindowsToBe(2));
        List<String> tabs = new ArrayList<>(base_driver.getWindowHandles());
        base_driver.switchTo().window(tabs.get(1));
        wait.until(ExpectedConditions.urlContains("https://www.linkedin.com/company/sauce-labs/"));
        Assert.assertTrue(base_driver.getCurrentUrl().contains("https://www.linkedin.com/company/sauce-labs/"),
                "Failed to switch to the expected tab!");
        System.out.println("Assertion passed: Switched to the correct tab!");
    }



              }


