@checkout
Feature: Checkout
  As a logged in customer
  I want to check out the products in my cart
  So that I can place an order

  @smoke @TC_24
  Scenario Outline: "<username>" can place an order
    Given the user is logged in as "<username>"
    When the user adds the following products to the cart:
      | Sauce Labs Backpack   |
      | Sauce Labs Bike Light |
    And the user opens the cart
    And the user proceeds to checkout
    Then the user should be on the checkout information page
    When the user enters the checkout information:
      | first name | last name | postal code |
      | marwa      | Ashraf    | 12345       |
    And the user continues the checkout
    Then the user should be on the checkout overview page
    When the user finishes the order
    Then the user should be on the checkout complete page
    And the order confirmation should show "Thank you for your order!"
    And the cart badge should not be displayed

    Examples:
      | username                |
      | standard_user           |
      | problem_user            |
      | performance_glitch_user |
      | error_user              |
      | visual_user             |

  @TC_25
  Scenario Outline: The order summary is correct for <products>
    Given the user is logged in as "standard_user"
    When the user adds the following products to the cart:
      | <first product>  |
      | <second product> |
    And the user checks out with first name "Test", last name "Total" and postal code "12345"
    Then the checkout overview should list the selected products
    And the payment information should be "SauceCard #31337"
    And the shipping information should be "Free Pony Express Delivery!"
    And the item total should be "<item total>"
    And the item total should equal the sum of the product prices
    And the tax should be 8% of the item total
    And the total should equal the item total plus tax

    Examples:
      | products              | first product            | second product                    | item total |
      | Backpack + Bike Light | Sauce Labs Backpack      | Sauce Labs Bike Light             | $39.98     |
      | Jacket + Onesie       | Sauce Labs Fleece Jacket | Sauce Labs Onesie                 | $57.98     |
      | Bolt + Red T-Shirt    | Sauce Labs Bolt T-Shirt  | Test.allTheThings() T-Shirt (Red) | $31.98     |

  @TC_25
  Scenario Outline: The order totals are calculated correctly for "<username>"
    Given the user is logged in as "<username>"
    When the user adds the following products to the cart:
      | Sauce Labs Backpack   |
      | Sauce Labs Bike Light |
    And the user checks out with first name "Test", last name "Total" and postal code "12345"
    Then the item total should match the selected products
    And the tax should be 8% of the item total
    And the total should equal the item total plus tax

    Examples:
      | username                |
      | standard_user           |
      | problem_user            |
      | performance_glitch_user |
      | error_user              |
      | visual_user             |

  @negative @TC_26
  Scenario Outline: Checkout information is required – <missing field>
    Given the user is logged in as "standard_user"
    When the user adds "Sauce Labs Backpack" to the cart
    And the user opens the cart
    And the user proceeds to checkout
    And the user enters first name "<first name>", last name "<last name>" and postal code "<postal code>"
    And the user continues the checkout
    Then the error message "<error>" should be displayed
    And the user should be on the checkout information page

    Examples:
      | missing field | first name | last name | postal code | error                          |
      | all fields    |            |           |             | Error: First Name is required  |
      | first name    |            | Ashraf    | 12345       | Error: First Name is required  |
      | last name     | Marwa      |           | 12345       | Error: Last Name is required   |
      | postal code   | Marwa      | Ashraf    |             | Error: Postal Code is required |

  @negative @TC_26
  Scenario Outline: "<username>" cannot continue the checkout with empty fields
    Given the user is logged in as "<username>"
    When the user adds "Sauce Labs Backpack" to the cart
    And the user opens the cart
    And the user proceeds to checkout
    And the user continues the checkout
    Then the error message "Error: First Name is required" should be displayed

    Examples:
      | username                |
      | standard_user           |
      | problem_user            |
      | performance_glitch_user |
      | error_user              |
      | visual_user             |

  @negative
  Scenario: The checkout error message can be dismissed
    Given the user is logged in as "standard_user"
    When the user adds "Sauce Labs Backpack" to the cart
    And the user opens the cart
    And the user proceeds to checkout
    And the user continues the checkout
    Then the error message "Error: First Name is required" should be displayed
    When the user dismisses the error message
    Then no error message should be displayed

  Scenario: Cancelling on the information step returns to the cart
    Given the user is logged in as "standard_user"
    When the user adds "Sauce Labs Backpack" to the cart
    And the user opens the cart
    And the user proceeds to checkout
    And the user cancels the checkout
    Then the user should be on the cart page
    And the cart should contain the selected products

  Scenario: Cancelling on the overview step returns to the products page and keeps the cart
    Given the user is logged in as "standard_user"
    When the user adds "Sauce Labs Backpack" to the cart
    And the user checks out with first name "Marwa", last name "Ashraf" and postal code "12345"
    And the user cancels the checkout
    Then the user should be on the products page
    And the cart badge should show 1

  Scenario: Back Home after the order returns to an empty store
    Given the user is logged in as "standard_user"
    When the user adds "Sauce Labs Onesie" to the cart
    And the user checks out with first name "Marwa", last name "Ashraf" and postal code "12345"
    And the user finishes the order
    And the user goes back home
    Then the user should be on the products page
    And the cart badge should not be displayed
    And the product "Sauce Labs Onesie" should show the "Add to cart" button
