# E-Commerce Project – Cucumber BDD Test Automation

End-to-end UI tests for [Swag Labs](https://www.saucedemo.com/) written with **Cucumber (Java)**, **Selenium WebDriver** and **TestNG**, following the **Page Object Model**.

## End-to-end journeys

`features/journeys/` holds the end-to-end suite: every scenario starts at login and walks a whole
session (browse → cart → checkout → order → logout), checking each page on the way.

| Feature file | Journeys |
|---|---|
| `01_Purchase_Journeys.feature` | Full purchase for standard/performance_glitch users, sort-then-buy for every sort option, buy each product from its details page, open by image then buy the whole catalog |
| `02_Cart_Journeys.feature` | Change the cart on the products, details and cart pages; cancel at the last step and adjust; cart kept after refresh and re-login; Reset App State then shop again |
| `03_Checkout_Journeys.feature` | Fix each missing checkout field then order; cancel on each checkout step then order; order totals |
| `04_Access_Journeys.feature` | Pages blocked before login, every login error, shop, blocked again after logout; locked-out user |
| `05_Navigation_Journeys.feature` | Side menu from every page while shopping, footer social links in new tabs while shopping, About after an order |
| `06_Known_Site_Bugs.feature` | `@known_issue` – journeys for problem_user, error_user and visual_user that hit the site's intentional bugs (expected to fail, skipped by default) |

```bash
mvn test -Dcucumber.filter.tags="@journey and not @known_issue"   # end-to-end suite (green)
mvn test -Dcucumber.filter.tags="@known_issue"                    # show the site's intentional bugs
```

The runner skips `@known_issue` by default; passing `-Dcucumber.filter.tags` replaces that filter.

## Coverage

| Feature file | What it covers |
|---|---|
| `01_Login_and_Logout.feature` | Login for every user, all login error messages, dismissing errors, pages blocked without login, logout |
| `02_Products.feature` | Catalog names/prices/descriptions/images, all 4 sort options for every user, sort kept after navigation, product details (by name and by image) |
| `03_Cart.feature` | Add/remove from the products page, the details page and the cart page, cart badge, cart contents, continue shopping, cart kept after refresh and re-login, Reset App State |
| `04_Checkout.feature` | Full checkout for every user, order summary (items, payment, shipping, item total, 8% tax, total), required-field errors, cancel on each step, Back Home |
| `05_Menu_and_Footer.feature` | Side menu options/open/close, All Items from every page, About, footer social links (new tab) and copyright |
| `06_End_to_End.feature` | Complete journeys: login → sort → add from list and details → cart → checkout → confirm → back home → logout; changing your mind; buying each product; buying everything; returning customer; reset and shop again |

## Project structure

```
src/test
├── java
│   ├── Pages/                 # Page Objects – locators + actions only (no assertions)
│   │   ├── BasePage.java              # shared helpers, page title, error banner
│   │   ├── AppPage.java               # page names used in steps ("the cart page") -> URL + title
│   │   ├── LoginPage.java
│   │   ├── InventoryPage.java         # products page
│   │   ├── ProductDetailsPage.java
│   │   ├── CartPage.java
│   │   ├── CheckoutInformationPage.java
│   │   ├── CheckoutOverviewPage.java
│   │   ├── CheckoutCompletePage.java
│   │   └── Components/                # Header (cart icon), SideMenu, Footer
│   ├── StepDefinitions/       # Reusable, parameterized steps (assertions live here)
│   │   ├── ParameterTypes.java        # {page}, {sortOption}, Product & CheckoutInfo table types
│   │   ├── NavigationSteps.java
│   │   ├── LoginSteps.java
│   │   ├── ProductSteps.java
│   │   ├── CartSteps.java
│   │   ├── CheckoutSteps.java
│   │   └── MenuAndFooterSteps.java
│   ├── Models/                # Product, CartLine, CheckoutInfo, SortOption, ExpectedCart
│   ├── Context/               # TestContext shared by all steps of a scenario (PicoContainer)
│   ├── Hooks/                 # browser setup/teardown, screenshot on failure
│   ├── Runners/               # TestRunner (Cucumber + TestNG)
│   └── Utility/               # DriverFactory, ConfigReader
└── resources
    ├── config.properties      # base URL, default password, headless, timeout
    └── features/              # Gherkin feature files
```

## Writing new scenarios with the reusable steps

Steps are generic and parameterized, so most new scenarios need no new Java code:

```gherkin
Given the user is logged in as "standard_user"
When the user sorts the products by "Price (low to high)"
And the user adds "Sauce Labs Backpack" to the cart          # works on the products or details page
And the user opens the product "Sauce Labs Onesie"
And the user adds "Sauce Labs Onesie" to the cart
And the user checks out with first name "A", last name "B" and postal code "123"
Then the user should be on the checkout overview page        # any page: login, products, product details, cart, ...
And the checkout overview should list the selected products  # compares with everything added/removed so far
And the tax should be 8% of the item total
```

The scenario remembers every product added or removed (`ExpectedCart`), so steps like
`the cart should contain the selected products` and `the item total should match the selected products`
check the cart and the order against what the scenario actually did.

## Running the tests

```bash
mvn test                                         # all scenarios (via testng.xml)
mvn test -Dheadless=true                         # headless Chrome
mvn test -Dcucumber.filter.tags="@e2e"           # end-to-end journeys only
mvn test -Dcucumber.filter.tags="@smoke"         # quick smoke run
mvn test -Dcucumber.filter.tags="not @known_issue"

# use a specific Chrome + matching ChromeDriver (no Selenium Manager download)
mvn test -Dchrome.binary=/path/to/chrome -Dwebdriver.chrome.driver=/path/to/chromedriver
```

Chrome and ChromeDriver must have the same major version. Any key in `config.properties` can be overridden with `-Dkey=value`.

Tags: `@smoke`, `@e2e`, `@authentication`, `@products`, `@sorting`, `@product_details`, `@cart`, `@persistence`, `@checkout`, `@navigation`, `@menu`, `@footer`, `@negative`, `@security`, `@known_issue`, plus the original test case IDs (`@TC_10` … `@TC_26`, `@TestLogin`, …).

Scenarios run for every user in the `Examples:` tables. `problem_user`, `error_user` and `visual_user` have deliberate bugs on the site, so some of their scenarios are expected to fail.

## Reports

- Cucumber HTML: `target/cucumber-reports/cucumber.html`
- Cucumber JSON: `target/cucumber-reports/cucumber.json`
- Allure: `allure serve target/allure-results`
