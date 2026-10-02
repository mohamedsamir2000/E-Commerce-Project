@cart
Feature: Shopping cart
  As a logged in customer
  I want to add and remove products from my cart
  So that the cart only contains what I want to buy

  @smoke @TC_14
  Scenario Outline: "<username>" can add a product and remove it from the cart page
    Given the user is logged in as "<username>"
    When the user adds "Sauce Labs Backpack" to the cart
    Then the product "Sauce Labs Backpack" should show the "Remove" button
    And the cart badge should show 1
    When the user opens the cart
    Then the cart should contain the selected products
    When the user removes "Sauce Labs Backpack" from the cart
    Then the cart should be empty
    And the cart badge should not be displayed
    When the user continues shopping
    Then the product "Sauce Labs Backpack" should show the "Add to cart" button

    Examples:
      | username                |
      | standard_user           |
      | problem_user            |
      | performance_glitch_user |
      | error_user              |
      | visual_user             |

  @TC_15 @TC_17
  Scenario Outline: "<username>" can add and remove products on the products page
    Given the user is logged in as "<username>"
    When the user adds the following products to the cart:
      | Sauce Labs Backpack               |
      | Sauce Labs Bike Light             |
      | Sauce Labs Bolt T-Shirt           |
      | Sauce Labs Fleece Jacket          |
      | Sauce Labs Onesie                 |
      | Test.allTheThings() T-Shirt (Red) |
    Then the following products should show the "Remove" button:
      | Sauce Labs Backpack               |
      | Sauce Labs Bike Light             |
      | Sauce Labs Bolt T-Shirt           |
      | Sauce Labs Fleece Jacket          |
      | Sauce Labs Onesie                 |
      | Test.allTheThings() T-Shirt (Red) |
    And the cart badge should show 6
    When the user removes the following products from the cart:
      | Sauce Labs Bike Light |
      | Sauce Labs Onesie     |
    Then the cart badge should show 4
    And the following products should show the "Add to cart" button:
      | Sauce Labs Bike Light |
      | Sauce Labs Onesie     |
    When the user removes all products from the cart
    Then the cart badge should not be displayed

    Examples:
      | username                |
      | standard_user           |
      | problem_user            |
      | performance_glitch_user |
      | error_user              |
      | visual_user             |

  @TC_16 @AddAllItemsOnCart
  Scenario Outline: "<username>" can add all products and remove them all from the cart page
    Given the user is logged in as "<username>"
    When the user adds all products to the cart
    Then the cart badge should show 6
    When the user opens the cart
    Then the cart should contain 6 products
    And the cart should contain the selected products
    When the user removes all products from the cart
    Then the cart should be empty
    And the cart badge should not be displayed

    Examples:
      | username                |
      | standard_user           |
      | problem_user            |
      | performance_glitch_user |
      | error_user              |
      | visual_user             |

  @AddToCartFromProductDetails
  Scenario Outline: "<username>" can add and remove a product on its details page
    Given the user is logged in as "<username>"
    When the user opens the product "Sauce Labs Bolt T-Shirt"
    And the user adds "Sauce Labs Bolt T-Shirt" to the cart
    Then the product "Sauce Labs Bolt T-Shirt" should show the "Remove" button
    And the cart badge should show 1
    When the user goes back to the products page
    Then the product "Sauce Labs Bolt T-Shirt" should show the "Remove" button
    When the user opens the product "Sauce Labs Bolt T-Shirt"
    And the user removes "Sauce Labs Bolt T-Shirt" from the cart
    Then the product "Sauce Labs Bolt T-Shirt" should show the "Add to cart" button
    And the cart badge should not be displayed

    Examples:
      | username                |
      | standard_user           |
      | problem_user            |
      | performance_glitch_user |
      | error_user              |
      | visual_user             |

  Scenario: A product in the cart can be opened to see its details
    Given the user is logged in as "standard_user"
    When the user adds "Sauce Labs Fleece Jacket" to the cart
    And the user opens the cart
    And the user opens the product "Sauce Labs Fleece Jacket"
    Then the product details page should show "Sauce Labs Fleece Jacket" priced at "$49.99"
    And the product "Sauce Labs Fleece Jacket" should show the "Remove" button

  @CheckCartItemIsOpen
  Scenario Outline: "<username>" sees an empty cart before adding anything
    Given the user is logged in as "<username>"
    When the user opens the cart
    Then the user should be on the cart page
    And the cart should be empty

    Examples:
      | username                |
      | standard_user           |
      | problem_user            |
      | performance_glitch_user |
      | error_user              |
      | visual_user             |

  @BackToInventory
  Scenario: Continue shopping returns to the products page and keeps the cart
    Given the user is logged in as "standard_user"
    When the user adds the following products to the cart:
      | Sauce Labs Backpack   |
      | Sauce Labs Bike Light |
    And the user opens the cart
    And the user continues shopping
    Then the user should be on the products page
    And the cart badge should show 2

  @persistence
  Scenario: The cart is kept after refreshing the page and navigating
    Given the user is logged in as "standard_user"
    When the user adds the following products to the cart:
      | Sauce Labs Backpack |
      | Sauce Labs Onesie   |
    And the user refreshes the page
    Then the cart badge should show 2
    And the following products should show the "Remove" button:
      | Sauce Labs Backpack |
      | Sauce Labs Onesie   |
    When the user opens the cart
    And the user refreshes the page
    Then the cart should contain the following products:
      | name                | price  |
      | Sauce Labs Backpack | $29.99 |
      | Sauce Labs Onesie   | $7.99  |

  @persistence
  Scenario: The cart is kept after logging out and in again
    Given the user is logged in as "standard_user"
    When the user adds "Sauce Labs Backpack" to the cart
    And the user logs out
    And the user logs in as "standard_user"
    Then the cart badge should show 1
    When the user opens the cart
    Then the cart should contain the selected products

  @menu
  Scenario: Reset App State empties the cart
    Given the user is logged in as "standard_user"
    When the user adds the following products to the cart:
      | Sauce Labs Backpack   |
      | Sauce Labs Bike Light |
      | Sauce Labs Onesie     |
    And the user resets the app state
    Then the cart badge should not be displayed
    When the user opens the cart
    Then the cart should be empty
