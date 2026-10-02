package Models;

/**
 * A product as shown on the site: its name and unit price.
 */
public record Product(String name, double price) {

    public static double parsePrice(String text) {
        return Double.parseDouble(text.replaceAll("[^0-9.]", ""));
    }
}
