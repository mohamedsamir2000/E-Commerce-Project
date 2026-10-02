@Sort
Feature: Sort products
  As a logged in customer
  I want to sort the products on the home page
  So that I can find items quickly

  @TC_10
  Scenario Outline: TC_10 Verify items can be sorted by price low to high for "<username>"
    Given the user is logged in as "<username>" with password "<password>"
    When the user sorts the items by "Price (low to high)"
    Then the items should be sorted by price from low to high

    Examples:
      | username                | password     |
      | standard_user           | secret_sauce |
      | problem_user            | secret_sauce |
      | performance_glitch_user | secret_sauce |
      | error_user              | secret_sauce |
      | visual_user             | secret_sauce |

  @TC_11
  Scenario Outline: TC_11 Verify items can be sorted by price high to low for "<username>"
    Given the user is logged in as "<username>" with password "<password>"
    When the user sorts the items by "Price (high to low)"
    Then the items should be sorted by price from high to low

    Examples:
      | username                | password     |
      | standard_user           | secret_sauce |
      | problem_user            | secret_sauce |
      | performance_glitch_user | secret_sauce |
      | error_user              | secret_sauce |
      | visual_user             | secret_sauce |

  @TC_12
  Scenario Outline: TC_12 Verify items can be sorted alphabetically A to Z for "<username>"
    Given the user is logged in as "<username>" with password "<password>"
    When the user sorts the items by "Name (A to Z)"
    Then the items should be sorted by name from A to Z

    Examples:
      | username                | password     |
      | standard_user           | secret_sauce |
      | problem_user            | secret_sauce |
      | performance_glitch_user | secret_sauce |
      | error_user              | secret_sauce |
      | visual_user             | secret_sauce |

  @TC_13
  Scenario Outline: TC_13 Verify items can be sorted alphabetically Z to A for "<username>"
    Given the user is logged in as "<username>" with password "<password>"
    When the user sorts the items by "Name (Z to A)"
    Then the items should be sorted by name from Z to A

    Examples:
      | username                | password     |
      | standard_user           | secret_sauce |
      | problem_user            | secret_sauce |
      | performance_glitch_user | secret_sauce |
      | error_user              | secret_sauce |
      | visual_user             | secret_sauce |
