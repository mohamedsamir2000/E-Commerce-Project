@products
Feature: Products catalog
  As a logged in customer
  I want to browse, sort and inspect the products
  So that I can choose what to buy

  @smoke
  Scenario Outline: The catalog shows all products with their prices for "<username>"
    Given the user is logged in as "<username>"
    Then the products page should list the following products:
      | name                              | price  |
      | Sauce Labs Backpack               | $29.99 |
      | Sauce Labs Bike Light             | $9.99  |
      | Sauce Labs Bolt T-Shirt           | $15.99 |
      | Sauce Labs Fleece Jacket          | $49.99 |
      | Sauce Labs Onesie                 | $7.99  |
      | Test.allTheThings() T-Shirt (Red) | $15.99 |
    And every product should have a name, description, price and image
    And the active sort option should be "Name (A to Z)"
    And the products should be sorted by "Name (A to Z)"

    Examples:
      | username                |
      | standard_user           |
      | problem_user            |
      | performance_glitch_user |
      | error_user              |
      | visual_user             |

  @known_issue @CheckItemsNames
  Scenario: Product names do not contain digits or special characters
    # "Test.allTheThings() T-Shirt (Red)" contains brackets, so this check is expected to fail
    Given the user is logged in as "standard_user"
    Then no product name should contain digits or special characters

  @sorting @TC_10 @TC_11 @TC_12 @TC_13
  Scenario Outline: "<username>" can sort the products by <sort option>
    Given the user is logged in as "<username>"
    When the user sorts the products by "<sort option>"
    Then the products should be sorted by "<sort option>"
    And the active sort option should be "<sort option>"

    Examples:
      | username                | sort option         |
      | standard_user           | Price (low to high) |
      | standard_user           | Price (high to low) |
      | standard_user           | Name (A to Z)       |
      | standard_user           | Name (Z to A)       |
      | problem_user            | Price (low to high) |
      | problem_user            | Price (high to low) |
      | problem_user            | Name (A to Z)       |
      | problem_user            | Name (Z to A)       |
      | performance_glitch_user | Price (low to high) |
      | performance_glitch_user | Price (high to low) |
      | performance_glitch_user | Name (A to Z)       |
      | performance_glitch_user | Name (Z to A)       |
      | error_user              | Price (low to high) |
      | error_user              | Price (high to low) |
      | error_user              | Name (A to Z)       |
      | error_user              | Name (Z to A)       |
      | visual_user             | Price (low to high) |
      | visual_user             | Price (high to low) |
      | visual_user             | Name (A to Z)       |
      | visual_user             | Name (Z to A)       |

  @sorting
  Scenario: The chosen sort order is kept after visiting a product
    Given the user is logged in as "standard_user"
    When the user sorts the products by "Price (high to low)"
    And the user opens the product "Sauce Labs Onesie"
    And the user goes back to the products page
    Then the products should be sorted by "Price (high to low)"

  @product_details
  Scenario Outline: The details page of "<product>" shows its name, description, price and image
    Given the user is logged in as "standard_user"
    When the user opens the product "<product>"
    Then the user should be on the product details page
    And the product details page should show "<product>" priced at "<price>"
    And the product "<product>" should show the "Add to cart" button
    When the user goes back to the products page
    Then the user should be on the products page

    Examples:
      | product                           | price  |
      | Sauce Labs Backpack               | $29.99 |
      | Sauce Labs Bike Light             | $9.99  |
      | Sauce Labs Bolt T-Shirt           | $15.99 |
      | Sauce Labs Fleece Jacket          | $49.99 |
      | Sauce Labs Onesie                 | $7.99  |
      | Test.allTheThings() T-Shirt (Red) | $15.99 |

  @product_details
  Scenario: A product can be opened by clicking its image
    Given the user is logged in as "standard_user"
    When the user opens the product "Sauce Labs Fleece Jacket" by its image
    Then the product details page should show "Sauce Labs Fleece Jacket" priced at "$49.99"
