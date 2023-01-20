package ru.Praktikim;

import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.core.IsEqual.equalTo;

public class CreateOrdersTest {

    private String emptyIngredients = "Ingredient ids must be provided";
    private String wrongHash = "One or more ids provided are incorrect";

    private User user;
    private UserClient userClient;
    private OrderClient orderClient;
    private Order order;

    private Response response;
    private String hash;
    private String accessToken;

    @Before
    public void setUp() throws Exception {
        orderClient = new OrderClient();
        order = new Order();
        userClient = new UserClient();
        getIngredients();
        createUserForOrders();
    }

    public void getIngredients() {
        Response response = orderClient.getAllIngredients();
        ArrayList<String> ingredients = response.path("data._id");
        hash = ingredients.get(0);
    }

    public void createUserForOrders() {
        user = GenerateUser.generateUser();
        userClient = new UserClient();
        response = userClient.createUser(user);
        accessToken = response.path("accessToken").toString();
    }

    @Test
    @Description("Создание заказа: без индгредиентов и без авторизации")
    public void createOrderWithoutIngredients() {
        Response response = orderClient.createOrderWithoutAuthorization(order);
        response.then().assertThat().
                statusCode(400) // проверь статус ответа
                .and().body("message", equalTo(emptyIngredients));

    }

    @Test
    @Description("Создание заказа: c индгредиентом и без авторизации")
    public void createOrderWithoutAuthorization() {
        order.setIngredients(Collections.singletonList(hash));
        Response response = orderClient.createOrderWithoutAuthorization(order);
        response.then().assertThat().
                statusCode(200)
                .and().body("success", equalTo(true))
                .and().body("order", notNullValue());
    }

    @Test
    @Description("Создание заказа: c неверным хешем интредиентов и без авторизации")
    public void createOrderWithWrongHash() {
        order.setIngredients(Collections.singletonList(null));
        Response response = orderClient.createOrderWithoutAuthorization(order);
        response.then().assertThat().
                statusCode(400)
                .and().body("success", equalTo(false))
                .and().body("message", equalTo(wrongHash));
    }

    @Test
    @Description("Создание заказа: с авторизации и ингредиентом")
    public void createOrderWithAuthorizationUserAndWithIngredients() {
        order.setIngredients(Collections.singletonList(hash));
        Response response = orderClient.createOrderByAuthorization(order, accessToken);
        response.then().assertThat().
                statusCode(200)
                .and().body("success", equalTo(true))
                .and().body("order", notNullValue());

    }

    @Test
    @Description("Создание заказа: с авторизации и без ингредиента")
    public void createOrderWithoutAuthorizationUser() {
        order.setIngredients(Collections.singletonList(null));
        Response response = orderClient.createOrderByAuthorization(order, accessToken);
        response.then().assertThat().
                statusCode(400)
                .and().body("success", equalTo(false))
                .and().body("message", equalTo(wrongHash));

    }

    @After
    @Description("Удаление пользователя")
    public void tearDown() throws Exception {
        userClient.deleteUser(accessToken);
        response.then().assertThat().
                statusCode(200);
    }
}