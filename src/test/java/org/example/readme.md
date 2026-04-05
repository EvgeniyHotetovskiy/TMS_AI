# UI and API Autotests for SauceDemo

This project contains automated tests for the web application [https://www.saucedemo.com/](https://www.saucedemo.com/).

## Project Structure

- `src/test/java/org/example/api`: API client layer.
- `src/test/java/org/example/pages`: Page Object layer for UI tests.
- `src/test/java/org/example/tests`: Test classes.
- `pom.xml`: Maven project configuration with all dependencies.

## Technologies Used

- **Java 11**: Programming language.
- **Selenide**: For UI browser automation.
- **TestNG**: Testing framework.
- **Rest Assured**: For API testing.
- **Allure**: For test reports.
- **Maven**: Build automation tool.

## How to Run Tests

1.  Ensure you have `Java 11` and `Maven` installed and configured.
2.  Navigate to the project root directory.
3.  Run the tests from the command line:
    ```bash
    mvn clean test
    ```

## How to Generate and View Allure Report

1.  After the tests have finished, generate the report:
    ```bash
    mvn allure:serve
    ```
2.  The report will automatically open in your default browser.

## Test Logic Explanation

- **`BaseTest.java`**: Configures the browser and base URL before tests and closes the browser after tests.
- **`SauceDemoTest.java`**: Contains 5 test cases:
    1.  **`testSuccessfulLoginAndLogout`**: Checks the full login and logout cycle.
    2.  **`testE2EScenario`**: A full end-to-end test from login to successful checkout.
    3.  **`testInvalidLogin`**: Verifies that an error message is displayed for incorrect credentials.
    4.  **`testCheckoutFormValidation`**: Checks that the checkout form displays errors if fields are empty.
    5.  **`testAddAndRemoveItemFromCart`**: Verifies that an item can be added to and removed from the cart.
- **Page Objects**: Each page of the application has its own class in the `pages` directory, which describes the elements and methods for interacting with them.
- **API Test**: A simple test is included to check that the main page returns a 200 OK status code, demonstrating the integration of Rest Assured.

## Regression and Boundary Values

- **For Regression**: All tests in `SauceDemoTest.java` should be included in the regression suite as they cover critical application functionality.
- **Boundary Values**:
    - **Login**: Test with empty username/password; test with non-existent users. The `testInvalidLogin` is an example.
    - **Checkout Form**: Test each field (`firstName`, `lastName`, `postalCode`) with empty values, special characters, long strings. The `testCheckoutFormValidation` is an example.
    - **Cart**: Test adding the maximum number of items, test adding/removing items in different sequences.
