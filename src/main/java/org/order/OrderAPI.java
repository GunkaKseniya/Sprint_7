package org.order;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import org.base.Configuration;


public class OrderAPI extends Configuration {
    private static final String ORDER_URL = "api/v1/orders";

    @Step("Создание заказа")
    public ValidatableResponse createOrder(Order order) {
        return spec()
                .and()
                .body(order)
                .when()
                .post(ORDER_URL)
                .then().log().all();
    }
    @Step("Получить список заказов")
    public ValidatableResponse getOrderList() {
        return spec()
                .when()
                .get(ORDER_URL)
                .then().log().all();

    }
    @Step("Отмена заказа")
    public ValidatableResponse cancelOrder(int trackId){
        return spec()
                .and()
                .queryParam("track", trackId)
                .when()
                .put(ORDER_URL + "/cancel")
                .then().log().all();
    }
}
