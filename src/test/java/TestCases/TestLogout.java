

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.Duration;

public class TestLogout {
    WebDriver driver;

    @BeforeClass
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }

    @Test
    public void testLogout() {
        // Step 1: Open the website
        driver.get("https://www.saucedemo.com/");

        // Step 2: Login with valid credentials
        driver.findElement(By.id("user-name")).sendKeys("standard_user");
        driver.findElement(By.id("password")).sendKeys("secret_sauce");
        driver.findElement(By.id("login-button")).click();

        // Verify login success
        Assert.assertTrue(driver.getCurrentUrl().contains("inventory.html"), "Login failed!");

        // Step 3: Click on the menu button
        WebElement menuButton = driver.findElement(By.id("react-burger-menu-btn"));
        menuButton.click();

        // Wait for the menu to appear
        try { Thread.sleep(2000); } catch (InterruptedException e) { e.printStackTrace(); }

        // Step 4: Click on the Logout button
        WebElement logoutButton = driver.findElement(By.id("logout_sidebar_link"));
        logoutButton.click();

        // Step 5: Verify that we are redirected to the login page
        Assert.assertTrue(driver.getCurrentUrl().contains("saucedemo.com"), "Logout failed!");

        System.out.println("Logout test passed successfully!");
    }

    @AfterClass
    public void tearDown() {
        driver.quit();
    }
}


