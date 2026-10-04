# E-Commerce Project – Cucumber BDD Test Automation

End-to-end UI tests for [Swag Labs](https://www.saucedemo.com/) written with **Cucumber (Java)**, **Selenium WebDriver**
and **TestNG**, following the **Page Object Model**. Every scenario is a whole journey: login → browse → cart →
checkout → order → logout, checking each screen on the way.

## Project structure

```
src
├── main
│   ├── java/com/core
│   │   ├── TestEnvConfig.java          # settings + environment (sit/uat): URL, headless, timeouts, video...
│   │   └── utils
│   │       ├── DriverFactory.java      # creates/quits Chrome (one driver per thread)
│   │       ├── TestDataReader.java     # reads testData/<env>/*.json, saves/reads "File//Key" values
│   │       └── ScreenRecorder.java     # screen recording of every scenario (Cucumber plugin)
│   └── resources
│       ├── config.properties           # default settings (any key: -Dkey=value)
│       ├── environments.properties     # base URL of each environment
│       └── log4j2.xml                  # logging -> logs/automation_<date>_<time>.log
└── test
    ├── java/com/core
    │   ├── features                    # Gherkin feature files (the test journeys)
    │   ├── pages/SwagLabs              # Page Objects: locators + actions, no assertions
    │   │   ├── components              # Header, SideMenu, Footer (shown on every screen)
    │   │   └── models                  # Product, CartLine, SortOption, ExpectedCart
    │   ├── runners
    │   │   └── MyRunnerTest.java       # Cucumber + TestNG runner: features, glue, tags, reports
    │   └── stepdef                     # step definitions (assertions live here) + Hooks
    │       ├── LoginStepDef.java           # login/logout, error messages
    │       ├── ReusableStepDef.java        # open/verify screens, refresh, URL
    │       ├── ProductsStepDef.java        # catalog, sorting, add/remove, product details
    │       ├── CartStepDef.java            # cart screen and badge
    │       ├── CheckoutStepDef.java        # checkout fields, overview, totals, confirmation
    │       ├── MenuAndFooterStepDef.java   # side menu, header, footer links
    │       ├── ParameterTypes.java         # table -> object conversion
    │       ├── TestContext.java            # page objects + expected cart shared by a scenario's steps
    │       └── Hooks.java                  # browser, screenshot, screen recording, logging per scenario
    └── resources
        ├── testData
        │   ├── sit                     # Users.json (roles -> credentials), StoreData.json (expected values)
        │   └── uat
        └── xmlSuites
            └── testng.xml              # suite used by mvn test
logs/                                   # execution logs (one file per run)
target/cucumber/                        # report.html, results/cucumber.json, result.xml
target/videos/                          # screen recordings
target/testData/<env>/                  # values saved during the run ("Save ... in StoreData//Key")
```

## Feature files

| Feature file | Journeys |
|---|---|
| `PurchaseJourneys.feature` | Full purchase for StandardUser/PerformanceGlitchUser, sort-then-buy for every sort option, buy each product from its details screen, open by image then buy the whole catalog |
| `CartJourneys.feature` | Change the cart on every screen; cancel at the last step and adjust; cart kept after refresh and re-login; Reset App State then shop again |
| `CheckoutJourneys.feature` | Fix each missing checkout field then order; cancel on each checkout step then order; order summary (item total, tax, total) |
| `AccessJourneys.feature` | Screens blocked before login, every login error, shop, blocked again after logout; locked-out user |
| `NavigationJourneys.feature` | Side menu from every screen while shopping, footer social links in new tabs, About after an order |
| `KnownSiteBugs.feature` | `@known_issue` – journeys for ProblemUser, ErrorUser and VisualUser that hit the site's intentional bugs (expected to fail, skipped by default) |

### Gherkin style

```gherkin
Scenario Outline: Full Purchase From Login To Logout
  Given Customer Login as a "<User>"                       # role from testData/<env>/Users.json
  And Add product "Sauce Labs Backpack" to cart
  And Open cart
  And Click on checkout
  And I fill the following fields:
    | label       | value       |
    | First Name  | <FirstName> |
    | Last Name   | <LastName>  |
    | Postal Code | 12345       |
  And Click on continue
  And Save the order total in "StoreData//OrderTotal"     # keep a value for later steps
  Then Verify checkout overview:
    | Payment Information | StoreData//PaymentInformation |   # value from testData/<env>/StoreData.json
    | Total               | StoreData//OrderTotal         |   # or saved earlier in the run
  And Click on finish
  Then Verify order confirmation "StoreData//ConfirmationHeader"
  And Customer Logout

  Examples:
    | User         | FirstName | LastName |
    | StandardUser | Marwa     | Ashraf   |
```

- A `"File//Key"` value is read from `testData/<env>/File.json`, or from a value saved earlier in the run with
  `Save ... in "File//Key"`.
- The scenario remembers every product added or removed, so `Verify cart contains the selected products`,
  `Verify checkout overview lists the selected products` and `Verify order totals with "8%" tax` check the cart
  and the order against what the scenario actually did.
- Screen names for `Customer Open screen` / `Verify screen ... is displayed`: Login, Products, Product Details,
  Cart, Checkout Information, Checkout Overview, Checkout Complete.

## Running the tests

```bash
mvn test                                            # all journeys (src/test/resources/xmlSuites/testng.xml)
mvn test -Dheadless=true                            # headless Chrome
mvn test -Denv=uat                                  # another environment (URL + testData/uat)
mvn test -Dcucumber.filter.tags="@Smoke"            # by tag: @Purchase @Cart @Checkout @Authentication @Navigation ...
mvn test -Dcucumber.filter.tags="@known_issue"      # show the site's intentional bugs

# use a specific Chrome + matching ChromeDriver (no Selenium Manager download)
mvn test -Dchrome.binary=/path/to/chrome -Dwebdriver.chrome.driver=/path/to/chromedriver
```

The runner skips `@known_issue` by default; passing `-Dcucumber.filter.tags` replaces that filter.

## Screen recordings and screenshots

Every scenario is screen recorded: while it runs, `ScreenRecorder` streams the browser tab as a live video
(Chrome DevTools screencast). When the scenario ends, `Hooks` writes it as an MP4 that plays at the real speed of
the test, with the step that was running shown in a caption bar (green = passed, red = failed). The video is
attached to the Cucumber HTML report and the Allure report next to the screenshot, and saved in `target/videos/`.

Works in headless mode and on any OS with any Chrome version (it records the browser tab, not the desktop),
no ffmpeg or screen-recording permission needed.

```bash
mvn test -Dvideo=failed        # keep videos of failed scenarios only (default: all)
mvn test -Dvideo=off           # no recording
mvn test -Dscreenshot=all      # final screenshot for every scenario (default: failed only)
```

## Reports and logs

- Cucumber HTML: `target/cucumber/report.html`
- Cucumber JSON / JUnit XML: `target/cucumber/results/cucumber.json`, `target/cucumber/result.xml`
- Allure: `allure serve target/allure-results`
- Execution log: `logs/automation_<date>_<time>.log`
