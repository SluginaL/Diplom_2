package ru.Praktikim;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class OrderClient extends Config {
    @Step("Send GET request to /api/ingredients")
    public Response getAllIngredients() {
        return given()
                .spec(getBaseSpec())
                .and()
                .get(EndPoints.INGREDIENTS);

    }

    @Step("Send GET request to /api/orders")
    public Response getOrdersByAuthorization(String accessToken) {
        return given()
                .spec(getBaseSpec())
                .header("Authorization", accessToken)
                .get(EndPoints.ORDER);

    }

    @Step("Send GET request to /api/orders")
    public Response getOrdersWithoutAuthorization() {
        return given()
                .spec(getBaseSpec())
                .get(EndPoints.ORDER);
    }

    @Step("Send POST request to /api/orders")
    public Response createOrderByAuthorization(Order order, String accessToken) {
        return given()
                .spec(getBaseSpec())
                .header("Authorization", accessToken)
                .body(order)
                .post(EndPoints.ORDER);

    }

    @Step("Send POST request to /api/orders")
    public Response createOrderWithoutAuthorization(Order order) {
        return given()
                .spec(getBaseSpec())
                .body(order)
                .post(EndPoints.ORDER);

    }
}