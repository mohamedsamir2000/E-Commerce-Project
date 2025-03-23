package TestCases;


import Pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class Login_and_Logout extends TestBase{
    LoginPage loginPage;



    @Test(dataProvider = "loginData")
    public void Testlogin(String username, String password, String ExpectedUrl){

        loginPage = new LoginPage(base_driver);
        loginPage.setUsername(username);
        loginPage.setPassword(password);
        loginPage.clickonlogin();

        String actual = base_driver.getCurrentUrl();
        Assert.assertEquals(actual, ExpectedUrl);

    }

}
