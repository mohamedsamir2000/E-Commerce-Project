@known_issue
Feature: Known Site Bugs
  Swag Labs ships demo users with deliberate defects. These journeys check the correct behaviour,
  so they are expected to FAIL and document each bug. They are skipped by default; run them with:
    mvn test -Dcucumber.filter.tags="@known_issue"

  # ProblemUser - sorting does nothing, some products cannot be added/removed, product links open the
  #               wrong product, Last Name cannot be typed so checkout never reaches the overview
  # ErrorUser   - sorting raises an alert and does nothing, some products cannot be added/removed,
  #               Finish does not place the order
  # VisualUser  - prices on the products screen are random and differ from the cart and checkout

  Scenario Outline: Full Purchase As "<User>"
    Given Customer Login as a "<User>"
    Then Verify the products list:
      | Product                           | Price  |
      | Sauce Labs Backpack               | $29.99 |
      | Sauce Labs Bike Light             | $9.99  |
      | Sauce Labs Bolt T-Shirt           | $15.99 |
      | Sauce Labs Fleece Jacket          | $49.99 |
      | Sauce Labs Onesie                 | $7.99  |
      | Test.allTheThings() T-Shirt (Red) | $15.99 |
    And Sort products by "Price (low to high)"
    Then Verify products are sorted by "Price (low to high)"
    And Add all products to cart
    Then Verify cart badge is "6"
    And Open product "Sauce Labs Bolt T-Shirt"
    Then Verify product details:
      | label | value                   |
      | Name  | Sauce Labs Bolt T-Shirt |
      | Price | $15.99                  |
    And Open cart
    Then Verify cart contains the selected products
    And Click on checkout
    And I fill the following fields:
      | label       | value  |
      | First Name  | Marwa  |
      | Last Name   | Ashraf |
      | Postal Code | 12345  |
    And Click on continue
    Then Verify screen "Checkout Overview" is displayed
    And Verify checkout overview:
      | Item Total | $129.94 |
    And Click on finish
    Then Verify screen "Checkout Complete" is displayed
    And Verify order confirmation "StoreData//ConfirmationHeader"
    And Customer Logout

    Examples:
      | User        |
      | ProblemUser |
      | ErrorUser   |
      | VisualUser  |

  Scenario: Product Names Have No Digits Or Special Characters
    # "Test.allTheThings() T-Shirt (Red)" contains brackets and dots
    Given Customer Login as a "StandardUser"
    Then Verify product names have no digits or special characters
