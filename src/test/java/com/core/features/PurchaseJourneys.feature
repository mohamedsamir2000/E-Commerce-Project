@Purchase
Feature: Purchase Journeys
  Customer goes from login to a placed order and back out in one session

  @Smoke
  Scenario Outline: Full Purchase From Login To Logout
    Given Customer Login as a "<User>"
    Then Verify the header logo is "Swag Labs"
    And Verify the products list:
      | Product                           | Price  |
      | Sauce Labs Backpack               | $29.99 |
      | Sauce Labs Bike Light             | $9.99  |
      | Sauce Labs Bolt T-Shirt           | $15.99 |
      | Sauce Labs Fleece Jacket          | $49.99 |
      | Sauce Labs Onesie                 | $7.99  |
      | Test.allTheThings() T-Shirt (Red) | $15.99 |
    And Verify every product has a name, description, price and image
    And Verify the active sort option is "Name (A to Z)"
    And Sort products by "Price (low to high)"
    Then Verify products are sorted by "Price (low to high)"
    And Add the following products to cart:
      | Product               |
      | Sauce Labs Onesie     |
      | Sauce Labs Bike Light |
    Then Verify the following products show the "Remove" button:
      | Product               |
      | Sauce Labs Onesie     |
      | Sauce Labs Bike Light |
    And Open product "Sauce Labs Fleece Jacket"
    Then Verify screen "Product Details" is displayed
    And Verify product details:
      | label | value                    |
      | Name  | Sauce Labs Fleece Jacket |
      | Price | $49.99                   |
    And Add product "Sauce Labs Fleece Jacket" to cart
    And Back to products
    Then Verify cart badge is "3"
    And Open cart
    Then Verify screen "Cart" is displayed
    And Verify cart contains the selected products
    And Click on checkout
    Then Verify screen "Checkout Information" is displayed
    And I fill the following fields:
      | label       | value        |
      | First Name  | <FirstName>  |
      | Last Name   | <LastName>   |
      | Postal Code | <PostalCode> |
    And Click on continue
    Then Verify screen "Checkout Overview" is displayed
    And Verify checkout overview lists the selected products
    And Verify checkout overview:
      | Payment Information  | StoreData//PaymentInformation  |
      | Shipping Information | StoreData//ShippingInformation |
      | Item Total           | $67.97                         |
    And Verify order totals with "StoreData//TaxPercent" tax
    And Click on finish
    Then Verify screen "Checkout Complete" is displayed
    And Verify order confirmation "StoreData//ConfirmationHeader"
    And Verify order confirmation text "StoreData//ConfirmationText"
    And Verify cart badge is not displayed
    And Back home
    Then Verify the following products show the "Add to cart" button:
      | Product                  |
      | Sauce Labs Onesie        |
      | Sauce Labs Bike Light    |
      | Sauce Labs Fleece Jacket |
    And Customer Logout

    Examples:
      | User                  | FirstName | LastName | PostalCode |
      | StandardUser          | Marwa     | Ashraf   | 12345      |
      | PerformanceGlitchUser | Mohamed   | Samir    | 11511      |

  @Sorting
  Scenario Outline: Sort By <SortOption> And Buy The First Two Products
    Given Customer Login as a "StandardUser"
    And Sort products by "<SortOption>"
    Then Verify products are sorted by "<SortOption>"
    And Verify the active sort option is "<SortOption>"
    And Add the following products to cart:
      | Product         |
      | <FirstProduct>  |
      | <SecondProduct> |
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
      | Item Total | <ItemTotal> |
    And Verify order totals with "StoreData//TaxPercent" tax
    And Click on finish
    Then Verify order confirmation "StoreData//ConfirmationHeader"
    And Back home
    Then Verify the active sort option is "Name (A to Z)"
    And Customer Logout

    Examples:
      | SortOption          | FirstProduct                      | SecondProduct            | ItemTotal |
      | Price (low to high) | Sauce Labs Onesie                 | Sauce Labs Bike Light    | $17.98    |
      | Price (high to low) | Sauce Labs Fleece Jacket          | Sauce Labs Backpack      | $79.98    |
      | Name (A to Z)       | Sauce Labs Backpack               | Sauce Labs Bike Light    | $39.98    |
      | Name (Z to A)       | Test.allTheThings() T-Shirt (Red) | Sauce Labs Onesie        | $23.98    |

  Scenario Outline: Buy Only "<Product>" From Its Details Screen
    Given Customer Login as a "StandardUser"
    And Open product "<Product>"
    Then Verify screen "Product Details" is displayed
    And Verify product details:
      | label | value     |
      | Name  | <Product> |
      | Price | <Price>   |
    And Verify product "<Product>" shows the "Add to cart" button
    And Add product "<Product>" to cart
    Then Verify cart badge is "1"
    And Open cart
    Then Verify cart contains the selected products
    And Click on checkout
    And I fill the following fields:
      | label       | value  |
      | First Name  | Marwa  |
      | Last Name   | Ashraf |
      | Postal Code | 12345  |
    And Click on continue
    Then Verify checkout overview lists the selected products
    And Verify checkout overview:
      | Item Total | <Price> |
    And Verify order totals with "StoreData//TaxPercent" tax
    And Click on finish
    Then Verify order confirmation "StoreData//ConfirmationHeader"
    And Customer Logout

    Examples:
      | Product                           | Price  |
      | Sauce Labs Backpack               | $29.99 |
      | Sauce Labs Bike Light             | $9.99  |
      | Sauce Labs Bolt T-Shirt           | $15.99 |
      | Sauce Labs Fleece Jacket          | $49.99 |
      | Sauce Labs Onesie                 | $7.99  |
      | Test.allTheThings() T-Shirt (Red) | $15.99 |

  Scenario: Open A Product By Its Image, Buy It, Then Buy The Whole Catalog
    Given Customer Login as a "StandardUser"
    And Open product "Sauce Labs Fleece Jacket" by its image
    Then Verify product details:
      | label | value                    |
      | Name  | Sauce Labs Fleece Jacket |
      | Price | $49.99                   |
    And Add product "Sauce Labs Fleece Jacket" to cart
    And Open cart
    And Click on checkout
    And I fill the following fields:
      | label       | value  |
      | First Name  | Marwa  |
      | Last Name   | Ashraf |
      | Postal Code | 12345  |
    And Click on continue
    Then Verify checkout overview:
      | Item Total | $49.99 |
    And Click on finish
    And Back home
    And Add all products to cart
    Then Verify cart badge is "6"
    And Open cart
    Then Verify cart contains the selected products
    And Click on checkout
    And I fill the following fields:
      | label       | value  |
      | First Name  | Marwa  |
      | Last Name   | Ashraf |
      | Postal Code | 12345  |
    And Click on continue
    Then Verify checkout overview lists the selected products
    And Verify checkout overview:
      | Item Total | $129.94 |
    And Verify order totals with "StoreData//TaxPercent" tax
    And Click on finish
    Then Verify order confirmation "StoreData//ConfirmationHeader"
    And Verify cart badge is not displayed
    And Customer Logout
