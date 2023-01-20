package ru.Praktikim;

import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.core.IsEqual.equalTo;

public class ChangeDataUserTest {
    private String failMessage = "You should be authorised";
    private User user;
    private UserClient userClient;
    private String token;
    private Response response;

    @Before
    public void setUp() throws Exception {
        createUserForOrders();
    }

    public void createUserForOrders() {
        user = GenerateUser.generateUser();
        userClient = new UserClient();
        response = userClient.createUser(user);
        token = response.path("accessToken").toString();
    }

    @Test
    @Description("Изменение данных пользователя:с авторизацией")
    public void updateUserWithAuthorization() {
        response = userClient.updateUserByAuthorization(GenerateUser.generateUser(), token);
        response.then().assertThat().
                statusCode(200). // проверь статус ответа
                and().body("success", equalTo(true));
    }

    @Test
    @Description("Изменение данных пользователя:без авторизацией")
    public void updateUserWithoutAuthorization() {
        Response response = userClient.updateUserWithoutAuthorization(GenerateUser.generateUser());
        response.then().assertThat().
                statusCode(401). // проверь статус ответа
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