package ru.praktikum_services.qa_scooter.model;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class RestAssuredClient {
    public RequestSpecification getBaseSpec()
    {
        return new RequestSpecBuilder().setContentType(ContentType.JSON)
                .setBaseUri("https://qa-scooter.praktikum-services.ru/").build();
    }
}
