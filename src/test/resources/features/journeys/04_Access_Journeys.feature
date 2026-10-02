@journey @authentication
Feature: Access journeys
  As a Swag Labs customer
  I want the store to be closed to anyone who is not logged in
  So that my cart and orders stay mine

  @negative @security
  Scenario: A visitor is turned away, recovers from login mistakes, shops and is locked out again after logout
    Given the user is on the login page
    # Pages are closed before login
    When the user opens the products page directly
    Then the user should be on the login page
    And the error message "Epic sadface: You can only access '/inventory.html' when you are logged in." should be displayed
    When the user opens the cart page directly
    Then the error message "Epic sadface: You can only access '/cart.html' when you are logged in." should be displayed
    When the user opens the checkout information page directly
    Then the error message "Epic sadface: You can only access '/checkout-step-one.html' when you are logged in." should be displayed
    When the user opens the checkout overview page directly
    Then the error message "Epic sadface: You can only access '/checkout-step-two.html' when you are logged in." should be displayed
    When the user opens the checkout complete page directly
    Then the error message "Epic sadface: You can only access '/checkout-complete.html' when you are logged in." should be displayed
    # Login mistakes
    When the user logs in as "" with password ""
    Then the error message "Epic sadface: Username is required" should be displayed
    When the user logs in as "standard_user" with password ""
    Then the error message "Epic sadface: Password is required" should be displayed
    When the user logs in as "standard_user" with password "wrong_password"
    Then the error message "Epic sadface: Username and password do not match any user in this service" should be displayed
    When the user logs in as "unknown_user" with password "secret_sauce"
    Then the error message "Epic sadface: Username and password do not match any user in this service" should be displayed
    When the user dismisses the error message
    Then no error message should be displayed
    # Successful session
    When the user logs in as "standard_user"
    Then the user should be on the products page
    When the user adds "Sauce Labs Onesie" to the cart
    And the user checks out with first name "Marwa", last name "Ashraf" and postal code "12345"
    And the user finishes the order
    Then the order confirmation should show "Thank you for your order!"
    When the user logs out
    Then the user should be on the login page
    # Closed again after logout
    When the user opens the products page directly
    Then the user should be on the login page
    And the error message "Epic sadface: You can only access '/inventory.html' when you are logged in." should be displayed
    When the user opens the cart page directly
    Then the error message "Epic sadface: You can only access '/cart.html' when you are logged in." should be displayed

  @negative
  Scenario: A locked out customer cannot get in, and another customer can use the store afterwards
    Given the user is on the login page
    When the user logs in as "locked_out_user"
    Then the error message "Epic sadface: Sorry, this user has been locked out." should be displayed
    And the user should be on the login page
    When the user opens the products page directly
    Then the user should be on the login page
    When the user logs in as "standard_user"
    Then the user should be on the products page
    And the cart badge should not be displayed
    When the user adds "Sauce Labs Backpack" to the cart
    And the user checks out with first name "Marwa", last name "Ashraf" and postal code "12345"
    And the user finishes the order
    Then the order confirmation should show "Thank you for your order!"
    When the user logs out
    Then the user should be on the login page
