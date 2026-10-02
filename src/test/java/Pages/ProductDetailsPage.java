package Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class ProductDetailsPage extends BasePage {

    private final By productName = By.className("inventory_details_name");
    private final By addToCartButton = By.xpath("//button[text()='Add to cart']");
    private final By removeButton = By.xpath("//button[text()='Remove']");
    private final By backButton = By.id("back-to-products");

    public ProductDetailsPage(WebDriver driver) {
        super(driver);
    }

    public String getProductName() {
        return getText(productName);
    }

    public void clickAddToCart() {
        click(addToCartButton);
    }

    public void clickRemove() {
        click(removeButton);
    }

    public void clickBackToProducts() {
        click(backButton);
    }
}
