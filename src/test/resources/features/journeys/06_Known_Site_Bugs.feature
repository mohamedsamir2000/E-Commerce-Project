@journey @known_issue
Feature: Journeys that hit the site's intentional bugs
  Swag Labs ships demo users with deliberate defects. These journeys assert the correct behaviour,
  so they are expected to FAIL and document each bug. They are skipped by default; run them with:
    mvn test -Dcucumber.filter.tags="@known_issue"

  # Known defects:
  #   problem_user - sorting does nothing, some products cannot be added/removed, product links open the
  #                  wrong product, Last Name cannot be typed so checkout never reaches the overview
  #   error_user   - sorting raises an alert and does nothing, some products cannot be added/removed,
  #                  Finish does not place the order
  #   visual_user  - prices on the products page are random and differ from the cart and checkout

  Scenario Outline: "<username>" browses, fills the cart, checks out and logs out
    Given the user is on the login page
    When the user logs in as "<username>"
    Then the user should be on the products page
    And the products page should list the following products:
      | name                              | price  |
      | Sauce Labs Backpack               | $29.99 |
      | Sauce Labs Bike Light             | $9.99  |
      | Sauce Labs Bolt T-Shirt           | $15.99 |
      | Sauce Labs Fleece Jacket          | $49.99 |
      | Sauce Labs Onesie                 | $7.99  |
      | Test.allTheThings() T-Shirt (Red) | $15.99 |
    When the user sorts the products by "Price (low to high)"
    Then the products should be sorted by "Price (low to high)"
    When the user adds all products to the cart
    Then the cart badge should show 6
    When the user opens the product "Sauce Labs Bolt T-Shirt"
    Then the product details page should show "Sauce Labs Bolt T-Shirt" priced at "$15.99"
    When the user opens the cart
    Then the cart should contain the selected products
    When the user proceeds to checkout
    And the user enters first name "Marwa", last name "Ashraf" and postal code "12345"
    And the user continues the checkout
    Then the user should be on the checkout overview page
    And the item total should be "$129.94"
    When the user finishes the order
    Then the user should be on the checkout complete page
    And the order confirmation should show "Thank you for your order!"
    When the user logs out
    Then the user should be on the login page

    Examples:
      | username     |
      | problem_user |
      | error_user   |
      | visual_user  |

  Scenario: Product names do not contain digits or special characters
    # "Test.allTheThings() T-Shirt (Red)" contains brackets and dots
    Given the user is logged in as "standard_user"
    Then no product name should contain digits or special characters
