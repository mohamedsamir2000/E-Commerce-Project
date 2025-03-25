package TestCases;

import Pages.HomePage;
import Pages.LoginPage;
import org.testng.annotations.Test;


public class Shopping_Items extends TestBase{
    HomePage homePage;
    LoginPage loginPage;



    @Test(dataProvider = "loginData_TestCases")
    public void Test(String username, String password){
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
