package Models;

import java.util.Arrays;

/**
 * The options of the products sort dropdown, by their visible label.
 */
public enum SortOption {
    NAME_A_TO_Z("Name (A to Z)"),
    NAME_Z_TO_A("Name (Z to A)"),
    PRICE_LOW_TO_HIGH("Price (low to high)"),
    PRICE_HIGH_TO_LOW("Price (high to low)");

    private final String label;

    SortOption(String label) {
        this.label = label;
    }

    public String label() {
        return label;
    }

    public boolean isByPrice() {
        return this == PRICE_LOW_TO_HIGH || this == PRICE_HIGH_TO_LOW;
    }

    public boolean isDescending() {
        return this == NAME_Z_TO_A || this == PRICE_HIGH_TO_LOW;
    }

    public static SortOption fromLabel(String label) {
        return Arrays.stream(values())
                .filter(o -> o.label.equalsIgnoreCase(label.trim()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown sort option: " + label));
    }
}
