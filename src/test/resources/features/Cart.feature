@Cart
Feature: Cart
  As a logged in customer
  I want to add and remove items from my cart
  So that the cart only contains what I want to buy

  @TC_14
  Scenario Outline: TC_14 Verify a single item can be removed from the cart page for "<username>"
    Given the user is logged in as "<username>" with password "<password>"
    Then each of the following items can be added from the home page and removed from the cart page one at a time:
      | Sauce Labs Backpack               |
      | Sauce Labs Bike Light             |
      | Sauce Labs Bolt T-Shirt           |
      | Sauce Labs Fleece Jacket          |
      | Sauce Labs Onesie                 |
      | Test.allTheThings() T-Shirt (Red) |

    Examples:
      | username                | password     |
      | standard_user           | secret_sauce |
      | problem_user            | secret_sauce |
      | performance_glitch_user | secret_sauce |
      | error_user              | secret_sauce |
      | visual_user             | secret_sauce |

  @TC_15
  Scenario Outline: TC_15 Verify a single item can be removed from the main page for "<username>"
    Given the user is logged in as "<username>" with password "<password>"
    When the user adds the following items to the cart from the home page:
      | Sauce Labs Backpack               |
      | Sauce Labs Bike Light             |
      | Sauce Labs Bolt T-Shirt           |
      | Sauce Labs Fleece Jacket          |
      | Sauce Labs Onesie                 |
      | Test.allTheThings() T-Shirt (Red) |
    Then each added item should show the "Remove" button on the home page
    When the user removes the added items from the home page
    Then each added item should show the "Add to cart" button on the home page

    Examples:
      | username                | password     |
      | standard_user           | secret_sauce |
      | problem_user            | secret_sauce |
      | performance_glitch_user | secret_sauce |
      | error_user              | secret_sauce |
      | visual_user             | secret_sauce |

  @TC_16
  Scenario Outline: TC_16 Verify all items can be removed from the cart page for "<username>"
    Given the user is logged in as "<username>" with password "<password>"
    When the user adds the following items to the cart from the home page:
      | Sauce Labs Backpack               |
      | Sauce Labs Bike Light             |
      | Sauce Labs Bolt T-Shirt           |
      | Sauce Labs Fleece Jacket          |
      | Sauce Labs Onesie                 |
      | Test.allTheThings() T-Shirt (Red) |
    Then each added item should show the "Remove" button on the home page
    When the user clicks the cart icon
    And the user removes the added items from the cart page
    Then the cart should be empty

    Examples:
      | username                | password     |
      | standard_user           | secret_sauce |
      | problem_user            | secret_sauce |
      | performance_glitch_user | secret_sauce |
      | error_user              | secret_sauce |
      | visual_user             | secret_sauce |

  @TC_17
  Scenario Outline: TC_17 Verify all items can be removed from the main page for "<username>"
    Given the user is logged in as "<username>" with password "<password>"
    When the user adds the following items to the cart from the home page:
      | Sauce Labs Backpack               |
      | Sauce Labs Bike Light             |
      | Sauce Labs Bolt T-Shirt           |
      | Sauce Labs Fleece Jacket          |
      | Sauce Labs Onesie                 |
      | Test.allTheThings() T-Shirt (Red) |
    Then each added item should show the "Remove" button on the home page
    When the user removes the added items from the home page
    Then the cart badge count should be 0

    Examples:
      | username                | password     |
      | standard_user           | secret_sauce |
      | problem_user            | secret_sauce |
      | performance_glitch_user | secret_sauce |
      | error_user              | secret_sauce |
      | visual_user             | secret_sauce |

  @BackToInventory
  Scenario Outline: User can go back to the inventory page from the cart for "<username>"
    Given the user is logged in as "<username>" with password "<password>"
    When the user adds the following items to the cart from the home page:
      | Sauce Labs Backpack   |
      | Sauce Labs Bike Light |
    And the user clicks the cart icon
    And the user clicks continue shopping
    Then the current URL should contain "inventory"

    Examples:
      | username                | password     |
      | standard_user           | secret_sauce |
      | problem_user            | secret_sauce |
      | performance_glitch_user | secret_sauce |
      | error_user              | secret_sauce |
      | visual_user             | secret_sauce |
