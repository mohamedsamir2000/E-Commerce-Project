@Login
Feature: Login and Logout
  As a Swag Labs customer
  I want to log in and out of the store
  So that only I can access my account

  @TestLogin
  Scenario Outline: Login with "<username>" / "<password>" redirects to the expected page
    Given the user is on the login page
    When the user logs in with username "<username>" and password "<password>"
    Then the current URL should be "<expectedUrl>"

    Examples:
      | username                | password     | expectedUrl                              |
      | standard_user           | secret_sauce | https://www.saucedemo.com/inventory.html |
      | locked_out_user         | secret_sauce | https://www.saucedemo.com/               |
      | problem_user            | secret_sauce | https://www.saucedemo.com/inventory.html |
      | performance_glitch_user | secret_sauce | https://www.saucedemo.com/inventory.html |
      | error_user              | secret_sauce | https://www.saucedemo.com/inventory.html |
      | visual_user             | secret_sauce | https://www.saucedemo.com/inventory.html |
      | hhhhhhhhhh              | secret_sauce | https://www.saucedemo.com/               |
      |                         | secret_sauce | https://www.saucedemo.com/               |
      | standard_user           |              | https://www.saucedemo.com/               |
      | standard_user           | hhhhhh       | https://www.saucedemo.com/               |

  @TestLogout
  Scenario Outline: "<username>" can log out from the side menu
    Given the user is logged in as "<username>" with password "<password>"
    When the user logs out
    Then the user should be on the login page
    And the current URL should be "https://www.saucedemo.com/"

    Examples:
      | username                | password     |
      | standard_user           | secret_sauce |
      | problem_user            | secret_sauce |
      | performance_glitch_user | secret_sauce |
      | error_user              | secret_sauce |
      | visual_user             | secret_sauce |
