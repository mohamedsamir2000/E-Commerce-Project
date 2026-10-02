package Pages;

import Models.Product;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class ProductDetailsPage extends BasePage {

    private final By productName = By.className("inventory_details_name");
    private final By productDescription = By.className("inventory_details_desc");
    private final By productPrice = By.className("inventory_details_price");
    private final By productImage = By.className("inventory_details_img");
    private final By cartButton = By.xpath("//button[text()='Add to cart' or text()='Remove']");
    private final By backButton = By.id("back-to-products");

    public ProductDetailsPage(WebDriver driver) {
        super(driver);
    }

    public boolean isDisplayed() {
        return isDisplayedWithin(productName);
    }

    public String getProductName() {
        return getText(productName);
    }

    public String getProductDescription() {
        return getText(productDescription);
    }

    public double getProductPrice() {
        return Product.parsePrice(getText(productPrice));
    }

    public Product getProduct() {
        return new Product(getProductName(), getProductPrice());
    }

    public boolean isProductImageDisplayed() {
        WebElement image = waitForVisible(productImage);
        return image.getAttribute("src") != null && !image.getAttribute("src").isBlank();
    }

    public String getButtonText() {
        return getText(cartButton);
    }

    public void addToCart() {
        if (!getButtonText().equalsIgnoreCase("Add to cart")) {
            throw new IllegalStateException(getProductName() + " is already in the cart");
        }
        click(cartButton);
    }

    public void removeFromCart() {
        if (!getButtonText().equalsIgnoreCase("Remove")) {
            throw new IllegalStateException(getProductName() + " is not in the cart");
        }
        click(cartButton);
    }

    public void backToProducts() {
        click(backButton);
    }
}
