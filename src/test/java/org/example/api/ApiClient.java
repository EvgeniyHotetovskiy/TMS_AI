package org.example.api;

import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;

import static io.restassured.RestAssured.given;

public class ApiClient {

    // Статическая инициализация для установки базового URI и фильтра Allure
    static {
        RestAssured.baseURI = "https://www.saucedemo.com";
        RestAssured.filters(new AllureRestAssured());
    }

    /**
     * Проверяет, что главная страница сайта возвращает статус код 200.
     * Это простой пример API-теста, который можно выполнить.
     */
    @Step("API: Проверить статус код главной страницы")
    public void checkMainPageStatusCode() {
        // given() - это начало описания запроса
        given()
        // when() - отправка запроса
        .when()
            .get("/")
        // then() - проверка ответа
        .then()
            .statusCode(200); // Ожидаем статус код 200 OK
    }
}
