package TestCases;

import Pages.HomePage;
import Pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.Test;


public class Sort extends TestBase{
    LoginPage loginPage;
    HomePage homePage;

    @Test(dataProvider = "loginData")
    public void TC_10_Verify_items_can_be_sorted_by_price_low_to_high(String username, String password){
        loginPage = new LoginPage(base_driver);
        homePage = new HomePage(base_driver);

        // Login
        loginPage.setUsername(username);
        loginPage.setPassword(password);
        loginPage.clickonlogin();

        // Verify sorting by price (Low to High)
        Assert.assertTrue(homePage.verifySortingByPrice(2), "Items are not sorted correctly from Low to High");
    }

    @Test(dataProvider = "loginData")
    public void TC_11_Verify_items_can_be_sorted_by_price_high_to_low(String username, String password){
        loginPage = new LoginPage(base_driver);
        homePage = new HomePage(base_driver);

        // Login
        loginPage.setUsername(username);
        loginPage.setPassword(password);
        loginPage.clickonlogin();

        Assert.assertTrue(homePage.verifySortingByPrice(3), "Items are not sorted correctly from High to Low");
    }


    @Test(dataProvider = "loginData")
    public void TC_12_Verify_items_can_be_sorted_alphabetically_AtoZ(String username, String password){
        loginPage = new LoginPage(base_driver);
        homePage = new HomePage(base_driver);


        //login
        loginPage.setUsername(username);
        loginPage.setPassword(password);
        loginPage.clickonlogin();

        Assert.assertTrue(homePage.verifySortingByName(0), "Sorting from A to Z failed!");

    }

    @Test(dataProvider = "loginData")
    public void TC_13_Verify_items_can_be_sorted_alphabetically_ZtoA(String username, String password){
        loginPage = new LoginPage(base_driver);
        homePage = new HomePage(base_driver);

        // Login
        loginPage.setUsername(username);
        loginPage.setPassword(password);
        loginPage.clickonlogin();

        Assert.assertTrue(homePage.verifySortingByName(1), "Sorting from Z to A failed!");

    }
}
