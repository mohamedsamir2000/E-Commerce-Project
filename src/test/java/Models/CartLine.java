package Models;

/**
 * One line in the cart or on the checkout overview.
 */
public record CartLine(String name, double price, int quantity) {
}
