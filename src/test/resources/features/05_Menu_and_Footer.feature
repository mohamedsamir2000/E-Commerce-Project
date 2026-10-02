@navigation
Feature: Side menu, header and footer
  As a logged in customer
  I want to use the side menu and the footer links
  So that I can navigate the store and reach Sauce Labs

  Background:
    Given the user is logged in as "standard_user"

  @menu
  Scenario: The side menu opens with all its options and can be closed
    When the user opens the menu
    Then the menu should be open
    And the menu should contain the following options:
      | All Items       |
      | About           |
      | Logout          |
      | Reset App State |
    When the user closes the menu
    Then the menu should be closed

  @menu @ClickAllItems
  Scenario Outline: "All Items" returns to the products page from the <page> page
    When the user adds "Sauce Labs Backpack" to the cart
    And the user opens the <page> page directly
    And the user selects "All Items" from the menu
    Then the user should be on the products page
    And the cart badge should show 1

    Examples:
      | page                 |
      | cart                 |
      | checkout information |

  @menu @ClickAllItems
  Scenario: "All Items" returns to the products page from a product details page
    When the user opens the product "Sauce Labs Backpack"
    And the user selects "All Items" from the menu
    Then the user should be on the products page

  @menu @ClickAbout
  Scenario: "About" opens the Sauce Labs website
    When the user selects "About" from the menu
    Then the current URL should contain "saucelabs.com"

  @footer
  Scenario: The footer shows the social links and copyright
    Then the footer should show the "X" link
    And the footer should show the "Facebook" link
    And the footer should show the "LinkedIn" link
    And the footer should contain the text "Sauce Labs. All Rights Reserved."

  @footer @SocialIcons
  Scenario Outline: The <network> footer link opens Sauce Labs' page in a new tab
    When the user clicks the "<network>" link in the footer
    Then a new tab should open with a URL containing "<url>"

    Examples:
      | network  | url                                     |
      | X        | x.com/saucelabs                         |
      | Facebook | facebook.com/saucelabs                  |
      | LinkedIn | linkedin.com/company/sauce-labs         |
