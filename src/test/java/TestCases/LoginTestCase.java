package TestCases;


import Pages.LoginPage;
import Utility.ExcelUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

public class LoginTestCase extends TestBase{
    LoginPage loginPage;

    @DataProvider(name = "loginData")
    public Object[][] getData(Method method) {
        String excelPath = "src/test/java/TestCases/resources/LoginData.xlsx";
        ExcelUtils excel = new ExcelUtils(excelPath, "Sheet1");

        int rowCount = excel.getRowCount();
        int colCount = excel.getColCount();

        Object data[][] = new Object[rowCount - 1][colCount];

        for (int i = 1; i < rowCount; i++) {
            for (int j = 0; j < colCount; j++) {
                data[i - 1][j] = excel.getCellData(i, j);
            }
        }
        return data;
    }

    @Test(dataProvider = "loginData")
    public void Testlogin(String username, String password, String ExpectedUrl){
        WebDriver base_driver = new ChromeDriver();
        loginPage = new LoginPage(base_driver);

        base_driver.get("https://www.saucedemo.com/");
        loginPage.setUsername(username);
        loginPage.setPassword(password);
        loginPage.clickonlogin();

        String actual = base_driver.getCurrentUrl();
        Assert.assertEquals(actual, ExpectedUrl);
        base_driver.quit();


    }

}
