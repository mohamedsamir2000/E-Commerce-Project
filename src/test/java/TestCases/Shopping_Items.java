package TestCases;

import Pages.HomePage;
import Pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.Test;


public class Shopping_Items extends TestBase{
    HomePage homePage;
    LoginPage loginPage;



    @Test(dataProvider = "loginData")
    public void Test(String username, String password){
        loginPage = new LoginPage(base_driver);
        homePage = new HomePage(base_driver);


        loginPage.setUsername(username);
        loginPage.setPassword(password);
        loginPage.clickonlogin();

        homePage.ClickOn(homePage.Facebook_btn);







    }
    @Test(dataProvider = "loginData")
    public void AddAllItemsonCart(String username, String password){
        loginPage = new LoginPage(base_driver);
        homePage = new HomePage(base_driver);


        loginPage.setUsername(username);
        loginPage.setPassword(password);
        loginPage.clickonlogin();
        System.out.println(homePage.isInventoryDisplayed());
        int ItemCount=homePage.getInventoryItemCount();
        System.out.println("number of items in page to add to cart"+ItemCount);
        homePage.clickAllAddToCartButtons();
       int RemoveButtons= homePage.getRemoveButtonsCount();
        System.out.println("number of items i can remove after added it to cart "+RemoveButtons);
        Assert.assertEquals(RemoveButtons,ItemCount,"not all items added ");
       int CartItemsCount=homePage.getCartItemCount();
       Assert.assertEquals(CartItemsCount,RemoveButtons,"Cart count not correct");
       homePage.clickAllRemoveButtons();
       Assert.assertEquals(CartItemsCount,RemoveButtons,"can't remove all added items to cart");

    }
    //will edit it again
    @Test(dataProvider = "loginData")
    public void testAddToCartFromProductDetails(String username, String password) {
        homePage = new HomePage(base_driver);
        loginPage = new LoginPage(base_driver);

        loginPage.setUsername(username);
        loginPage.setPassword(password);
        loginPage.clickonlogin();
        int totalProducts = homePage.getAllProductLinks().size();
        int initialCartCount = homePage.getCartItemCount();
        System.out.println("initialCartCount"+initialCartCount);

        for (int i = 0; i < totalProducts; i++) {
            homePage.openProductByIndex(i);
            homePage.clickAddToCart();
           homePage.goBackToHomePage();
        }

        int finalCartCount = homePage.getCartItemCount();
        Assert.assertEquals(finalCartCount, initialCartCount + totalProducts, "Cart count is incorrect!");
    }




}
