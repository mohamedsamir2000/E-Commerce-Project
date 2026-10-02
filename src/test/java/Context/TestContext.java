package Context;

import Pages.CartPage;
import Pages.CheckoutPage;
import Pages.HomePage;
import Pages.LoginPage;
import Pages.ProductDetailsPage;
import Utility.DriverFactory;
import org.openqa.selenium.WebDriver;

import java.util.ArrayList;
import java.util.List;

/**
 * Shared state for a single scenario. PicoContainer injects the same instance
 * into Hooks and every step definition class, so all steps of a scenario use
 * the same driver and page objects.
 */
public class TestContext {

    private LoginPage loginPage;
    private HomePage homePage;
    private ProductDetailsPage productDetailsPage;
    private CartPage cartPage;
    private CheckoutPage checkoutPage;

    private String currentUsername;
    private List<String> selectedItems = new ArrayList<>();

    public WebDriver getDriver() {
        return DriverFactory.getDriver();
    }

    public LoginPage getLoginPage() {
        if (loginPage == null) loginPage = new LoginPage(getDriver());
        return loginPage;
    }

    public HomePage getHomePage() {
        if (homePage == null) homePage = new HomePage(getDriver());
        return homePage;
    }

    public ProductDetailsPage getProductDetailsPage() {
        if (productDetailsPage == null) productDetailsPage = new ProductDetailsPage(getDriver());
        return productDetailsPage;
    }

    public CartPage getCartPage() {
        if (cartPage == null) cartPage = new CartPage(getDriver());
        return cartPage;
    }

    public CheckoutPage getCheckoutPage() {
        if (checkoutPage == null) checkoutPage = new CheckoutPage(getDriver());
        return checkoutPage;
    }

    public String getCurrentUsername() {
        return currentUsername;
    }

    public void setCurrentUsername(String currentUsername) {
        this.currentUsername = currentUsername;
    }

    /** Products the current scenario added to the cart, in order. */
    public List<String> getSelectedItems() {
        return selectedItems;
    }

    public void setSelectedItems(List<String> selectedItems) {
        this.selectedItems = new ArrayList<>(selectedItems);
    }
}
