package ru.Praktikim;

import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.core.IsEqual.equalTo;

public class CreateUserTest {
    private String message = "User already exists";
    private String failMessage = "Email, password and name are required fields";
    private User user;
    private UserClient userClient;
    private String token;
    private Response response;

    @Before
    public void setUp() throws Exception {
        user = GenerateUser.generateUser();
        userClient = new UserClient();
        response = userClient.createUser(user);
        token = response.path("accessToken").toString();

    }

    @Test
    @Description("Создание уникального пользователя")
    public void createUniqueUser() {
        response.then().assertThat().
                statusCode(200). // проверь статус ответа
                and().body("success", equalTo(true));
    }

    @Test
    @Description("Создание пользователя, который уже зарегистрирован")
    public void checkUserAlreadyExist() {
        Response response = userClient.createUser(user);
        response.then().assertThat().
                statusCode(403). // проверь статус ответа
                and().body("message", equalTo(message));
    }

    @Test
    @Description("Cоздание пользователя и не заполнить одно из обязательных полей")
    public void createUserWithoutOneField() {
        user.setEmail(null);
        Response response = userClient.createUser(user);
        response.then().assertThat().
                statusCode(403). // проверь статус ответа
                and().body("message", equalTo(failMessage));
    }

    @After
    @Description("Удаление пользователя")
    public void tearDown() throws Exception {
        userClient.deleteUser(token);
        response.then().assertThat().
                statusCode(200);
    }
}