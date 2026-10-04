package com.core.pages.SwagLabs;

import java.util.Arrays;

/**
 * The pages of the site, as named in feature files ("the products page", "the cart page", ...).
 */
public enum AppPage {
    LOGIN("login", "", null),
    PRODUCTS("products", "inventory.html", "Products"),
    PRODUCT_DETAILS("product details", "inventory-item.html", null),
    CART("cart", "cart.html", "Your Cart"),
    CHECKOUT_INFORMATION("checkout information", "checkout-step-one.html", "Checkout: Your Information"),
    CHECKOUT_OVERVIEW("checkout overview", "checkout-step-two.html", "Checkout: Overview"),
    CHECKOUT_COMPLETE("checkout complete", "checkout-complete.html", "Checkout: Complete!");

    private final String displayName;
    private final String path;
    private final String title;

    AppPage(String displayName, String path, String title) {
        this.displayName = displayName;
        this.path = path;
        this.title = title;
    }

    /** Path relative to the base URL, e.g. "cart.html" (empty for the login page). */
    public String path() {
        return path;
    }

    /** Header title shown on the page, or null when the page has none. */
    public String title() {
        return title;
    }

    public static AppPage fromName(String name) {
        return Arrays.stream(values())
                .filter(p -> p.displayName.equalsIgnoreCase(name.trim()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown page: " + name));
    }
}
