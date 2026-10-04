@Authentication
Feature: Access Journeys
  The store is closed to anyone who is not logged in

  @Negative @Security
  Scenario: Visitor Is Turned Away, Recovers From Login Mistakes, Shops And Is Locked Out After Logout
    Given Customer Open the store
    And Customer Open screen "Products"
    Then Verify screen "Login" is displayed
    And Verify error message "Epic sadface: You can only access '/inventory.html' when you are logged in."
    And Customer Open screen "Cart"
    Then Verify error message "Epic sadface: You can only access '/cart.html' when you are logged in."
    And Customer Open screen "Checkout Information"
    Then Verify error message "Epic sadface: You can only access '/checkout-step-one.html' when you are logged in."
    And Customer Open screen "Checkout Overview"
    Then Verify error message "Epic sadface: You can only access '/checkout-step-two.html' when you are logged in."
    And Customer Open screen "Checkout Complete"
    Then Verify error message "Epic sadface: You can only access '/checkout-complete.html' when you are logged in."
    And Customer try to login with username "" and password ""
    Then Verify error message "Epic sadface: Username is required"
    And Customer try to login with username "standard_user" and password ""
    Then Verify error message "Epic sadface: Password is required"
    And Customer try to login with username "standard_user" and password "wrong_password"
    Then Verify error message "Epic sadface: Username and password do not match any user in this service"
    And Customer try to login with username "unknown_user" and password "secret_sauce"
    Then Verify error message "Epic sadface: Username and password do not match any user in this service"
    And Close the error message
    Then Verify no error message is displayed
    And Customer Login as a "StandardUser"
    And Add product "Sauce Labs Onesie" to cart
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
    And Customer Logout
    And Customer Open screen "Products"
    Then Verify screen "Login" is displayed
    And Verify error message "Epic sadface: You can only access '/inventory.html' when you are logged in."
    And Customer Open screen "Cart"
    Then Verify error message "Epic sadface: You can only access '/cart.html' when you are logged in."

  @Negative
  Scenario: Locked Out Customer Cannot Get In, And Another Customer Can Shop Afterwards
    Given Customer Open the store
    And Customer try to login as a "LockedOutUser"
    Then Verify error message "Epic sadface: Sorry, this user has been locked out."
    And Verify screen "Login" is displayed
    And Customer Open screen "Products"
    Then Verify screen "Login" is displayed
    And Customer Login as a "StandardUser"
    Then Verify cart badge is not displayed
    And Add product "Sauce Labs Backpack" to cart
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
    And Customer Logout
