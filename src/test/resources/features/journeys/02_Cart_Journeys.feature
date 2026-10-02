@journey @cart
Feature: Shopping journeys that change the cart along the way
  As a Swag Labs customer
  I want to add and remove products anywhere in the store before buying
  So that I only pay for what I finally decide to buy

  Scenario: A customer changes their mind on every page before buying
    Given the user is logged in as "standard_user"
    When the user opens the cart
    Then the user should be on the cart page
    And the cart should be empty
    # Products page: add then remove
    When the user continues shopping
    And the user adds the following products to the cart:
      | Sauce Labs Backpack               |
      | Sauce Labs Bike Light             |
      | Sauce Labs Bolt T-Shirt           |
      | Sauce Labs Fleece Jacket          |
      | Sauce Labs Onesie                 |
      | Test.allTheThings() T-Shirt (Red) |
    Then the cart badge should show 6
    When the user removes the following products from the cart:
      | Sauce Labs Bike Light |
      | Sauce Labs Onesie     |
    Then the cart badge should show 4
    And the following products should show the "Add to cart" button:
      | Sauce Labs Bike Light |
      | Sauce Labs Onesie     |
    # Details page: remove
    When the user opens the product "Sauce Labs Bolt T-Shirt"
    Then the product "Sauce Labs Bolt T-Shirt" should show the "Remove" button
    When the user removes "Sauce Labs Bolt T-Shirt" from the cart
    Then the product "Sauce Labs Bolt T-Shirt" should show the "Add to cart" button
    And the cart badge should show 3
    # Cart page: open a product, then remove another one
    When the user opens the cart
    And the user opens the product "Sauce Labs Fleece Jacket"
    Then the product details page should show "Sauce Labs Fleece Jacket" priced at "$49.99"
    And the product "Sauce Labs Fleece Jacket" should show the "Remove" button
    When the user opens the cart
    And the user removes "Test.allTheThings() T-Shirt (Red)" from the cart
    Then the cart should contain the selected products
    And the cart badge should match the selected products
    # Buy what is left
    When the user proceeds to checkout
    And the user enters first name "Marwa", last name "Ashraf" and postal code "12345"
    And the user continues the checkout
    Then the checkout overview should list the following products:
      | name                     | price  |
      | Sauce Labs Backpack      | $29.99 |
      | Sauce Labs Fleece Jacket | $49.99 |
    And the item total should be "$79.98"
    And the total should equal the item total plus tax
    When the user finishes the order
    Then the order confirmation should show "Thank you for your order!"
    When the user logs out
    Then the user should be on the login page

  Scenario: A customer cancels at the last step, adjusts the cart and buys
    Given the user is logged in as "standard_user"
    When the user adds the following products to the cart:
      | Sauce Labs Backpack     |
      | Sauce Labs Bolt T-Shirt |
      | Sauce Labs Onesie       |
    And the user opens the cart
    And the user removes "Sauce Labs Onesie" from the cart
    Then the cart should contain the selected products
    When the user continues shopping
    Then the user should be on the products page
    When the user sorts the products by "Name (Z to A)"
    And the user adds "Test.allTheThings() T-Shirt (Red)" to the cart
    Then the cart badge should show 3
    When the user checks out with first name "Marwa", last name "Ashraf" and postal code "12345"
    Then the checkout overview should list the selected products
    When the user cancels the checkout
    Then the user should be on the products page
    And the cart badge should show 3
    When the user removes "Sauce Labs Bolt T-Shirt" from the cart
    And the user checks out with first name "Marwa", last name "Ashraf" and postal code "12345"
    Then the checkout overview should list the following products:
      | name                              | price  |
      | Sauce Labs Backpack               | $29.99 |
      | Test.allTheThings() T-Shirt (Red) | $15.99 |
    And the item total should be "$45.98"
    And the tax should be 8% of the item total
    And the total should equal the item total plus tax
    When the user finishes the order
    Then the order confirmation should show "Thank you for your order!"
    When the user goes back home
    Then the cart badge should not be displayed
    When the user logs out
    Then the user should be on the login page

  @persistence
  Scenario: The cart survives a refresh, a logout and a new login, then the order is placed
    Given the user is logged in as "standard_user"
    When the user adds the following products to the cart:
      | Sauce Labs Backpack      |
      | Sauce Labs Fleece Jacket |
    And the user refreshes the page
    Then the cart badge should show 2
    And the following products should show the "Remove" button:
      | Sauce Labs Backpack      |
      | Sauce Labs Fleece Jacket |
    When the user opens the cart
    And the user refreshes the page
    Then the cart should contain the following products:
      | name                     | price  |
      | Sauce Labs Backpack      | $29.99 |
      | Sauce Labs Fleece Jacket | $49.99 |
    When the user logs out
    Then the user should be on the login page
    When the user logs in as "standard_user"
    Then the user should be on the products page
    And the cart badge should show 2
    When the user checks out with first name "Marwa", last name "Ashraf" and postal code "12345"
    Then the checkout overview should list the selected products
    And the item total should be "$79.98"
    When the user finishes the order
    Then the order confirmation should show "Thank you for your order!"
    When the user logs out
    Then the user should be on the login page

  @menu
  Scenario: A customer resets the store, then shops again from scratch
    Given the user is logged in as "standard_user"
    When the user adds all products to the cart
    Then the cart badge should show 6
    When the user resets the app state
    Then the cart badge should not be displayed
    When the user opens the cart
    Then the cart should be empty
    When the user continues shopping
    And the user refreshes the page
    And the user adds "Sauce Labs Bike Light" to the cart
    And the user checks out with first name "Marwa", last name "Ashraf" and postal code "12345"
    Then the checkout overview should list the selected products
    And the item total should be "$9.99"
    When the user finishes the order
    Then the order confirmation should show "Thank you for your order!"
    When the user logs out
    Then the user should be on the login page
