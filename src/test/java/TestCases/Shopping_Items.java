package TestCases;

import Pages.HomePage;
import Pages.LoginPage;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Shopping_Items extends TestBase{
    HomePage homePage;
    LoginPage loginPage;


    //test that all items in home page can add it to cart then remove them
    @Test(dataProvider = "loginData")
    public void AddAllItemsonCart(String username, String password){
        loginPage = new LoginPage(base_driver);
        homePage = new HomePage(base_driver);

     //login
        loginPage.setUsername(username);
        loginPage.setPassword(password);
        loginPage.clickonlogin();
        //check if home page was displayed
        System.out.println(homePage.isInventoryDisplayed());
        //get count of all items in the page
        int ItemCount=homePage.getInventoryItemCount();
        //System.out.println("number of items in page to add to cart"+ItemCount);
        //then add all items in cart
        homePage.clickAllAddToCartButtons();
        //get count of remove buttons remo
       int RemoveButtons= homePage.getRemoveButtonsCount();
        //System.out.println("number of items i can remove after added it to cart "+RemoveButtons);
        //check that all items added to cart
        Assert.assertEquals(RemoveButtons,ItemCount,"not all items added ");
        //get cart count
       int CartItemsCount=homePage.getCartItemCount();
       //check that cart count equal items count that added to cart
       Assert.assertEquals(CartItemsCount,RemoveButtons,"Cart count not correct");
       //then remove all items
       homePage.clickAllRemoveButtons();
        System.out.println(homePage.getCartItemCount()+"cart count");
        //check that all items removed from cart
       Assert.assertEquals(CartItemsCount,RemoveButtons,"can't remove all added items to cart");

    }
    //will edit it again
    @Test(dataProvider = "loginData")
    public void testAddToCartFromProductDetails(String username, String password) {
        homePage = new HomePage(base_driver);
        loginPage = new LoginPage(base_driver);
      //login
        loginPage.setUsername(username);
        loginPage.setPassword(password);
        loginPage.clickonlogin();
        //get all xpaths for all items
        String[] Allxpaths={"//*[@id=\"item_4_title_link\"]/div","//*[@id=\"item_0_title_link\"]/div","//*[@id=\"item_1_title_link\"]/div","//*[@id=\"item_5_title_link\"]/div","//*[@id=\"item_2_title_link\"]/div","//*[@id=\"item_3_title_link\"]/div"};
       // go to each item and open it then click on add to cart button
        for(int i=0;i<Allxpaths.length;i++){
            homePage.AddItemtoCart(Allxpaths[i]);
            homePage.CLickAddtoCartAndBack();
        }
        //get cart count after add all items to cart
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
        //check that cart count return to 0 again
        Assert.assertEquals(0,AfterCartItemsCount,"Cart count not correct");

    }
    //test items names that are correct to user
    @Test(dataProvider = "loginData")
    public void CheckItemsNames (String username, String password) {
        homePage = new HomePage(base_driver);
        loginPage = new LoginPage(base_driver);
     //login
        loginPage.setUsername(username);
        loginPage.setPassword(password);
        loginPage.clickonlogin();
        //get list of  items names
        List<String> ItemsNames=homePage.getItemsName();
        //regex test
        Pattern digit = Pattern.compile("[0-9]");
        Pattern special = Pattern.compile ("[!@#$%&*()+=|<>?{}\\[\\]~]");
        int ActualCount=0;
        int ExpectedCount=6;
        for(String name:ItemsNames) {
            Matcher digits = digit.matcher(name);
            Matcher SPcharacter = special.matcher(name);

            boolean val1 = digits.find();
            boolean val2 = SPcharacter.find();
            if(val1 == false && val2 == false){
                System.out.println(" product name is correct :"+name);
                ActualCount=ActualCount+1;

            }else
                System.out.println("product name is not correct :" +name);

        }
        System.out.println(ActualCount+"the actual count");
         Assert.assertEquals(ActualCount,ExpectedCount,"there is error in names of items");

        }
        // check can open cart page
    @Test(dataProvider = "loginData")
    public void CheckCartItemIsOpen (String username, String password) {
        homePage = new HomePage(base_driver);
        loginPage = new LoginPage(base_driver);

        loginPage.setUsername(username);
        loginPage.setPassword(password);
        loginPage.clickonlogin();
        base_driver.findElement(By.xpath("//*[@id=\"shopping_cart_container\"]/a")).click();
        Assert.assertEquals(base_driver.getCurrentUrl(),"https://www.saucedemo.com/cart.html");




    }

}
