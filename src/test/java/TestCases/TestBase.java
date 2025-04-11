package TestCases;

import Utility.ExcelUtils;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.*;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class TestBase {
    WebDriver base_driver;

    @BeforeMethod
    public void BeforeMethod() {

        ChromeOptions options = new ChromeOptions();
        // Disable Chrome password manager
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("credentials_enable_service", false);
        prefs.put("profile.password_manager_enabled", false);
        options.setExperimentalOption("prefs", prefs);

        // Run in Incognito mode to prevent password checkups
        options.addArguments("--incognito");

        // Optional: Disable infobars and notifications
        options.addArguments("disable-infobars");
        options.addArguments("--disable-notifications");
        options.addArguments("--disable-popup-blocking");


        base_driver = new ChromeDriver(options);
        base_driver.get("https://www.saucedemo.com/");
    }


    @DataProvider(name = "loginData")
    public Object[][] getData(Method method) {
        String excelPath = "src/test/java/Resources/LoginData.xlsx";
        ExcelUtils excel = new ExcelUtils(excelPath, "Sheet1");

        int rowCount = excel.getRowCount();
        int colCount = excel.getColCount();

        Object[][] data = new Object[rowCount - 1][colCount];

        for (int i = 1; i < rowCount; i++) {
            for (int j = 0; j < colCount; j++) {
                data[i - 1][j] = excel.getCellData(i, j);
            }
        }
        return data;
    }

    @DataProvider(name = "login_TestCases")
    public Object[][] getData_TestCases(Method method) {
        String excelPath = "src/test/java/Resources/LoginTestCases.xlsx";
        ExcelUtils excel = new ExcelUtils(excelPath, "Sheet1");

        int rowCount = excel.getRowCount();
        int colCount = excel.getColCount();

        Object[][] data = new Object[rowCount - 1][colCount];

        for (int i = 1; i < rowCount; i++) {
            for (int j = 0; j < colCount; j++) {
                data[i - 1][j] = excel.getCellData(i, j);
            }
        }
        return data;
    }

    @AfterMethod
    public void AfterMethod() {

        base_driver.quit();
    }
}
