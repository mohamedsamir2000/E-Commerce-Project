
import Utility.ExcelUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class TestLogin {
    @DataProvider(name = "loginData")
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
