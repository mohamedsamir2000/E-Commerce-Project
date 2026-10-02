@Checkout
Feature: Checkout
  As a logged in customer
  I want to check out the items in my cart
  So that I can place an order

  @TC_24
  Scenario Outline: TC_24 Checkout process completes successfully for "<username>"
    Given the user is logged in as "<username>" with password "<password>"
    And the user adds the following items to the cart from the home page:
      | Sauce Labs Backpack   |
      | Sauce Labs Bike Light |
    And the user clicks the cart icon
    And the user proceeds to checkout
    When the user enters checkout information "marwa" "Ashraf" "12345"
    And the user continues the checkout
    And the user finishes the order
    Then the order confirmation message "Thank you for your order!" should be displayed

    Examples:
      | username                | password     |
      | standard_user           | secret_sauce |
      | problem_user            | secret_sauce |
      | performance_glitch_user | secret_sauce |
      | error_user              | secret_sauce |
      | visual_user             | secret_sauce |

  @TC_25
  Scenario Outline: TC_25 Checkout total is calculated correctly for "<username>"
    Given the user is logged in as "<username>" with password "<password>"
    And the user adds the following items to the cart from the home page:
      | Sauce Labs Backpack   |
      | Sauce Labs Bike Light |
    And the user clicks the cart icon
    And the user proceeds to checkout
    When the user enters checkout information "Test" "Total" "12345"
    And the user continues the checkout
    Then the item total should equal the sum of the item prices
    And the total should equal the item total plus tax

    Examples:
      | username                | password     |
      | standard_user           | secret_sauce |
      | problem_user            | secret_sauce |
      | performance_glitch_user | secret_sauce |
      | error_user              | secret_sauce |
      | visual_user             | secret_sauce |

  @TC_26
  Scenario Outline: TC_26 Checkout with empty fields shows an error for "<username>"
    Given the user is logged in as "<username>" with password "<password>"
    And the user adds the following items to the cart from the home page:
      | Sauce Labs Backpack   |
      | Sauce Labs Bike Light |
    And the user clicks the cart icon
    And the user proceeds to checkout
    When the user enters checkout information "" "" ""
    And the user continues the checkout
    Then the checkout error "Error: First Name is required" should be displayed

    Examples:
      | username                | password     |
      | standard_user           | secret_sauce |
      | problem_user            | secret_sauce |
      | performance_glitch_user | secret_sauce |
      | error_user              | secret_sauce |
      | visual_user             | secret_sauce |
