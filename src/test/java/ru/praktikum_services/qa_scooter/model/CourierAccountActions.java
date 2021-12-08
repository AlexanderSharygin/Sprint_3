package ru.praktikum_services.qa_scooter.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import java.util.ArrayList;
import static io.restassured.RestAssured.*;

public class CourierAccountActions {

    private ArrayList<CourierAccount> successfullyRegisteredAccounts = new ArrayList<>();

    private String serializeCourierAccount(CourierAccount courierAccount)
    {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
        return  gson.toJson(courierAccount);
    }
    public Response registerNewCourierLoginAndGetResponse(CourierAccount courierAccount)
    {

        String json = serializeCourierAccount(courierAccount);
        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body(json)
                .when()
                .post("https://qa-scooter.praktikum-services.ru/api/v1/courier");

       if(response.statusCode()==201) {
           successfullyRegisteredAccounts.add(courierAccount);
       }
        return response;
    }

    public void removeAllCreatedAccounts() throws RemoveTestDataException
    {
        for(int i = 0; i< successfullyRegisteredAccounts.size(); i++)
        {
           String json = serializeCourierAccount(successfullyRegisteredAccounts.get(i));
           String responseBody = given()
                    .header("Content-type", "application/json")
                    .and()
                    .body(json)
                    .when()
                    .post("https://qa-scooter.praktikum-services.ru/api/v1/courier/login").thenReturn().getBody().asString();
            JsonPath jsonPath = new JsonPath(responseBody);
            String accountId = jsonPath.getString("id");
            Response response = given().delete("https://qa-scooter.praktikum-services.ru/api/v1/courier/"+accountId);
            if(response.statusCode()!=200) {
                throw  new RemoveTestDataException("Ошибка при удалении тестовых данных из базы данных");
            }

        }

    }

}
