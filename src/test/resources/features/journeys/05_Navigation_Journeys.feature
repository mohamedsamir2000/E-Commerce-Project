@journey @navigation
Feature: Navigation journeys with the side menu and the footer
  As a Swag Labs customer
  I want to move around the store with the menu and reach Sauce Labs from the footer
  So that I never lose my way or my cart while shopping

  @menu
  Scenario: A customer uses the side menu from every page while shopping
    Given the user is logged in as "standard_user"
    When the user opens the menu
    Then the menu should be open
    And the menu should contain the following options:
      | All Items       |
      | About           |
      | Logout          |
      | Reset App State |
    When the user closes the menu
    Then the menu should be closed
    # From a product details page
    When the user opens the product "Sauce Labs Backpack"
    And the user adds "Sauce Labs Backpack" to the cart
    And the user selects "All Items" from the menu
    Then the user should be on the products page
    And the cart badge should show 1
    # From the cart
    When the user opens the cart
    And the user selects "All Items" from the menu
    Then the user should be on the products page
    And the cart badge should show 1
    # From the checkout information page
    When the user adds "Sauce Labs Bike Light" to the cart
    And the user opens the cart
    And the user proceeds to checkout
    And the user selects "All Items" from the menu
    Then the user should be on the products page
    And the cart badge should show 2
    # Finish the order and leave through the menu
    When the user checks out with first name "Marwa", last name "Ashraf" and postal code "12345"
    Then the checkout overview should list the selected products
    When the user finishes the order
    Then the order confirmation should show "Thank you for your order!"
    When the user logs out
    Then the user should be on the login page

  @footer
  Scenario: A customer checks Sauce Labs' social pages from the footer and keeps shopping
    Given the user is logged in as "standard_user"
    Then the footer should show the "X" link
    And the footer should show the "Facebook" link
    And the footer should show the "LinkedIn" link
    And the footer should contain the text "Sauce Labs. All Rights Reserved."
    When the user adds "Sauce Labs Fleece Jacket" to the cart
    And the user clicks the "X" link in the footer
    Then a new tab should open with a URL containing "x.com/saucelabs"
    When the user closes the new tab and returns to the store
    And the user clicks the "Facebook" link in the footer
    Then a new tab should open with a URL containing "facebook.com/saucelabs"
    When the user closes the new tab and returns to the store
    And the user clicks the "LinkedIn" link in the footer
    Then a new tab should open with a URL containing "linkedin.com/company/sauce-labs"
    When the user closes the new tab and returns to the store
    Then the user should be on the products page
    And the cart badge should show 1
    When the user checks out with first name "Marwa", last name "Ashraf" and postal code "12345"
    Then the item total should be "$49.99"
    When the user finishes the order
    Then the order confirmation should show "Thank you for your order!"
    When the user logs out
    Then the user should be on the login page

  @menu @about
  Scenario: A customer finishes an order and then visits Sauce Labs through About
    Given the user is logged in as "standard_user"
    When the user adds "Sauce Labs Bolt T-Shirt" to the cart
    And the user checks out with first name "Marwa", last name "Ashraf" and postal code "12345"
    And the user finishes the order
    Then the order confirmation should show "Thank you for your order!"
    When the user goes back home
    And the user selects "About" from the menu
    Then the current URL should contain "saucelabs.com"
