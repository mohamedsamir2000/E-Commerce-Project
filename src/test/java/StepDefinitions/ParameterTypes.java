package StepDefinitions;

import Models.CheckoutInfo;
import Models.Product;
import Models.SortOption;
import Pages.AppPage;
import io.cucumber.java.DataTableType;
import io.cucumber.java.ParameterType;

import java.util.Map;

/**
 * Custom Cucumber parameter and data table types used across all step definitions.
 */
public class ParameterTypes {

    /** {page}: login, products, product details, cart, checkout information, checkout overview, checkout complete */
    @ParameterType("login|products|product details|cart|checkout information|checkout overview|checkout complete")
    public AppPage page(String name) {
        return AppPage.fromName(name);
    }

    /** {sortOption}: one of the sort dropdown labels, in quotes, e.g. "Price (low to high)" */
    @ParameterType("\"(Name \\(A to Z\\)|Name \\(Z to A\\)|Price \\(low to high\\)|Price \\(high to low\\))\"")
    public SortOption sortOption(String label) {
        return SortOption.fromLabel(label);
    }

    /** Table rows with "name" and "price" columns, e.g. | Sauce Labs Backpack | $29.99 | */
    @DataTableType
    public Product product(Map<String, String> row) {
        return new Product(value(row, "name"), Product.parsePrice(value(row, "price")));
    }

    /** Table rows with "first name", "last name" and "postal code" columns. Empty cells mean empty input. */
    @DataTableType
    public CheckoutInfo checkoutInfo(Map<String, String> row) {
        return new CheckoutInfo(value(row, "first name"), value(row, "last name"), value(row, "postal code"));
    }

    private static String value(Map<String, String> row, String column) {
        return row.entrySet().stream()
                .filter(e -> e.getKey().trim().equalsIgnoreCase(column))
                .map(Map.Entry::getValue)
                .map(v -> v == null ? "" : v.trim())
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Missing column '" + column + "' in table " + row.keySet()));
    }
}
