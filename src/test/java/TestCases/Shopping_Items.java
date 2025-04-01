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
        String[] Allxpaths={"//*[@id=\"item_4_title_link\"]/div","//*[@id=\"item_0_title_link\"]/div","//*[@id=\"item_1_title_link\"]/div","//*[@id=\"item_5_title_link\"]/div","//*[@id=\"item_2_title_link\"]/div","//*[@id=\"item_3_title_link\"]/div"};
        for(int i=0;i<Allxpaths.length;i++){
            homePage.AddItemtoCart(Allxpaths[i]);
            homePage.CLickAddtoCartAndBack();
        }
        int CartItemsCount=homePage.getCartItemCount();
        Assert.assertEquals(CartItemsCount,Allxpaths.length,"Cart count not correct");
        //Test to open all product again and remove them again
        for(int i=0;i<Allxpaths.length;i++){
            homePage.AddItemtoCart(Allxpaths[i]);
            homePage.ClickRemoveThenBack();
        }
        //get items count afterRemove all products
        int AfterCartItemsCount=homePage.getCartItemCount();
        System.out.println(AfterCartItemsCount);
        Assert.assertEquals(0,AfterCartItemsCount,"Cart count not correct");



    }
}
