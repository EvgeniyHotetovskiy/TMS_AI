package org.example.tests;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeSuite;

import java.util.HashMap;
import java.util.Map;

public class BaseTest {

    // Метод, выполняемый перед запуском всех тестов в сьюте
    @BeforeSuite
    public void setup() {
        // --- Настройка для отключения всплывающих окон о паролях (усиленная версия) ---
        ChromeOptions options = new ChromeOptions();

        // 1. Добавляем аргументы командной строки для полного отключения UI паролей
        options.addArguments("--disable-save-password-bubble");
        options.addArguments("--disable-features=PasswordManager,PasswordManagerUI");

        // 2. Устанавливаем экспериментальные флаги (prefs), чтобы отключить сервис
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("credentials_enable_service", false);
        prefs.put("profile.password_manager_enabled", false);
        options.setExperimentalOption("prefs", prefs);

        // Применяем все эти опции к сессии браузера
        Configuration.browserCapabilities = options;
        // -----------------------------------------------------------

        // Указываем путь к локальному файлу chromedriver.exe.
        // Этого достаточно, чтобы Selenide не скачивал драйвер автоматически.
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
