package com.core.pages.SwagLabs.models;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * What the scenario expects to be in the cart. Steps that add or remove products keep it
 * up to date, so later steps (cart, checkout overview, totals) can verify against it.
 */
public class ExpectedCart {

    private final Map<String, Double> products = new LinkedHashMap<>();

    public void add(Product product) {
        products.put(product.name(), product.price());
    }

    public void remove(String productName) {
        products.remove(productName);
    }

    public void clear() {
        products.clear();
    }

    public int size() {
        return products.size();
    }

    public List<String> names() {
        return new ArrayList<>(products.keySet());
    }

    public List<Product> products() {
        return products.entrySet().stream().map(e -> new Product(e.getKey(), e.getValue())).toList();
    }

    public double total() {
        return products.values().stream().mapToDouble(Double::doubleValue).sum();
    }
}
