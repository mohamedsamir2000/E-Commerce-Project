@MenuAndIcons
Feature: Menu and footer icons
  As a logged in customer
  I want to use the side menu and the social media links
  So that I can navigate the site and reach Sauce Labs

  @LoginAllUsers
  Scenario Outline: "<username>" lands on the home page after login
    Given the user is logged in as "<username>" with password "<password>"
    Then the inventory page should be displayed

    Examples:
      | username                | password     |
      | standard_user           | secret_sauce |
      | problem_user            | secret_sauce |
      | performance_glitch_user | secret_sauce |
      | error_user              | secret_sauce |
      | visual_user             | secret_sauce |

  @ClickAllItems
  Scenario: "All Items" in the side menu opens the inventory page
    Given the user is logged in as "standard_user" with password "secret_sauce"
    When the user opens the side menu
    And the user clicks "All Items" in the side menu
    Then the current URL should be "https://www.saucedemo.com/inventory.html"

  @ClickAbout
  Scenario: "About" in the side menu opens the Sauce Labs website
    Given the user is logged in as "standard_user" with password "secret_sauce"
    When the user opens the side menu
    And the user clicks "About" in the side menu
    Then the current URL should contain "saucelabs.com"

  @SocialIcons
  Scenario Outline: The "<network>" footer icon opens Sauce Labs' page in a new tab
    Given the user is logged in as "standard_user" with password "secret_sauce"
    When the user clicks the "<network>" icon in the footer
    Then a new tab should open with a URL containing "<expectedUrl>"

    Examples:
      | network  | expectedUrl                                 |
      | Twitter  | https://x.com/saucelabs                     |
      | Facebook | https://www.facebook.com/saucelabs          |
      | LinkedIn | https://www.linkedin.com/company/sauce-labs |
