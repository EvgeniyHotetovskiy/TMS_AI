package org.example.pages;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

public class CheckoutStepTwoPage extends BasePage {

    // Элементы страницы
    private final SelenideElement pageTitle = $(".title");
    private final SelenideElement finishButton = $("#finish");

    // Метод для проверки, что страница открыта
    @Step("Проверить, что страница 'Checkout: Overview' открыта")
    public CheckoutStepTwoPage verifyIsLoaded() {
        pageTitle.shouldHave(text("Checkout: Overview"));
        return this;
    }

    // Метод для завершения заказа
    @Step("Завершить оформление заказа")
    public CheckoutCompletePage finishCheckout() {
        finishButton.click();
        return new CheckoutCompletePage();
    }
}
