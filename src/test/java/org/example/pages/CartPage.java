package org.example.pages;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.exist;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

public class CartPage extends BasePage {

    // Элементы страницы
    private final SelenideElement pageTitle = $(".title");
    private final SelenideElement checkoutButton = $("#checkout");

    // Метод для проверки, что страница корзины открыта
    @Step("Проверить, что страница корзины открыта")
    public CartPage verifyIsLoaded() {
        pageTitle.shouldHave(text("Your Cart"));
        return this;
    }

    // Метод для проверки наличия товара в корзине
    @Step("Проверить наличие товара '{productName}' в корзине")
    public CartPage verifyItemInCart(String productName) {
        $x("//div[text()='" + productName + "']").should(exist);
        return this;
    }

    // Метод для удаления товара из корзины
    @Step("Удалить товар '{productName}' из корзины")
    public CartPage removeItemFromCart(String productName) {
        $x("//div[text()='" + productName + "']/ancestor::div[@class='cart_item']//button").click();
        return this;
    }

    // Метод для перехода к оформлению заказа
    @Step("Перейти к оформлению заказа")
    public CheckoutStepOnePage goToCheckout() {
        checkoutButton.click();
        return new CheckoutStepOnePage();
    }
}
