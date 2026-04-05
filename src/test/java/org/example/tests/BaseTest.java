package org.example.tests;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeSuite;

public class BaseTest {

    // Метод, выполняемый перед запуском всех тестов в сьюте
    @BeforeSuite
    public void setup() {
        // Отключаем автоматическое управление драйверами в Selenide
//        Configuration.webdriverManagerEnabled = false;
        // Указываем путь к локальному файлу chromedriver.exe, который был распакован
        System.setProperty("webdriver.chrome.driver", "drivers/chromedriver-win64/chromedriver.exe");

        // Установка базового URL
        Configuration.baseUrl = "https://www.saucedemo.com";
        // Можно указать браузер, по умолчанию - Chrome
        Configuration.browser = "chrome";
        // Можно настроить размер окна браузера
        Configuration.browserSize = "1920x1080";
        // Подключение Allure-листера для Selenide
        SelenideLogger.addListener("AllureSelenide", new AllureSelenide().screenshots(true).savePageSource(false));
    }

    // Метод, выполняемый после каждого тестового метода
    @AfterMethod
    public void tearDown() {
        // Закрытие браузера после каждого теста
        Selenide.closeWebDriver();
    }
}
