package ru.praktikum_services.qa_scooter.model;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class CourierAccountClient extends RestAssuredClient {

    private static final String COURIER_PATH = "api/v1/courier";

    @Step("Register new account and return Response for registration request")
    public ValidatableResponse registerNewCourierAccount(CourierAccount courierAccount) {

        return given()
                .spec(getBaseSpec())
                .and()
                .body(courierAccount)
                .when()
                .post(COURIER_PATH).then();
    }

    @Step("Login courier account and return Response for registration request")
    public ValidatableResponse loginCourierAccount(CourierCredentials courierCredentials) {
        return given()
                .spec(getBaseSpec())
                .body(courierCredentials)
                .when()
                .post(COURIER_PATH + "/login").then();


    }

    @Step("Delete courier account by id")
    public ValidatableResponse deleteCourierAccount(String accountId) {
        return given().spec(getBaseSpec())
                .and().delete(COURIER_PATH + "/" + accountId).then();
    }


}


