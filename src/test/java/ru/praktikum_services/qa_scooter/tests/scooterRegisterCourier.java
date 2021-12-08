package ru.praktikum_services.qa_scooter.tests;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import java.util.ArrayList;
import static io.restassured.RestAssured.*;

public class scooterRegisterCourier {



    public String serializeCourierAccount(CourierAccount courierAccount)
    {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
        return  gson.toJson(courierAccount);

    }

    public Response createNewCourierLoginAndCheckResponse(CourierAccount courierAccount)
    {

        String json = serializeCourierAccount(courierAccount);
        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body(json)
                .when()
                .post("https://qa-scooter.praktikum-services.ru/api/v1/courier");

        // если регистрация прошла успешно (код ответа 201), добавляем в список логин и пароль курьера
        return response;

    }

}
