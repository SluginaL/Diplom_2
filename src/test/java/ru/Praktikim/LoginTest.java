package ru.Praktikim;

import com.github.javafaker.Faker;
import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.core.IsEqual.equalTo;


public class LoginTest {

    private Faker faker;
    private String failLogin = "email or password are incorrect";
    private User user;
    private UserClient userClient;
    private String token;
    private Response response;

    @Before
    public void setUp() throws Exception {
        user = GenerateUser.generateUser();
        userClient = new UserClient();
        faker = new Faker();
        response = userClient.createUser(user);
        token = response.path("accessToken").toString();
    }

    @Test
    @Description("Логин под существующим пользователем")
    public void tryAuthorizedUserIfAlreadyExist() {
        user.setName(null);
        Response response = userClient.loginUser(user);
        response.then().assertThat().
                statusCode(200). // проверь статус ответа
                and().body("success", equalTo(true));
    }

    @Test
    @Description("Логин под существующим пользователем")
    public void tryAuthorizedUserWithWrongFields() {
        user.setEmail(faker.name().bloodGroup());
        user.setPassword(faker.name().prefix());
        user.setName(null);
        Response response = userClient.loginUser(user);
        response.then().assertThat().
                statusCode(401). // проверь статус ответа
                and().body("message", equalTo(failLogin));

    }

    @After
    @Description("Удаление пользователя")
    public void tearDown() throws Exception {
        userClient.deleteUser(token);
        response.then().assertThat().
                statusCode(200);
    }
}