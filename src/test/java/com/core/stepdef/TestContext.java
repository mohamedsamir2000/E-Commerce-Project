package com.core.stepdef;

import com.core.pages.SwagLabs.models.ExpectedCart;
import com.core.pages.SwagLabs.AppPage;
import com.core.pages.SwagLabs.CartPage;
import com.core.pages.SwagLabs.CheckoutCompletePage;
import com.core.pages.SwagLabs.CheckoutInformationPage;
import com.core.pages.SwagLabs.CheckoutOverviewPage;
import com.core.pages.SwagLabs.components.Footer;
import com.core.pages.SwagLabs.components.Header;
import com.core.pages.SwagLabs.components.SideMenu;
import com.core.pages.SwagLabs.InventoryPage;
import com.core.pages.SwagLabs.LoginPage;
import com.core.pages.SwagLabs.ProductDetailsPage;
import com.core.TestEnvConfig;
import com.core.utils.DriverFactory;
import org.openqa.selenium.WebDriver;

/**
 * Shared state for a single scenario. PicoContainer creates one instance per scenario and
 * injects it into Hooks and every step definition class, so all steps share the same
 * driver, page objects and expected cart.
 */
public class TestContext {

    private LoginPage loginPage;
    private InventoryPage inventoryPage;
    private ProductDetailsPage productDetailsPage;
    private CartPage cartPage;
    private CheckoutInformationPage checkoutInformationPage;
    private CheckoutOverviewPage checkoutOverviewPage;
    private CheckoutCompletePage checkoutCompletePage;
    private Header header;
    private SideMenu sideMenu;
    private Footer footer;

    private final ExpectedCart expectedCart = new ExpectedCart();

    public WebDriver getDriver() {
        return DriverFactory.getDriver();
    }

    public LoginPage loginPage() {
        if (loginPage == null) loginPage = new LoginPage(getDriver());
        return loginPage;
    }

    public InventoryPage inventoryPage() {
        if (inventoryPage == null) inventoryPage = new InventoryPage(getDriver());
        return inventoryPage;
    }

    public ProductDetailsPage productDetailsPage() {
        if (productDetailsPage == null) productDetailsPage = new ProductDetailsPage(getDriver());
        return productDetailsPage;
    }

    public CartPage cartPage() {
        if (cartPage == null) cartPage = new CartPage(getDriver());
        return cartPage;
    }

    public CheckoutInformationPage checkoutInformationPage() {
        if (checkoutInformationPage == null) checkoutInformationPage = new CheckoutInformationPage(getDriver());
        return checkoutInformationPage;
    }

    public CheckoutOverviewPage checkoutOverviewPage() {
        if (checkoutOverviewPage == null) checkoutOverviewPage = new CheckoutOverviewPage(getDriver());
        return checkoutOverviewPage;
    }

    public CheckoutCompletePage checkoutCompletePage() {
        if (checkoutCompletePage == null) checkoutCompletePage = new CheckoutCompletePage(getDriver());
        return checkoutCompletePage;
    }

    public Header header() {
        if (header == null) header = new Header(getDriver());
        return header;
    }

    public SideMenu sideMenu() {
        if (sideMenu == null) sideMenu = new SideMenu(getDriver());
        return sideMenu;
    }

    public Footer footer() {
        if (footer == null) footer = new Footer(getDriver());
        return footer;
    }

    /** Products the scenario expects in the cart, kept up to date by the add/remove steps. */
    public ExpectedCart expectedCart() {
        return expectedCart;
    }

    /** True when the browser is currently on the given page (by URL path). */
    public boolean isOn(AppPage page) {
        String url = getDriver().getCurrentUrl();
        return page == AppPage.LOGIN
                ? url.equals(TestEnvConfig.baseUrl())
                : url.contains("/" + page.path());
    }
}
