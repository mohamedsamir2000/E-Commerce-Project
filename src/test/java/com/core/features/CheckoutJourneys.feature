@Checkout
Feature: Checkout Journeys
  Customer makes mistakes or changes their mind during checkout and still completes the order

  @Negative
  Scenario: Fix Every Missing Checkout Field One By One And Complete The Order
    Given Customer Login as a "StandardUser"
    And Add product "Sauce Labs Backpack" to cart
    And Open cart
    And Click on checkout
    Then Verify screen "Checkout Information" is displayed
    And Click on continue
    Then Verify error message "Error: First Name is required"
    And Close the error message
    Then Verify no error message is displayed
    And I fill the following fields:
      | label       | value  |
      | First Name  |        |
      | Last Name   | Ashraf |
      | Postal Code | 12345  |
    And Click on continue
    Then Verify error message "Error: First Name is required"
    And I fill the following fields:
      | label       | value |
      | First Name  | Marwa |
      | Last Name   |       |
      | Postal Code | 12345 |
    And Click on continue
    Then Verify error message "Error: Last Name is required"
    And I fill the following fields:
      | label       | value  |
      | First Name  | Marwa  |
      | Last Name   | Ashraf |
      | Postal Code |        |
    And Click on continue
    Then Verify error message "Error: Postal Code is required"
    And Verify screen "Checkout Information" is displayed
    And I fill the following fields:
      | label       | value  |
      | First Name  | Marwa  |
      | Last Name   | Ashraf |
      | Postal Code | 12345  |
    And Click on continue
    Then Verify screen "Checkout Overview" is displayed
    And Verify checkout overview lists the selected products
    And Verify checkout overview:
      | Item Total | $29.99 |
    And Click on finish
    Then Verify order confirmation "StoreData//ConfirmationHeader"
    And Customer Logout

  Scenario: Cancel On Each Checkout Step Before Finally Buying
    Given Customer Login as a "StandardUser"
    And Add the following products to cart:
      | Product               |
      | Sauce Labs Backpack   |
      | Sauce Labs Bike Light |
    And Open cart
    And Click on checkout
    And Click on cancel
    Then Verify screen "Cart" is displayed
    And Verify cart contains the selected products
    And Click on checkout
    And I fill the following fields:
      | label       | value  |
      | First Name  | Marwa  |
      | Last Name   | Ashraf |
      | Postal Code | 12345  |
    And Click on continue
    Then Verify screen "Checkout Overview" is displayed
    And Save the order total in "StoreData//OrderTotalBeforeCancel"
    And Click on cancel
    Then Verify screen "Products" is displayed
    And Verify cart badge is "2"
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
      | Payment Information  | StoreData//PaymentInformation     |
      | Shipping Information | StoreData//ShippingInformation    |
      | Item Total           | $39.98                            |
      | Total                | StoreData//OrderTotalBeforeCancel |
    And Verify order totals with "StoreData//TaxPercent" tax
    And Click on finish
    Then Verify screen "Checkout Complete" is displayed
    And Back home
    Then Verify screen "Products" is displayed
    And Verify cart badge is not displayed
    And Customer Logout

  Scenario Outline: Order Summary Is Correct For <Products>
    Given Customer Login as a "StandardUser"
    And Add the following products to cart:
      | Product         |
      | <FirstProduct>  |
      | <SecondProduct> |
    And Open cart
    And Click on checkout
    And I fill the following fields:
      | label       | value |
      | First Name  | Test  |
      | Last Name   | Total |
      | Postal Code | 12345 |
    And Click on continue
    Then Verify checkout overview lists the selected products
    And Verify checkout overview:
      | Item Total | <ItemTotal> |
      | Tax        | <Tax>       |
      | Total      | <Total>     |
    And Verify order totals with "StoreData//TaxPercent" tax
    And Click on finish
    Then Verify order confirmation "StoreData//ConfirmationHeader"
    And Customer Logout

    Examples:
      | Products              | FirstProduct             | SecondProduct                     | ItemTotal | Tax   | Total  |
      | Backpack + Bike Light | Sauce Labs Backpack      | Sauce Labs Bike Light             | $39.98    | $3.20 | $43.18 |
      | Jacket + Onesie       | Sauce Labs Fleece Jacket | Sauce Labs Onesie                 | $57.98    | $4.64 | $62.62 |
      | Bolt + Red T-Shirt    | Sauce Labs Bolt T-Shirt  | Test.allTheThings() T-Shirt (Red) | $31.98    | $2.56 | $34.54 |
