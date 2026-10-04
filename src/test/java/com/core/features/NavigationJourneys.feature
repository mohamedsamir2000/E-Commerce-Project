@Navigation
Feature: Navigation Journeys
  Customer moves around the store with the side menu and reaches Sauce Labs from the footer

  @Menu
  Scenario: Use The Side Menu From Every Screen While Shopping
    Given Customer Login as a "StandardUser"
    And Open the menu
    Then Verify the menu is open
    And Verify the menu options:
      | Option          |
      | All Items       |
      | About           |
      | Logout          |
      | Reset App State |
    And Close the menu
    Then Verify the menu is closed
    And Open product "Sauce Labs Backpack"
    And Add product "Sauce Labs Backpack" to cart
    And Select "All Items" from the menu
    Then Verify screen "Products" is displayed
    And Verify cart badge is "1"
    And Open cart
    And Select "All Items" from the menu
    Then Verify screen "Products" is displayed
    And Verify cart badge is "1"
    And Add product "Sauce Labs Bike Light" to cart
    And Open cart
    And Click on checkout
    And Select "All Items" from the menu
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
    And Click on finish
    Then Verify order confirmation "StoreData//ConfirmationHeader"
    And Customer Logout

  @Footer
  Scenario: Check Sauce Labs Social Pages From The Footer And Keep Shopping
    Given Customer Login as a "StandardUser"
    Then Verify the footer links:
      | Link     |
      | X        |
      | Facebook |
      | LinkedIn |
    And Verify the footer text contains "Sauce Labs. All Rights Reserved."
    And Add product "Sauce Labs Fleece Jacket" to cart
    And Click on "X" footer link
    Then Verify a new tab opens with URL containing "x.com/saucelabs"
    And Close the new tab
    And Click on "Facebook" footer link
    Then Verify a new tab opens with URL containing "facebook.com/saucelabs"
    And Close the new tab
    And Click on "LinkedIn" footer link
    Then Verify a new tab opens with URL containing "linkedin.com/company/sauce-labs"
    And Close the new tab
    Then Verify screen "Products" is displayed
    And Verify cart badge is "1"
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
    Then Verify order confirmation "StoreData//ConfirmationHeader"
    And Customer Logout

  @Menu @About
  Scenario: Finish An Order And Visit Sauce Labs Through About
    Given Customer Login as a "StandardUser"
    And Add product "Sauce Labs Bolt T-Shirt" to cart
    And Open cart
    And Click on checkout
    And I fill the following fields:
      | label       | value  |
      | First Name  | Marwa  |
      | Last Name   | Ashraf |
      | Postal Code | 12345  |
    And Click on continue
    And Click on finish
    Then Verify order confirmation "StoreData//ConfirmationHeader"
    And Back home
    And Select "About" from the menu
    Then Verify the URL contains "saucelabs.com"
