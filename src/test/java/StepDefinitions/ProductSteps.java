package StepDefinitions;

import Context.TestContext;
import Models.Product;
import Models.SortOption;
import Pages.AppPage;
import Pages.InventoryPage;
import Pages.ProductDetailsPage;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Steps for the products page and the product details page. Add/remove steps work on
 * whichever of those pages (or the cart page) the user is currently on.
 */
public class ProductSteps {

    private static final Pattern INVALID_NAME_CHARACTERS = Pattern.compile("[0-9!@#$%&*()+=|<>?{}\\[\\]~]");

    private final TestContext context;
    private final InventoryPage inventoryPage;
    private final ProductDetailsPage detailsPage;

    public ProductSteps(TestContext context) {
        this.context = context;
        this.inventoryPage = context.inventoryPage();
        this.detailsPage = context.productDetailsPage();
    }

    // ---------- Add / remove ----------

    @When("the user adds {string} to the cart")
    public void theUserAddsToTheCart(String productName) {
        Product product;
        if (context.isOn(AppPage.PRODUCT_DETAILS)) {
            Assert.assertEquals(detailsPage.getProductName(), productName, "A different product is open");
            product = detailsPage.getProduct();
            detailsPage.addToCart();
        } else {
            product = inventoryPage.getProduct(productName);
            inventoryPage.addToCart(productName);
        }
        context.expectedCart().add(product);
    }

    @When("the user removes {string} from the cart")
    public void theUserRemovesFromTheCart(String productName) {
        if (context.isOn(AppPage.PRODUCT_DETAILS)) {
            Assert.assertEquals(detailsPage.getProductName(), productName, "A different product is open");
            detailsPage.removeFromCart();
        } else if (context.isOn(AppPage.CART)) {
            context.cartPage().removeFromCart(productName);
        } else {
            inventoryPage.removeFromCart(productName);
        }
        context.expectedCart().remove(productName);
    }

    @When("the user adds the following products to the cart:")
    public void theUserAddsTheFollowingProducts(List<String> productNames) {
        productNames.forEach(this::theUserAddsToTheCart);
    }

    @When("the user removes the following products from the cart:")
    public void theUserRemovesTheFollowingProducts(List<String> productNames) {
        productNames.forEach(this::theUserRemovesFromTheCart);
    }

    @When("the user adds all products to the cart")
    public void theUserAddsAllProducts() {
        for (String name : inventoryPage.getProductNames()) {
            if (inventoryPage.getButtonText(name).equalsIgnoreCase("Add to cart")) {
                theUserAddsToTheCart(name);
            }
        }
    }

    @When("the user removes all products from the cart")
    public void theUserRemovesAllProducts() {
        List<String> names = context.isOn(AppPage.CART)
                ? context.cartPage().getProductNames()
                : inventoryPage.getProductNames().stream()
                    .filter(n -> inventoryPage.getButtonText(n).equalsIgnoreCase("Remove"))
                    .toList();
        names.forEach(this::theUserRemovesFromTheCart);
    }

    @Then("the product {string} should show the {string} button")
    public void theProductShouldShowTheButton(String productName, String buttonText) {
        String actual = context.isOn(AppPage.PRODUCT_DETAILS)
                ? detailsPage.getButtonText()
                : inventoryPage.getButtonText(productName);
        Assert.assertEquals(actual, buttonText, "Unexpected button for " + productName);
    }

    @Then("the following products should show the {string} button:")
    public void theFollowingProductsShouldShowTheButton(String buttonText, List<String> productNames) {
        productNames.forEach(name -> theProductShouldShowTheButton(name, buttonText));
    }

    // ---------- Product details ----------

    @When("the user opens the product {string}")
    public void theUserOpensTheProduct(String productName) {
        if (context.isOn(AppPage.CART)) {
            context.cartPage().openProduct(productName);
        } else {
            inventoryPage.openProductByName(productName);
        }
    }

    @When("the user opens the product {string} by its image")
    public void theUserOpensTheProductByItsImage(String productName) {
        inventoryPage.openProductByImage(productName);
    }

    @When("the user goes back to the products page")
    public void theUserGoesBackToTheProductsPage() {
        detailsPage.backToProducts();
    }

    @Then("the product details page should show {string} priced at {string}")
    public void theProductDetailsPageShouldShow(String productName, String price) {
        Assert.assertTrue(detailsPage.isDisplayed(), "Product details page is not displayed");
        Assert.assertEquals(detailsPage.getProductName(), productName, "Product name");
        Assert.assertEquals(detailsPage.getProductPrice(), Product.parsePrice(price), 0.001, "Product price");
        Assert.assertFalse(detailsPage.getProductDescription().isBlank(), "Product description is empty");
        Assert.assertTrue(detailsPage.isProductImageDisplayed(), "Product image is not displayed");
    }

    // ---------- Catalog ----------

    @Then("the products page should show {int} products")
    public void theProductsPageShouldShowProducts(int expectedCount) {
        Assert.assertEquals(inventoryPage.getProductCount(), expectedCount, "Number of products");
    }

    @Then("the products page should list the following products:")
    public void theProductsPageShouldList(List<Product> expectedProducts) {
        List<Product> actual = inventoryPage.getProducts();
        Assert.assertEquals(new HashSet<>(actual), new HashSet<>(expectedProducts), "Products on the page");
    }

    @Then("every product should have a name, description, price and image")
    public void everyProductShouldBeComplete() {
        for (Product product : inventoryPage.getProducts()) {
            Assert.assertFalse(product.name().isBlank(), "A product has no name");
            Assert.assertTrue(product.price() > 0, product.name() + " has no price");
            Assert.assertFalse(inventoryPage.getProductDescription(product.name()).isBlank(), product.name() + " has no description");
            Assert.assertTrue(inventoryPage.isProductImageDisplayed(product.name()), product.name() + " has no image");
        }
    }

    @Then("no product name should contain digits or special characters")
    public void noProductNameShouldContainInvalidCharacters() {
        List<String> invalid = inventoryPage.getProductNames().stream()
                .filter(name -> INVALID_NAME_CHARACTERS.matcher(name).find())
                .toList();
        Assert.assertTrue(invalid.isEmpty(), "Product names with digits or special characters: " + invalid);
    }

    // ---------- Sorting ----------

    @When("the user sorts the products by {sortOption}")
    public void theUserSortsTheProductsBy(SortOption option) {
        inventoryPage.sortBy(option);
    }

    @Then("the active sort option should be {sortOption}")
    public void theActiveSortOptionShouldBe(SortOption option) {
        Assert.assertEquals(inventoryPage.getActiveSortOption(), option.label());
    }

    @Then("the products should be sorted by {sortOption}")
    public void theProductsShouldBeSortedBy(SortOption option) {
        if (option.isByPrice()) {
            assertSorted(inventoryPage.getProductPrices(), option.isDescending(), option);
        } else {
            assertSorted(inventoryPage.getProductNames(), option.isDescending(), option);
        }
    }

    private static <T extends Comparable<T>> void assertSorted(List<T> actual, boolean descending, SortOption option) {
        List<T> expected = new ArrayList<>(actual);
        expected.sort(descending ? Comparator.reverseOrder() : Comparator.naturalOrder());
        Assert.assertEquals(actual, expected, "Products are not sorted by " + option.label());
    }
}
