package org.example.pages;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

public class ProductsPage extends BasePage {

    // Элементы страницы
    private final SelenideElement pageTitle = $(".title");
    private final SelenideElement cartIcon = $(".shopping_cart_link");
    private final SelenideElement burgerMenuIcon = $("#react-burger-menu-btn");
    private final SelenideElement logoutLink = $("#logout_sidebar_link");

    // Метод для проверки, что страница продуктов загрузилась
    @Step("Проверить, что страница продуктов открыта")
    public ProductsPage verifyIsLoaded() {
        pageTitle.shouldHave(text("Products"));
        return this;
    }

    // Метод для добавления товара в корзину по имени
    @Step("Добавить товар '{productName}' в корзину")
    public ProductsPage addItemToCart(String productName) {
        $x("//div[text()='" + productName + "']/ancestor::div[@class='inventory_item']//button").click();
        return this;
    }

    // Метод для перехода в корзину
    @Step("Перейти в корзину")
    public CartPage goToCart() {
        cartIcon.click();
        return new CartPage();
    }
    
    // Метод для открытия бургер-меню
    @Step("Открыть бургер-меню")
    public void openBurgerMenu() {
        burgerMenuIcon.click();
    }

    // Метод для выполнения выхода из системы
    @Step("Выйти из системы")
    public LoginPage logout() {
        openBurgerMenu();
        logoutLink.click();
        return new LoginPage();
    }
}
