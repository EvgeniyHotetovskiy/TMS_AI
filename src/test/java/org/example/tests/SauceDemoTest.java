package org.example.tests;

import io.qameta.allure.*;
import org.example.api.ApiClient;
import org.example.pages.*;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

@Epic("SauceDemo Application")
@Feature("E-commerce Functionality")
public class SauceDemoTest extends BaseTest {

    private LoginPage loginPage;
    private final String STANDARD_USER = "standard_user";
    private final String LOCKED_OUT_USER = "locked_out_user";
    private final String PASSWORD = "secret_sauce";

    // Предусловие: перед каждым тестом открывается главная страница
    @BeforeMethod
    public void openLoginPage() {
        loginPage = open("/", LoginPage.class);
    }

    /**
     * Этот тест проверяет базовый сценарий успешного входа и последующего выхода из системы.
     */
    @Test(description = "Successful Login and Logout")
    @Story("User Authentication")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Verify that a standard user can successfully log in and then log out.")
    public void testSuccessfulLoginAndLogout() {
        // Выполняем вход
        ProductsPage productsPage = loginPage.loginAs(STANDARD_USER, PASSWORD);
        // Проверяем, что вход выполнен успешно и мы на странице продуктов
        productsPage.verifyIsLoaded();
        // Выполняем выход
        productsPage.logout();
        // Проверяем, что мы вернулись на страницу входа
        Assert.assertTrue($("#login-button").isDisplayed(), "Должны вернуться на страницу входа");
    }

    /**
     * Этот тест проверяет полный сквозной сценарий: вход, добавление товара,
     * переход в корзину, оформление и завершение заказа.
     */
    @Test(description = "End-to-End E-commerce Scenario")
    @Story("Checkout Process")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify the complete user journey from login to placing an order.")
    public void testE2EScenario() {
        // Вход в систему
        ProductsPage productsPage = loginPage.loginAs(STANDARD_USER, PASSWORD);
        productsPage.verifyIsLoaded();

        // Добавление товара в корзину
        productsPage.addItemToCart("Sauce Labs Backpack");

        // Переход в корзину и проверка
        CartPage cartPage = productsPage.goToCart();
        cartPage.verifyIsLoaded();
        cartPage.verifyItemInCart("Sauce Labs Backpack");

        // Переход к оформлению заказа
        CheckoutStepOnePage checkoutStepOne = cartPage.goToCheckout();
        checkoutStepOne.verifyIsLoaded();

        // Заполнение данных пользователя
        CheckoutStepTwoPage checkoutStepTwo = checkoutStepOne.fillUserInfo("John", "Doe", "12345");
        checkoutStepTwo.verifyIsLoaded();

        // Завершение заказа
        CheckoutCompletePage completePage = checkoutStepTwo.finishCheckout();
        completePage.verifyIsLoaded();
        completePage.verifyThankYouMessage();
    }

    /**
     * Этот тест проверяет, что система корректно обрабатывает попытку входа
     * с неверными (в данном случае, заблокированными) учетными данными.
     */
    @Test(description = "Invalid Login Attempt")
    @Story("User Authentication")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that an appropriate error message is shown for a locked-out user.")
    public void testInvalidLogin() {
        // Попытка входа с заблокированным пользователем
        loginPage.loginAs(LOCKED_OUT_USER, PASSWORD);
        // Проверка сообщения об ошибке
        String expectedError = "Epic sadface: Sorry, this user has been locked out.";
        String actualError = loginPage.getErrorMessage();
        Assert.assertEquals(actualError, expectedError, "Сообщение об ошибке не соответствует ожидаемому");
    }

    /**
     * Этот тест проверяет валидацию полей на форме оформления заказа.
     */
    @Test(description = "Checkout Form Validation")
    @Story("Checkout Process")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that the checkout form shows an error when submitted with empty fields.")
    public void testCheckoutFormValidation() {
        // Вход и добавление товара
        ProductsPage productsPage = loginPage.loginAs(STANDARD_USER, PASSWORD);
        productsPage.addItemToCart("Sauce Labs Onesie");
        CartPage cartPage = productsPage.goToCart();

        // Переход на страницу оформления заказа
        CheckoutStepOnePage checkoutStepOne = cartPage.goToCheckout();
        checkoutStepOne.verifyIsLoaded();

        // Попытка продолжить с пустыми полями
        checkoutStepOne.clickContinue();

        // Проверка сообщения об ошибке
        String expectedError = "Error: First Name is required";
        String actualError = checkoutStepOne.getErrorMessage();
        Assert.assertEquals(actualError, expectedError, "Должна быть ошибка о необходимости заполнить имя");
    }

    /**
     * Этот тест проверяет функциональность добавления и последующего удаления товара из корзины.
     */
    @Test(description = "Add and Remove Item from Cart")
    @Story("Cart Management")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that an item can be added to the cart and subsequently removed from it.")
    public void testAddAndRemoveItemFromCart() {
        // Вход и добавление товара
        ProductsPage productsPage = loginPage.loginAs(STANDARD_USER, PASSWORD);
        productsPage.addItemToCart("Sauce Labs Bike Light");

        // Переход в корзину и проверка, что товар добавлен
        CartPage cartPage = productsPage.goToCart();
        cartPage.verifyIsLoaded();
        cartPage.verifyItemInCart("Sauce Labs Bike Light");

        // Удаление товара и проверка, что он исчез
        cartPage.removeItemFromCart("Sauce Labs Bike Light");
        Assert.assertFalse($(".cart_item").isDisplayed(), "Корзина должна быть пустой после удаления товара");
    }
    
    /**
     * Этот тест демонстрирует интеграцию с Rest Assured для простой API-проверки.
     */
    @Test(description = "API Test: Check Main Page Status")
    @Story("API Integration")
    @Severity(SeverityLevel.TRIVIAL)
    @Description("Verify that the main application page is accessible and returns a 200 OK status code.")
    public void testApiCheck() {
        ApiClient apiClient = new ApiClient();
        apiClient.checkMainPageStatusCode();
    }
}
