package tests;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.courier.Courier;
import org.courier.CourierLoginDetails;
import org.courier.CourierAPI;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.net.HttpURLConnection;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertNotNull;

public class CourierLoginTest {
    private CourierAPI courierAPI;
    private Courier courier;
    private int id;

    @Before
    public void setUp() {
        courierAPI = new CourierAPI();
        courier = Courier.random();
        courierAPI.createCourier(courier);
    }

    @Test
    @DisplayName("Успешная авторизация, возврат id")
    public void courierSuccessfulAuthorization() {
        var credentials = CourierLoginDetails.fromCourier(courier);
        ValidatableResponse loginResponse = courierAPI.logIn(credentials);
        id = loginResponse.extract().path("id");
        loginResponse.statusCode(HttpURLConnection.HTTP_OK);
        assertNotNull(loginResponse.extract().body().path("id"));
    }

    @Test
    @DisplayName("Логин курьера без данных")
    public void courierLogInWithNoParamsFailed() {
        CourierLoginDetails courierLoginDetails = new CourierLoginDetails("", "");
        ValidatableResponse loginResponse = courierAPI.logIn(courierLoginDetails);
        loginResponse.statusCode(HttpURLConnection.HTTP_BAD_REQUEST);
        loginResponse.body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Логин курьера без логина")
    public void courierLogInWithNoLoginFailed() {
        CourierLoginDetails courierLoginDetails = new CourierLoginDetails("", "5588pass");
        ValidatableResponse loginResponse = courierAPI.logIn(courierLoginDetails);
        loginResponse.statusCode(HttpURLConnection.HTTP_BAD_REQUEST);
        loginResponse.body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Логин курьера без пароля")
    public void courierLogInWithNoPasswordFailed() {
        CourierLoginDetails courierLoginDetails = new CourierLoginDetails("Kseniya", "");
        ValidatableResponse loginResponse = courierAPI.logIn(courierLoginDetails);
        loginResponse.statusCode(HttpURLConnection.HTTP_BAD_REQUEST);
        loginResponse.body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Вход c неверным логином")
    public void courierLogInWithIncorrectLoginAccountNotFound() {
        CourierLoginDetails correctCredentials = CourierLoginDetails.fromCourier(courier);
        CourierLoginDetails incorrectLoginCredentials = new CourierLoginDetails("Ayinesk", correctCredentials.getPassword());
        ValidatableResponse loginResponse = courierAPI.logIn(incorrectLoginCredentials);
        loginResponse.statusCode(HttpURLConnection.HTTP_NOT_FOUND);
        loginResponse.body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Вход c неверным паролем")
    public void courierLogInWithIncorrectPasswordAccountNotFound() {
        CourierLoginDetails correctCredentials = CourierLoginDetails.fromCourier(courier);
        CourierLoginDetails incorrectLoginCredentials = new CourierLoginDetails(correctCredentials.getLogin(), "pass5599");
        ValidatableResponse loginResponse = courierAPI.logIn(incorrectLoginCredentials);
        loginResponse.statusCode(HttpURLConnection.HTTP_NOT_FOUND);
        loginResponse.body("message", equalTo("Учетная запись не найдена"));
    }

    @After
    public void tearDown() {
        if (id > 0) {
            courierAPI.delete(id);
        }
    }
}