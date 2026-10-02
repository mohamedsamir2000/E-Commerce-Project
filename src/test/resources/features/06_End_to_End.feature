@e2e
Feature: End-to-end shopping journeys
  As a Swag Labs customer
  I want to complete a whole shopping session
  So that I can buy products from login to logout

  @smoke
  Scenario Outline: "<username>" completes a full purchase from login to logout
    Given the user is on the login page
    When the user logs in as "<username>"
    Then the user should be on the products page
    # Browse and choose
    When the user sorts the products by "Price (low to high)"
    Then the products should be sorted by "Price (low to high)"
    When the user adds the following products to the cart:
      | Sauce Labs Onesie     |
      | Sauce Labs Bike Light |
    And the user opens the product "Sauce Labs Fleece Jacket"
    Then the product details page should show "Sauce Labs Fleece Jacket" priced at "$49.99"
    When the user adds "Sauce Labs Fleece Jacket" to the cart
    And the user goes back to the products page
    Then the cart badge should show 3
    # Review the cart
    When the user opens the cart
    Then the user should be on the cart page
    And the cart should contain the selected products
    # Checkout
    When the user proceeds to checkout
    And the user enters the checkout information:
      | first name | last name | postal code |
      | Marwa      | Ashraf    | 12345       |
    And the user continues the checkout
    Then the user should be on the checkout overview page
    And the checkout overview should list the selected products
    And the payment information should be "SauceCard #31337"
    And the shipping information should be "Free Pony Express Delivery!"
    And the item total should match the selected products
    And the tax should be 8% of the item total
    And the total should equal the item total plus tax
    # Place the order
    When the user finishes the order
    Then the user should be on the checkout complete page
    And the order confirmation should show "Thank you for your order!"
    And the order confirmation text should be "Your order has been dispatched, and will arrive just as fast as the pony can get there!"
    When the user goes back home
    Then the user should be on the products page
    And the cart badge should not be displayed
    # Leave
    When the user logs out
    Then the user should be on the login page

    Examples:
      | username                |
      | standard_user           |
      | problem_user            |
      | performance_glitch_user |
      | error_user              |
      | visual_user             |

  Scenario: A customer changes their mind several times before buying
    Given the user is logged in as "standard_user"
    When the user adds the following products to the cart:
      | Sauce Labs Backpack     |
      | Sauce Labs Bolt T-Shirt |
      | Sauce Labs Onesie       |
    And the user opens the cart
    And the user removes "Sauce Labs Onesie" from the cart
    Then the cart should contain the selected products
    When the user continues shopping
    And the user sorts the products by "Name (Z to A)"
    And the user adds "Test.allTheThings() T-Shirt (Red)" to the cart
    Then the cart badge should show 3
    When the user checks out with first name "Marwa", last name "Ashraf" and postal code "12345"
    Then the checkout overview should list the selected products
    # Cancel at the last step, adjust the cart and try again
    When the user cancels the checkout
    Then the user should be on the products page
    When the user removes "Sauce Labs Bolt T-Shirt" from the cart
    And the user checks out with first name "Marwa", last name "Ashraf" and postal code "12345"
    Then the checkout overview should list the following products:
      | name                              | price  |
      | Sauce Labs Backpack               | $29.99 |
      | Test.allTheThings() T-Shirt (Red) | $15.99 |
    And the item total should be "$45.98"
    And the total should equal the item total plus tax
    When the user finishes the order
    Then the order confirmation should show "Thank you for your order!"

  Scenario Outline: A customer buys only "<product>"
    Given the user is logged in as "standard_user"
    When the user opens the product "<product>"
    And the user adds "<product>" to the cart
    And the user checks out with first name "Marwa", last name "Ashraf" and postal code "12345"
    Then the checkout overview should list the selected products
    And the item total should be "<price>"
    And the tax should be 8% of the item total
    And the total should equal the item total plus tax
    When the user finishes the order
    Then the order confirmation should show "Thank you for your order!"

    Examples:
      | product                           | price  |
      | Sauce Labs Backpack               | $29.99 |
      | Sauce Labs Bike Light             | $9.99  |
      | Sauce Labs Bolt T-Shirt           | $15.99 |
      | Sauce Labs Fleece Jacket          | $49.99 |
      | Sauce Labs Onesie                 | $7.99  |
      | Test.allTheThings() T-Shirt (Red) | $15.99 |

  Scenario: A customer buys the whole catalog
    Given the user is logged in as "standard_user"
    When the user adds all products to the cart
    And the user checks out with first name "Marwa", last name "Ashraf" and postal code "12345"
    Then the checkout overview should list the selected products
    And the item total should be "$129.94"
    And the tax should be 8% of the item total
    And the total should equal the item total plus tax
    When the user finishes the order
    Then the order confirmation should show "Thank you for your order!"

  Scenario: A returning customer finds their cart after logging in again and completes the order
    Given the user is logged in as "standard_user"
    When the user adds the following products to the cart:
      | Sauce Labs Backpack      |
      | Sauce Labs Fleece Jacket |
    And the user logs out
    And the user logs in as "standard_user"
    Then the cart badge should show 2
    When the user checks out with first name "Marwa", last name "Ashraf" and postal code "12345"
    Then the checkout overview should list the selected products
    And the item total should be "$79.98"
    When the user finishes the order
    Then the order confirmation should show "Thank you for your order!"

  Scenario: A customer resets the store, then shops again from scratch
    Given the user is logged in as "standard_user"
    When the user adds all products to the cart
    And the user resets the app state
    Then the cart badge should not be displayed
    When the user refreshes the page
    And the user adds "Sauce Labs Bike Light" to the cart
    And the user checks out with first name "Marwa", last name "Ashraf" and postal code "12345"
    Then the checkout overview should list the selected products
    And the item total should be "$9.99"
    When the user finishes the order
    Then the order confirmation should show "Thank you for your order!"
