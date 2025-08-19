package tests;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.order.OrderAPI;
import org.junit.Test;

import java.net.HttpURLConnection;

import static org.junit.Assert.assertNotNull;

public class GetOrderListTest {
    OrderAPI orderAPI = new OrderAPI();


    @Test
    @DisplayName("Получение списка заказов")
    public void getListOfOrders() {
        ValidatableResponse getOrderListResponse = orderAPI.getOrderList();
        getOrderListResponse.statusCode(HttpURLConnection.HTTP_OK);
        assertNotNull(getOrderListResponse.extract().path("orders"));
    }

}
