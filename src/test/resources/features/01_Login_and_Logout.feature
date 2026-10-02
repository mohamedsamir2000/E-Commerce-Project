@authentication
Feature: Login and logout
  As a Swag Labs customer
  I want to log in and out of the store
  So that only I can access my account

  Background:
    Given the user is on the login page

  @smoke @TestLogin
  Scenario Outline: "<username>" can log in and lands on the products page
    When the user logs in as "<username>"
    Then the user should be on the products page
    And the header should show the logo "Swag Labs"
    And the products page should show 6 products

    Examples:
      | username                |
      | standard_user           |
      | problem_user            |
      | performance_glitch_user |
      | error_user              |
      | visual_user             |

  @negative @TestLogin
  Scenario Outline: Login is rejected – <case>
    When the user logs in as "<username>" with password "<password>"
    Then the error message "<error>" should be displayed
    And the user should be on the login page

    Examples:
      | case                     | username        | password       | error                                                                     |
      | locked out user          | locked_out_user | secret_sauce   | Epic sadface: Sorry, this user has been locked out.                       |
      | unknown username         | hhhhhhhhhh      | secret_sauce   | Epic sadface: Username and password do not match any user in this service |
      | wrong password           | standard_user   | hhhhhh         | Epic sadface: Username and password do not match any user in this service |
      | missing username         |                 | secret_sauce   | Epic sadface: Username is required                                        |
      | missing password         | standard_user   |                | Epic sadface: Password is required                                        |
      | missing both credentials |                 |                | Epic sadface: Username is required                                        |

  @negative
  Scenario: The login error message can be dismissed
    When the user logs in as "locked_out_user"
    Then the error message "Epic sadface: Sorry, this user has been locked out." should be displayed
    When the user dismisses the error message
    Then no error message should be displayed

  @security
  Scenario Outline: The <page> page cannot be opened without logging in
    When the user opens the <page> page directly
    Then the user should be on the login page
    And the error message "Epic sadface: You can only access '/<path>' when you are logged in." should be displayed

    Examples:
      | page                 | path                   |
      | products             | inventory.html         |
      | cart                 | cart.html              |
      | checkout information | checkout-step-one.html |
      | checkout overview    | checkout-step-two.html |
      | checkout complete    | checkout-complete.html |

  @smoke @TestLogout
  Scenario Outline: "<username>" can log out from the side menu
    Given the user is logged in as "<username>"
    When the user logs out
    Then the user should be on the login page

    Examples:
      | username                |
      | standard_user           |
      | problem_user            |
      | performance_glitch_user |
      | error_user              |
      | visual_user             |

  @security
  Scenario: After logout the products page is no longer accessible
    Given the user is logged in as "standard_user"
    When the user logs out
    And the user opens the products page directly
    Then the user should be on the login page
    And the error message "Epic sadface: You can only access '/inventory.html' when you are logged in." should be displayed
