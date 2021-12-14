package ru.praktikum_services.qa_scooter.model;

import io.qameta.allure.Step;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;

public class CourierAccountActions {

    @Step("Register new account and return Response for registration request")
    public static Response registerNewCourierAccountAndGetResponse(CourierAccount courierAccount)
    {

        return given()
                .header("Content-type", "application/json")
                .and()
                .body(courierAccount)
                .when()
                .post("https://qa-scooter.praktikum-services.ru/api/v1/courier");
    }
    @Step("Login courier account and return Response for registration request")
    public static  Response loginCourierAndGetResponse(CourierAccount courierAccount)
    {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(courierAccount)
                .when()
                .post("https://qa-scooter.praktikum-services.ru/api/v1/courier/login");


    }
    @Step("Get courier account id from the login response request")
    public static String getCourierAccountIdFromLoginResponse(Response loginResponse)
    {
       JsonPath jsonPath = new JsonPath(loginResponse.thenReturn().getBody().asString());
       return jsonPath.getString("id");
    }
    @Step("Delete courier account by id")
    public static Response deleteCourierAndGetResponse(String accountId)
    {
        return given().delete("https://qa-scooter.praktikum-services.ru/api/v1/courier/{id}", accountId);
    }
    @Step("Delete courier account that was created for testing purpose")
   public static void deleteTestDataFromDB(CourierAccount courierAccount) throws RemoveTestDataException
    {
        Response loginResponse = loginCourierAndGetResponse(courierAccount);
        String accountId = getCourierAccountIdFromLoginResponse(loginResponse);
         Response response = deleteCourierAndGetResponse(accountId);
         if(response.statusCode()!=200) {
                throw  new RemoveTestDataException("Ошибка при удалении тестовых данных из базы данных");
            }
        }

    }


