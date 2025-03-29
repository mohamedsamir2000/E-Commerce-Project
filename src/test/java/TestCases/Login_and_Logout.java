package TestCases;


import Utility.ExcelUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;


import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;


import java.time.Duration;

public class Login_and_Logout extends TestBase{



    @Test(dataProvider = "loginData_TestCases")
    public Object[][] getData() {
        String excelPath = "D:\\New Microsoft Excel Worksheet.xlsx"; // Ensure file exists
        ExcelUtils excel = new ExcelUtils(excelPath, "Sheet1");

        int rowCount = excel.getRowCount();
        int colCount = excel.getColCount();

        Object[][] data = new Object[rowCount][colCount];

        for (int i = 1; i <= rowCount; i++) {
            for (int j = 0; j < colCount; j++) {
                data[i - 1][j] = excel.getCellData(i, j);
            }
        }
        return data;
    }

    @Test(dataProvider = "loginData")
    public void loginData(String username, String password) {
        System.setProperty("webDriver.chrome.driver", "C:\\path\\to\\chromedriver.exe"); // Set path to ChromeDriver

        WebDriver driver = new ChromeDriver();

        driver.get("https://www.saucedemo.com/");

        System.out.println("Testing with: " + username + " | " + password);
        driver.findElement(By.id("user-name")).sendKeys(username);
        driver.findElement(By.id("password")).sendKeys(password);
        driver.findElement(By.id("login-button")).click();

        driver.quit();
    }
}
class TestLogout {
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



