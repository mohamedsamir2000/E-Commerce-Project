@ShoppingItems
Feature: Shopping items
  As a logged in customer
  I want to browse and select products
  So that I can build my order

  @AddAllItemsOnCart
  Scenario Outline: All items on the home page can be added to the cart and removed for "<username>"
    Given the user is logged in as "<username>" with password "<password>"
    Then the inventory page should be displayed
    When the user adds all items to the cart from the home page
    Then every item on the page should show a Remove button
    And the cart badge count should equal the number of items on the page
    When the user removes all items from the home page
    Then the cart badge count should be 0

    Examples:
      | username                | password     |
      | standard_user           | secret_sauce |
      | problem_user            | secret_sauce |
      | performance_glitch_user | secret_sauce |
      | error_user              | secret_sauce |
      | visual_user             | secret_sauce |

  @AddToCartFromProductDetails
  Scenario Outline: Items can be added and removed from the product details page for "<username>"
    Given the user is logged in as "<username>" with password "<password>"
    When the user adds each of the following items from its product details page:
      | Sauce Labs Backpack               |
      | Sauce Labs Bike Light             |
      | Sauce Labs Bolt T-Shirt           |
      | Sauce Labs Fleece Jacket          |
      | Sauce Labs Onesie                 |
      | Test.allTheThings() T-Shirt (Red) |
    Then the cart badge count should be 6
    When the user removes each added item from its product details page
    Then the cart badge count should be 0

    Examples:
      | username                | password     |
      | standard_user           | secret_sauce |
      | problem_user            | secret_sauce |
      | performance_glitch_user | secret_sauce |
      | error_user              | secret_sauce |
      | visual_user             | secret_sauce |

  @CheckItemsNames
  Scenario Outline: Item names are displayed correctly for "<username>"
    Given the user is logged in as "<username>" with password "<password>"
    Then all 6 item names should not contain digits or special characters

    Examples:
      | username                | password     |
      | standard_user           | secret_sauce |
      | problem_user            | secret_sauce |
      | performance_glitch_user | secret_sauce |
      | error_user              | secret_sauce |
      | visual_user             | secret_sauce |

  @CheckCartItemIsOpen
  Scenario Outline: The cart page can be opened from the cart icon for "<username>"
    Given the user is logged in as "<username>" with password "<password>"
    When the user clicks the cart icon
    Then the current URL should be "https://www.saucedemo.com/cart.html"

    Examples:
      | username                | password     |
      | standard_user           | secret_sauce |
      | problem_user            | secret_sauce |
      | performance_glitch_user | secret_sauce |
      | error_user              | secret_sauce |
      | visual_user             | secret_sauce |
