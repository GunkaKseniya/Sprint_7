package tests;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.courier.Courier;
import org.courier.CourierLoginDetails;
import org.courier.CourierAPI;
import org.junit.After;
import org.junit.Test;

import java.net.HttpURLConnection;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;


public class CreateCourierTest {
    CourierAPI courierAPI = new CourierAPI();
    private int id;


    @Test
    @DisplayName("Успешный запрос создания возвращает ok: true")
    public void courierSuccessfulCreation() {
        Courier courier = Courier.random();
        ValidatableResponse createResponse = courierAPI.createCourier(courier);
        createResponse.statusCode(HttpURLConnection.HTTP_CREATED).extract().path("ok");
        var credentials = CourierLoginDetails.fromCourier(courier);
        ValidatableResponse loginResponse = courierAPI.logIn(credentials);
        loginResponse.statusCode(HttpURLConnection.HTTP_OK);
        id = loginResponse.extract().path("id");
        assertNotEquals(0, id);
    }

    @Test
    @DisplayName("Нельзя создать двух одинаковых курьеров")
    public void createDuplicateCourierFailed() {
        Courier courier = Courier.random();
        ValidatableResponse createResponse = courierAPI.createCourier(courier);
        ValidatableResponse createResponseTwice = courierAPI.createCourier(courier);
        createResponse.statusCode(HttpURLConnection.HTTP_CREATED).extract().path("ok");
        createResponseTwice.statusCode(HttpURLConnection.HTTP_CONFLICT);
        String errorMessage = createResponseTwice.extract().path("message");
        assertEquals("Этот логин уже используется. Попробуйте другой.", errorMessage);

    }

    @Test
    @DisplayName("Если нет логина, запрос возвращает ошибку")
    public void cannotCreateCourierWithNoLoginNoData() {
        Courier courier = new Courier(null, "5588pass", "Gunka");
        ValidatableResponse createResponse = courierAPI.createCourier(courier);
        createResponse.statusCode(HttpURLConnection.HTTP_BAD_REQUEST);
        createResponse.body("message", equalTo("Недостаточно данных для создания учетной записи"));

    }

    @Test
    @DisplayName("Если нет пароля, запрос возвращает ошибку")
    public void cannotCreateCourierWithNoPasswordNoData() {
        Courier courier = new Courier("Kseniya", null, "Gunka");
        ValidatableResponse createResponse = courierAPI.createCourier(courier);
        createResponse.statusCode(HttpURLConnection.HTTP_BAD_REQUEST);
        createResponse.body("message", equalTo("Недостаточно данных для создания учетной записи"));

    }

    @After
    public void deleteCourier() {
        if (id > 0) {
            courierAPI.delete(id);
        }
    }
}
