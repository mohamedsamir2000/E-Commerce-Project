@journey @checkout
Feature: Checkout journeys with mistakes and second thoughts
  As a Swag Labs customer
  I want the checkout to guide me when I make mistakes or change my mind
  So that I can still complete my order

  @negative
  Scenario: A customer fixes every missing checkout field one by one and completes the order
    Given the user is logged in as "standard_user"
    When the user adds "Sauce Labs Backpack" to the cart
    And the user opens the cart
    And the user proceeds to checkout
    Then the user should be on the checkout information page
    When the user continues the checkout
    Then the error message "Error: First Name is required" should be displayed
    When the user dismisses the error message
    Then no error message should be displayed
    When the user enters first name "", last name "Ashraf" and postal code "12345"
    And the user continues the checkout
    Then the error message "Error: First Name is required" should be displayed
    When the user enters first name "Marwa", last name "" and postal code "12345"
    And the user continues the checkout
    Then the error message "Error: Last Name is required" should be displayed
    When the user enters first name "Marwa", last name "Ashraf" and postal code ""
    And the user continues the checkout
    Then the error message "Error: Postal Code is required" should be displayed
    And the user should be on the checkout information page
    When the user enters first name "Marwa", last name "Ashraf" and postal code "12345"
    And the user continues the checkout
    Then the user should be on the checkout overview page
    And the checkout overview should list the selected products
    And the item total should be "$29.99"
    When the user finishes the order
    Then the order confirmation should show "Thank you for your order!"
    When the user logs out
    Then the user should be on the login page

  Scenario: A customer cancels on each checkout step before finally buying
    Given the user is logged in as "standard_user"
    When the user adds the following products to the cart:
      | Sauce Labs Backpack   |
      | Sauce Labs Bike Light |
    And the user opens the cart
    And the user proceeds to checkout
    # Cancel on the information step: back to the cart, nothing lost
    And the user cancels the checkout
    Then the user should be on the cart page
    And the cart should contain the selected products
    # Cancel on the overview step: back to the products, nothing lost
    When the user proceeds to checkout
    And the user enters first name "Marwa", last name "Ashraf" and postal code "12345"
    And the user continues the checkout
    Then the user should be on the checkout overview page
    When the user cancels the checkout
    Then the user should be on the products page
    And the cart badge should show 2
    # Buy for real
    When the user checks out with first name "Marwa", last name "Ashraf" and postal code "12345"
    Then the checkout overview should list the selected products
    And the payment information should be "SauceCard #31337"
    And the shipping information should be "Free Pony Express Delivery!"
    And the item total should be "$39.98"
    And the tax should be 8% of the item total
    And the total should equal the item total plus tax
    When the user finishes the order
    Then the user should be on the checkout complete page
    When the user goes back home
    Then the user should be on the products page
    And the cart badge should not be displayed
    When the user logs out
    Then the user should be on the login page

  Scenario Outline: The order summary is correct for <products>
    Given the user is logged in as "standard_user"
    When the user adds the following products to the cart:
      | <first product>  |
      | <second product> |
    And the user checks out with first name "Test", last name "Total" and postal code "12345"
    Then the checkout overview should list the selected products
    And the item total should be "<item total>"
    And the item total should equal the sum of the product prices
    And the tax should be 8% of the item total
    And the total should equal the item total plus tax
    When the user finishes the order
    Then the order confirmation should show "Thank you for your order!"
    When the user logs out
    Then the user should be on the login page

    Examples:
      | products              | first product            | second product                    | item total |
      | Backpack + Bike Light | Sauce Labs Backpack      | Sauce Labs Bike Light             | $39.98     |
      | Jacket + Onesie       | Sauce Labs Fleece Jacket | Sauce Labs Onesie                 | $57.98     |
      | Bolt + Red T-Shirt    | Sauce Labs Bolt T-Shirt  | Test.allTheThings() T-Shirt (Red) | $31.98     |
