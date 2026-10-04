package com.core.stepdef;

import com.core.pages.SwagLabs.AppPage;
import com.core.pages.SwagLabs.InventoryPage;
import com.core.pages.SwagLabs.ProductDetailsPage;
import com.core.pages.SwagLabs.models.Product;
import com.core.pages.SwagLabs.models.SortOption;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Products screen and product details screen: catalog, sorting, add/remove, details.
 * Add/remove work on whichever of these screens (or the cart) the customer is on, and keep the
 * expected cart in {@link TestContext} up to date.
 */
public class ProductsStepDef {

    private static final Pattern INVALID_NAME_CHARACTERS = Pattern.compile("[0-9!@#$%&*()+=|<>?{}\\[\\]~]");

    private final TestContext context;
    private final InventoryPage inventoryPage;
    private final ProductDetailsPage detailsPage;

    public ProductsStepDef(TestContext context) {
        this.context = context;
        this.inventoryPage = context.inventoryPage();
        this.detailsPage = context.productDetailsPage();
    }

    // ---------- Catalog ----------

    @Then("Verify the products list:")
    public void verifyTheProductsList(List<Product> expected) {
        Assert.assertEquals(new HashSet<>(inventoryPage.getProducts()), new HashSet<>(expected), "Products on the screen");
    }

    @Then("Verify every product has a name, description, price and image")
    public void verifyEveryProductIsComplete() {
        for (Product product : inventoryPage.getProducts()) {
            Assert.assertFalse(product.name().isBlank(), "A product has no name");
            Assert.assertTrue(product.price() > 0, product.name() + " has no price");
            Assert.assertFalse(inventoryPage.getProductDescription(product.name()).isBlank(), product.name() + " has no description");
            Assert.assertTrue(inventoryPage.isProductImageDisplayed(product.name()), product.name() + " has no image");
        }
    }

    @Then("Verify product names have no digits or special characters")
    public void verifyProductNamesHaveNoSpecialCharacters() {
        List<String> invalid = inventoryPage.getProductNames().stream()
                .filter(name -> INVALID_NAME_CHARACTERS.matcher(name).find())
                .toList();
        Assert.assertTrue(invalid.isEmpty(), "Product names with digits or special characters: " + invalid);
    }

    // ---------- Sorting ----------

    @When("Sort products by {string}")
    public void sortProductsBy(String option) {
        inventoryPage.sortBy(SortOption.fromLabel(option));
    }

    @Then("Verify the active sort option is {string}")
    public void verifyTheActiveSortOptionIs(String option) {
        Assert.assertEquals(inventoryPage.getActiveSortOption(), SortOption.fromLabel(option).label(), "Active sort option");
    }

    @Then("Verify products are sorted by {string}")
    public void verifyProductsAreSortedBy(String label) {
        SortOption option = SortOption.fromLabel(label);
        if (option.isByPrice()) {
            assertSorted(inventoryPage.getProductPrices(), option);
        } else {
            assertSorted(inventoryPage.getProductNames(), option);
        }
    }

    private static <T extends Comparable<T>> void assertSorted(List<T> actual, SortOption option) {
        List<T> expected = new ArrayList<>(actual);
        expected.sort(option.isDescending() ? Comparator.reverseOrder() : Comparator.naturalOrder());
        Assert.assertEquals(actual, expected, "Products are not sorted by " + option.label());
    }

    // ---------- Add / remove ----------

    @When("Add product {string} to cart")
    public void addProductToCart(String productName) {
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

    @When("Remove product {string} from cart")
    public void removeProductFromCart(String productName) {
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

    /** Table with a "Product" header. */
    @When("Add the following products to cart:")
    public void addTheFollowingProductsToCart(DataTable products) {
        productNames(products).forEach(this::addProductToCart);
    }

    /** Table with a "Product" header. */
    @When("Remove the following products from cart:")
    public void removeTheFollowingProductsFromCart(DataTable products) {
        productNames(products).forEach(this::removeProductFromCart);
    }

    @When("Add all products to cart")
    public void addAllProductsToCart() {
        for (String name : inventoryPage.getProductNames()) {
            if (inventoryPage.getButtonText(name).equalsIgnoreCase("Add to cart")) {
                addProductToCart(name);
            }
        }
    }

    @When("Remove all products from cart")
    public void removeAllProductsFromCart() {
        List<String> names = context.isOn(AppPage.CART)
                ? context.cartPage().getProductNames()
                : inventoryPage.getProductNames().stream()
                    .filter(n -> inventoryPage.getButtonText(n).equalsIgnoreCase("Remove"))
                    .toList();
        names.forEach(this::removeProductFromCart);
    }

    @Then("Verify product {string} shows the {string} button")
    public void verifyProductShowsTheButton(String productName, String buttonText) {
        String actual = context.isOn(AppPage.PRODUCT_DETAILS)
                ? detailsPage.getButtonText()
                : inventoryPage.getButtonText(productName);
        Assert.assertEquals(actual, buttonText, "Button of " + productName);
    }

    /** Table with a "Product" header. */
    @Then("Verify the following products show the {string} button:")
    public void verifyTheFollowingProductsShowTheButton(String buttonText, DataTable products) {
        productNames(products).forEach(name -> verifyProductShowsTheButton(name, buttonText));
    }

    // ---------- Product details ----------

    @When("Open product {string}")
    public void openProduct(String productName) {
        if (context.isOn(AppPage.CART)) {
            context.cartPage().openProduct(productName);
        } else {
            inventoryPage.openProductByName(productName);
        }
    }

    @When("Open product {string} by its image")
    public void openProductByItsImage(String productName) {
        inventoryPage.openProductByImage(productName);
    }

    @When("Back to products")
    public void backToProducts() {
        detailsPage.backToProducts();
    }

    /** Table with "label | value" header; labels: Name, Price. Also checks description and image. */
    @Then("Verify product details:")
    public void verifyProductDetails(DataTable details) {
        Assert.assertTrue(detailsPage.isDisplayed(), "Product details screen is not displayed");
        for (Map<String, String> row : details.asMaps()) {
            String label = row.get("label").trim();
            String value = row.get("value");
            switch (label.toLowerCase()) {
                case "name" -> Assert.assertEquals(detailsPage.getProductName(), value, "Product name");
                case "price" -> Assert.assertEquals(detailsPage.getProductPrice(), Product.parsePrice(value), 0.001, "Product price");
                default -> throw new IllegalArgumentException("Unknown product detail: " + label);
            }
        }
        Assert.assertFalse(detailsPage.getProductDescription().isBlank(), "Product description is empty");
        Assert.assertTrue(detailsPage.isProductImageDisplayed(), "Product image is not displayed");
    }

    static List<String> productNames(DataTable table) {
        return table.asMaps().stream().map(row -> row.get("Product").trim()).toList();
    }
}
