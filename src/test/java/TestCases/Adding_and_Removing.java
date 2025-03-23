package TestCases;

import Pages.HomePage;
import Pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.Test;

import static org.apache.poi.sl.usermodel.PresetColor.Menu;


public class Adding_and_Removing extends TestBase{
    HomePage homePage;
    LoginPage loginPage;



    @Test(dataProvider = "loginData")
    public void Test(String username, String password, String ExpectedUrl){
        homePage = new HomePage(base_driver);
        loginPage = new LoginPage(base_driver);

        loginPage.setUsername(username);
        loginPage.setPassword(password);
        loginPage.clickonlogin();

        homePage.ClickOn(homePage.Facebook_btn);
        String name = homePage.getItemsName().get(1).getText();
        System.out.println(name);

        homePage.ClickOn(homePage.getItemsName().get(0));





    }

}
