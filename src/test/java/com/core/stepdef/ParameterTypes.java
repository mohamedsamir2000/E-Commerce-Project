package com.core.stepdef;

import com.core.pages.SwagLabs.models.Product;
import io.cucumber.java.DataTableType;

import java.util.Map;

/**
 * Converts feature file tables into objects, so steps can take e.g. {@code List<Product>}.
 */
public class ParameterTypes {

    /** Table rows with "Product" and "Price" columns, e.g. | Sauce Labs Backpack | $29.99 | */
    @DataTableType
    public Product product(Map<String, String> row) {
        return new Product(value(row, "Product"), Product.parsePrice(value(row, "Price")));
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
