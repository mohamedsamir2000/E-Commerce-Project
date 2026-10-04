@Cart
Feature: Cart Journeys
  Customer changes the cart on every screen before buying, and the cart survives refresh and re-login

  Scenario: Change The Cart On Every Screen Before Buying
    Given Customer Login as a "StandardUser"
    And Open cart
    Then Verify screen "Cart" is displayed
    And Verify cart is empty
    And Continue shopping
    And Add all products to cart
    Then Verify cart badge is "6"
    And Remove the following products from cart:
      | Product               |
      | Sauce Labs Bike Light |
      | Sauce Labs Onesie     |
    Then Verify cart badge is "4"
    And Verify the following products show the "Add to cart" button:
      | Product               |
      | Sauce Labs Bike Light |
      | Sauce Labs Onesie     |
    And Open product "Sauce Labs Bolt T-Shirt"
    And Remove product "Sauce Labs Bolt T-Shirt" from cart
    Then Verify product "Sauce Labs Bolt T-Shirt" shows the "Add to cart" button
    And Verify cart badge is "3"
    And Open cart
    And Open product "Sauce Labs Fleece Jacket"
    Then Verify product details:
      | label | value                    |
      | Name  | Sauce Labs Fleece Jacket |
      | Price | $49.99                   |
    And Verify product "Sauce Labs Fleece Jacket" shows the "Remove" button
    And Open cart
    And Remove product "Test.allTheThings() T-Shirt (Red)" from cart
    Then Verify cart contains the selected products
    And Verify cart badge matches the selected products
    And Click on checkout
    And I fill the following fields:
      | label       | value  |
      | First Name  | Marwa  |
      | Last Name   | Ashraf |
      | Postal Code | 12345  |
    And Click on continue
    Then Verify checkout overview lists the following products:
      | Product                  | Price  |
      | Sauce Labs Backpack      | $29.99 |
      | Sauce Labs Fleece Jacket | $49.99 |
    And Verify checkout overview:
      | Item Total | $79.98 |
    And Verify order totals with "StoreData//TaxPercent" tax
    And Click on finish
    Then Verify order confirmation "StoreData//ConfirmationHeader"
    And Customer Logout

  Scenario: Cancel At The Last Step, Adjust The Cart And Buy
    Given Customer Login as a "StandardUser"
    And Add the following products to cart:
      | Product                 |
      | Sauce Labs Backpack     |
      | Sauce Labs Bolt T-Shirt |
      | Sauce Labs Onesie       |
    And Open cart
    And Remove product "Sauce Labs Onesie" from cart
    Then Verify cart contains the selected products
    And Continue shopping
    And Sort products by "Name (Z to A)"
    And Add product "Test.allTheThings() T-Shirt (Red)" to cart
    Then Verify cart badge is "3"
    And Open cart
    And Click on checkout
    And I fill the following fields:
      | label       | value  |
      | First Name  | Marwa  |
      | Last Name   | Ashraf |
      | Postal Code | 12345  |
    And Click on continue
    Then Verify checkout overview lists the selected products
    And Click on cancel
    Then Verify screen "Products" is displayed
    And Verify cart badge is "3"
    And Remove product "Sauce Labs Bolt T-Shirt" from cart
    And Open cart
    And Click on checkout
    And I fill the following fields:
      | label       | value  |
      | First Name  | Marwa  |
      | Last Name   | Ashraf |
      | Postal Code | 12345  |
    And Click on continue
    Then Verify checkout overview lists the following products:
      | Product                           | Price  |
      | Sauce Labs Backpack               | $29.99 |
      | Test.allTheThings() T-Shirt (Red) | $15.99 |
    And Verify checkout overview:
      | Item Total | $45.98 |
    And Verify order totals with "StoreData//TaxPercent" tax
    And Click on finish
    Then Verify order confirmation "StoreData//ConfirmationHeader"
    And Back home
    Then Verify cart badge is not displayed
    And Customer Logout

  @Persistence
  Scenario: Cart Survives Refresh, Logout And Login Again
    Given Customer Login as a "StandardUser"
    And Add the following products to cart:
      | Product                  |
      | Sauce Labs Backpack      |
      | Sauce Labs Fleece Jacket |
    And Refresh the page
    Then Verify cart badge is "2"
    And Verify the following products show the "Remove" button:
      | Product                  |
      | Sauce Labs Backpack      |
      | Sauce Labs Fleece Jacket |
    And Open cart
    And Refresh the page
    Then Verify cart contains the following products:
      | Product                  | Price  |
      | Sauce Labs Backpack      | $29.99 |
      | Sauce Labs Fleece Jacket | $49.99 |
    And Click on checkout
    And I fill the following fields:
      | label       | value  |
      | First Name  | Marwa  |
      | Last Name   | Ashraf |
      | Postal Code | 12345  |
    And Click on continue
    And Save the order total in "StoreData//OrderTotalBeforeLogout"
    And Customer Logout
    And Customer Login as a "StandardUser"
    Then Verify cart badge is "2"
    And Open cart
    And Click on checkout
    And I fill the following fields:
      | label       | value  |
      | First Name  | Marwa  |
      | Last Name   | Ashraf |
      | Postal Code | 12345  |
    And Click on continue
    Then Verify checkout overview lists the selected products
    And Verify checkout overview:
      | Item Total | $79.98                            |
      | Total      | StoreData//OrderTotalBeforeLogout |
    And Click on finish
    Then Verify order confirmation "StoreData//ConfirmationHeader"
    And Customer Logout

  @Menu
  Scenario: Reset The Store, Then Shop Again From Scratch
    Given Customer Login as a "StandardUser"
    And Add all products to cart
    Then Verify cart badge is "6"
    And Reset app state
    Then Verify cart badge is not displayed
    And Open cart
    Then Verify cart is empty
    And Continue shopping
    And Refresh the page
    And Add product "Sauce Labs Bike Light" to cart
    And Open cart
    And Click on checkout
    And I fill the following fields:
      | label       | value  |
      | First Name  | Marwa  |
      | Last Name   | Ashraf |
      | Postal Code | 12345  |
    And Click on continue
    Then Verify checkout overview lists the selected products
    And Verify checkout overview:
      | Item Total | $9.99 |
    And Click on finish
    Then Verify order confirmation "StoreData//ConfirmationHeader"
    And Customer Logout
