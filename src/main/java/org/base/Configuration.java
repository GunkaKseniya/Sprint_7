package org.base;

import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;


public class Configuration {
    private static final String BASE_URL = "http://qa-scooter.praktikum-services.ru";
    public RequestSpecification spec() {
        return given().log().all()
                .contentType(ContentType.JSON).accept(ContentType.JSON)
                .baseUri(BASE_URL);

    }
}
