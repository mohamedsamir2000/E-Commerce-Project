# E-Commerce Project – Cucumber BDD Test Automation

UI tests for [Swag Labs](https://www.saucedemo.com/) written with **Cucumber (Java)**, **Selenium WebDriver** and **TestNG**, following the **Page Object Model**.

## Project structure

```
src/test
├── java
│   ├── Pages/            # Page Objects – locators + page actions only (no assertions)
│   │   ├── BasePage.java
│   │   ├── LoginPage.java
│   │   ├── HomePage.java          # inventory, header cart, side menu, footer links
│   │   ├── ProductDetailsPage.java
│   │   ├── CartPage.java
│   │   └── CheckoutPage.java
│   ├── StepDefinitions/  # Glue code – calls page objects and asserts
│   ├── Context/          # TestContext shared between step classes (PicoContainer)
│   ├── Hooks/            # @Before / @After – browser setup, screenshot on failure
│   ├── Runners/          # TestRunner (Cucumber + TestNG)
│   └── Utility/          # DriverFactory
└── resources/features/   # Gherkin feature files
    ├── Login_and_Logout.feature
    ├── Sort.feature
    ├── Cart.feature
    ├── Checkout.feature
    ├── Shopping_Items.feature
    └── Menu_and_Icons.feature
```

Test data that used to live in the Excel sheets (`LoginData.xlsx`, `LoginTestCases.xlsx`) is now in the `Examples:` tables of each `Scenario Outline`.

## Running the tests

```bash
mvn test                                       # all scenarios (via testng.xml)
mvn test -Dheadless=true                       # headless Chrome
mvn test -Dcucumber.filter.tags="@Checkout"    # only one feature / tag
mvn test -Dcucumber.filter.tags="@TC_10 or @TC_11"
```

## Reports

- Cucumber HTML: `target/cucumber-reports/cucumber.html`
- Cucumber JSON: `target/cucumber-reports/cucumber.json`
- Allure: `allure serve target/allure-results`
