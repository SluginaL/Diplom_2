package ru.Praktikim;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class UserClient extends Config {
    @Step("Send POST request to /api/auth/register")
    public Response createUser(User user) {
        return given()
                .spec(getBaseSpec())
                .body(user)
                .post(EndPoints.USER + "register");

    }

    @Step("Send POST request to /api/auth/login")
    public Response loginUser(User user) {
        return given()
                .spec(getBaseSpec())
                .body(user)
                .post(EndPoints.USER + "login");

    }

    @Step("Send DELETE request to /api/auth/user")
    public Response deleteUser(String accessToken) {
        return given()
                .spec(getBaseSpec())
                .auth().oauth2(accessToken)
                .delete(EndPoints.USER + "user");

    }

    @Step("Send PATCH request to /api/auth/user")
    public Response updateUserByAuthorization(User user, String accessToken) {
        return given()
                .spec(getBaseSpec())
                .header("Authorization", accessToken)
                .body(user)
                .patch(EndPoints.USER + "user");

    }

    @Step("Send PATCH request to /api/auth/user")
    public Response updateUserWithoutAuthorization(User user) {
        return given()
                .spec(getBaseSpec())
                .body(user)
                .patch(EndPoints.USER + "user");

    }
}