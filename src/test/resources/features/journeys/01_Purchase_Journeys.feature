@journey @purchase
Feature: Purchase journeys
  As a Swag Labs customer
  I want to go from login to a placed order and back out
  So that I can buy products in one complete session

  @smoke
  Scenario Outline: "<username>" browses, fills the cart, checks out and logs out
    # Login
    Given the user is on the login page
    When the user logs in as "<username>"
    Then the user should be on the products page
    And the header should show the logo "Swag Labs"
    And the products page should list the following products:
      | name                              | price  |
      | Sauce Labs Backpack               | $29.99 |
      | Sauce Labs Bike Light             | $9.99  |
      | Sauce Labs Bolt T-Shirt           | $15.99 |
      | Sauce Labs Fleece Jacket          | $49.99 |
      | Sauce Labs Onesie                 | $7.99  |
      | Test.allTheThings() T-Shirt (Red) | $15.99 |
    And every product should have a name, description, price and image
    And the active sort option should be "Name (A to Z)"
    # Browse and choose
    When the user sorts the products by "Price (low to high)"
    Then the products should be sorted by "Price (low to high)"
    When the user adds the following products to the cart:
      | Sauce Labs Onesie     |
      | Sauce Labs Bike Light |
    Then the following products should show the "Remove" button:
      | Sauce Labs Onesie     |
      | Sauce Labs Bike Light |
    When the user opens the product "Sauce Labs Fleece Jacket"
    Then the user should be on the product details page
    And the product details page should show "Sauce Labs Fleece Jacket" priced at "$49.99"
    When the user adds "Sauce Labs Fleece Jacket" to the cart
    Then the product "Sauce Labs Fleece Jacket" should show the "Remove" button
    When the user goes back to the products page
    Then the cart badge should show 3
    # Review the cart
    When the user opens the cart
    Then the user should be on the cart page
    And the cart should contain 3 products
    And the cart should contain the selected products
    # Checkout
    When the user proceeds to checkout
    Then the user should be on the checkout information page
    When the user enters the checkout information:
      | first name | last name | postal code |
      | Marwa      | Ashraf    | 12345       |
    And the user continues the checkout
    Then the user should be on the checkout overview page
    And the checkout overview should list the selected products
    And the payment information should be "SauceCard #31337"
    And the shipping information should be "Free Pony Express Delivery!"
    And the item total should be "$67.97"
    And the item total should equal the sum of the product prices
    And the tax should be 8% of the item total
    And the total should equal the item total plus tax
    # Place the order
    When the user finishes the order
    Then the user should be on the checkout complete page
    And the order confirmation should show "Thank you for your order!"
    And the order confirmation text should be "Your order has been dispatched, and will arrive just as fast as the pony can get there!"
    And the cart badge should not be displayed
    When the user goes back home
    Then the user should be on the products page
    And the following products should show the "Add to cart" button:
      | Sauce Labs Onesie        |
      | Sauce Labs Bike Light    |
      | Sauce Labs Fleece Jacket |
    # Leave
    When the user logs out
    Then the user should be on the login page

    Examples:
      | username                |
      | standard_user           |
      | performance_glitch_user |

  @sorting
  Scenario Outline: A customer sorts by <sort option> and buys the first two products listed
    Given the user is logged in as "standard_user"
    When the user sorts the products by "<sort option>"
    Then the products should be sorted by "<sort option>"
    And the active sort option should be "<sort option>"
    When the user adds the following products to the cart:
      | <first>  |
      | <second> |
    And the user checks out with first name "Marwa", last name "Ashraf" and postal code "12345"
    Then the checkout overview should list the selected products
    And the item total should be "<item total>"
    And the tax should be 8% of the item total
    And the total should equal the item total plus tax
    When the user finishes the order
    Then the order confirmation should show "Thank you for your order!"
    When the user goes back home
    Then the active sort option should be "Name (A to Z)"
    When the user logs out
    Then the user should be on the login page

    Examples:
      | sort option         | first                             | second                   | item total |
      | Price (low to high) | Sauce Labs Onesie                 | Sauce Labs Bike Light    | $17.98     |
      | Price (high to low) | Sauce Labs Fleece Jacket          | Sauce Labs Backpack      | $79.98     |
      | Name (A to Z)       | Sauce Labs Backpack               | Sauce Labs Bike Light    | $39.98     |
      | Name (Z to A)       | Test.allTheThings() T-Shirt (Red) | Sauce Labs Onesie        | $23.98     |

  Scenario Outline: A customer inspects "<product>" on its details page and buys only that
    Given the user is logged in as "standard_user"
    When the user opens the product "<product>"
    Then the user should be on the product details page
    And the product details page should show "<product>" priced at "<price>"
    And the product "<product>" should show the "Add to cart" button
    When the user adds "<product>" to the cart
    Then the cart badge should show 1
    When the user opens the cart
    Then the cart should contain the selected products
    When the user proceeds to checkout
    And the user enters first name "Marwa", last name "Ashraf" and postal code "12345"
    And the user continues the checkout
    Then the checkout overview should list the selected products
    And the item total should be "<price>"
    And the tax should be 8% of the item total
    And the total should equal the item total plus tax
    When the user finishes the order
    Then the order confirmation should show "Thank you for your order!"
    When the user logs out
    Then the user should be on the login page

    Examples:
      | product                           | price  |
      | Sauce Labs Backpack               | $29.99 |
      | Sauce Labs Bike Light             | $9.99  |
      | Sauce Labs Bolt T-Shirt           | $15.99 |
      | Sauce Labs Fleece Jacket          | $49.99 |
      | Sauce Labs Onesie                 | $7.99  |
      | Test.allTheThings() T-Shirt (Red) | $15.99 |

  Scenario: A customer opens a product by its image, buys it, then buys the whole catalog
    Given the user is logged in as "standard_user"
    When the user opens the product "Sauce Labs Fleece Jacket" by its image
    Then the product details page should show "Sauce Labs Fleece Jacket" priced at "$49.99"
    When the user adds "Sauce Labs Fleece Jacket" to the cart
    And the user checks out with first name "Marwa", last name "Ashraf" and postal code "12345"
    Then the item total should be "$49.99"
    When the user finishes the order
    And the user goes back home
    And the user adds all products to the cart
    Then the cart badge should show 6
    When the user opens the cart
    Then the cart should contain 6 products
    And the cart should contain the selected products
    When the user proceeds to checkout
    And the user enters first name "Marwa", last name "Ashraf" and postal code "12345"
    And the user continues the checkout
    Then the checkout overview should list the selected products
    And the item total should be "$129.94"
    And the tax should be 8% of the item total
    And the total should equal the item total plus tax
    When the user finishes the order
    Then the order confirmation should show "Thank you for your order!"
    And the cart badge should not be displayed
    When the user logs out
    Then the user should be on the login page
