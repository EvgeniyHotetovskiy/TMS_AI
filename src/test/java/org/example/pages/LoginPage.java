package org.example.pages;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class LoginPage extends BasePage {

    // Элементы страницы
    private final SelenideElement usernameInput = $("#user-name");
    private final SelenideElement passwordInput = $("#password");
    private final SelenideElement loginButton = $("#login-button");
    private final SelenideElement errorMessage = $("h3[data-test='error']");

    // Метод для ввода имени пользователя
    @Step("Ввести имя пользователя: {username}")
    public void setUsername(String username) {
        usernameInput.setValue(username);
    }

    // Метод для ввода пароля
    @Step("Ввести пароль")
    public void setPassword(String password) {
        passwordInput.setValue(password);
    }

    // Метод для нажатия кнопки входа
    @Step("Нажать кнопку 'Login'")
    public void clickLoginButton() {
        loginButton.click();
    }

    // Комплексный метод для выполнения входа
    @Step("Выполнить вход с именем пользователя '{username}'")
    public ProductsPage loginAs(String username, String password) {
        setUsername(username);
        setPassword(password);
        clickLoginButton();
        return new ProductsPage();
    }

    // Метод для получения текста ошибки
    @Step("Получить текст сообщения об ошибке")
    public String getErrorMessage() {
        return errorMessage.shouldBe(visible).getText();
    }
}
