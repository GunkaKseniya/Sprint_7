package tests;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.order.Order;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.order.OrderAPI;

import java.net.HttpURLConnection;

import static org.junit.Assert.assertNotNull;

@RunWith(Parameterized.class)
public class CreateOrderTest {
    OrderAPI orderAPI = new OrderAPI();
    private int trackId;
    private final String[] color;

    public CreateOrderTest(String[] color) {
        this.color = color;
    }

    @Parameterized.Parameters(name = "Цвет самоката. Тестовые данные: \"GREY\", \"GREY\", \"BLACK\", \"BLACK\", null")
    public static Object[][] getColorData() {
        return new Object[][]{
                {new String[]{"GREY"}},
                {new String[]{"BLACK"}},
                {new String[]{"GREY", "BLACK"}},
                {new String[]{null}},
        };
    }

    @Test
    @DisplayName("Cоздание заказа с разными цветами самоката")
    public void createOrder() {
        Order order = new Order("Ксения", "Гунька", "Москва, Коминтерна, 54к2", 5, "8 905 000 00 00", 2, "2025-08-20", "Нужен в первой половине дня", color);
        ValidatableResponse orderCreateResponse = orderAPI.createOrder(order);
        orderCreateResponse.statusCode(HttpURLConnection.HTTP_CREATED);
        trackId = orderCreateResponse.extract().path("track");
        assertNotNull(trackId);

    }

    @After
    public void tearDown() {
        if (trackId > 0) {
            orderAPI.cancelOrder(trackId);
        }
    }

}
