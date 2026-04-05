package org.example.pages;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class CheckoutStepOnePage extends BasePage {

    // Элементы страницы
    private final SelenideElement pageTitle = $(".title");
    private final SelenideElement firstNameInput = $("#first-name");
    private final SelenideElement lastNameInput = $("#last-name");
    private final SelenideElement postalCodeInput = $("#postal-code");
    private final SelenideElement continueButton = $("#continue");
    private final SelenideElement errorMessage = $("h3[data-test='error']");

    // Метод для проверки, что страница открыта
    @Step("Проверить, что страница 'Checkout: Your Information' открыта")
    public CheckoutStepOnePage verifyIsLoaded() {
        pageTitle.shouldHave(text("Checkout: Your Information"));
        return this;
    }

    // Метод для ввода имени
    @Step("Ввести имя: {firstName}")
    public CheckoutStepOnePage setFirstName(String firstName) {
        firstNameInput.setValue(firstName);
        return this;
    }

    // Метод для ввода фамилии
    @Step("Ввести фамилию: {lastName}")
    public CheckoutStepOnePage setLastName(String lastName) {
        lastNameInput.setValue(lastName);
        return this;
    }

    // Метод для ввода почтового индекса
    @Step("Ввести почтовый индекс: {postalCode}")
    public CheckoutStepOnePage setPostalCode(String postalCode) {
        postalCodeInput.setValue(postalCode);
        return this;
    }

    // Метод для нажатия кнопки 'Continue'
    @Step("Нажать кнопку 'Continue'")
    public void clickContinue() {
        continueButton.click();
    }
    
    // Комплексный метод для заполнения формы
    @Step("Заполнить информацию о пользователе")
    public CheckoutStepTwoPage fillUserInfo(String firstName, String lastName, String postalCode) {
        setFirstName(firstName);
        setLastName(lastName);
        setPostalCode(postalCode);
        clickContinue();
        return new CheckoutStepTwoPage();
    }
    
    // Метод для получения текста ошибки
    @Step("Получить текст сообщения об ошибке")
    public String getErrorMessage() {
        return errorMessage.shouldBe(visible).getText();
    }
}
