package org.example.pages;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

public class CheckoutCompletePage extends BasePage {

    // Элементы страницы
    private final SelenideElement pageTitle = $(".title");
    private final SelenideElement completeHeader = $(".complete-header");

    // Метод для проверки, что страница открыта
    @Step("Проверить, что страница 'Checkout: Complete!' открыта")
    public CheckoutCompletePage verifyIsLoaded() {
        pageTitle.shouldHave(text("Checkout: Complete!"));
        return this;
    }

    // Метод для проверки текста благодарности за заказ
    @Step("Проверить наличие сообщения 'Thank you for your order!'")
    public CheckoutCompletePage verifyThankYouMessage() {
        completeHeader.shouldHave(text("Thank you for your order!"));
        return this;
    }
}
