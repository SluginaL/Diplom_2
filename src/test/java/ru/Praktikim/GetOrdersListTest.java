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

public class GetOrdersListTest {
    private String failMessage = "You should be authorised";
    private User user;
    private UserClient userClient;
    private OrderClient orderClient;
    private Order order;
    private String accessToken;
    private Response response;

    @Before
    public void setUp() throws Exception {
        orderClient = new OrderClient();
        order = new Order();
        createUserForOrders();
        createOrderForNewUser();

    }

    public void createUserForOrders() {
        user = GenerateUser.generateUser();
        userClient = new UserClient();
        response = userClient.createUser(user);
        accessToken = response.path("accessToken").toString();
    }

    public void createOrderForNewUser() {
        Response response = orderClient.getAllIngredients();
        ArrayList<String> ingredients = response.path("data._id");
        String hash = ingredients.get(1);
        order.setIngredients(Collections.singletonList(hash));
        orderClient.createOrderByAuthorization(order, accessToken);
    }

    @Test
    @Description("Получение заказов конкретного пользователя:авторизованный пользователь")
    public void getOrdersWithAuthorizedUser() {
        Response response = orderClient.getOrdersByAuthorization(accessToken);
        response.then().assertThat().
                statusCode(200)
                .and().body("success", equalTo(true))
                .and().body("orders", notNullValue());


    }

    @Test
    @Description("Получение заказов конкретного пользователя:неавторизованный пользователь")
    public void getOrdersWithoutAuthorizedUser() {
        Response response = orderClient.getOrdersWithoutAuthorization();
        response.then().assertThat().
                statusCode(401). // проверь статус ответа
                and().body("success", equalTo(false))
                .and().body("message", equalTo(failMessage));
    }

    @After
    @Description("Удаление пользователя")
    public void tearDown() throws Exception {
        userClient.deleteUser(accessToken);
        response.then().assertThat().
                statusCode(200);
    }
}