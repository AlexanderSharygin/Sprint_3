package ru.praktikum_services.qa_scooter.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import java.util.ArrayList;
import static io.restassured.RestAssured.*;

public class CourierAccountActions {

    private ArrayList<String> registeredAccountsIds = new ArrayList<>();

    public Response registerNewCourierAccountAndGetResponse(CourierAccount courierAccount)
    {
        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body(courierAccount)
                .when()
                .post("https://qa-scooter.praktikum-services.ru/api/v1/courier");
       if(response.statusCode()==201) {
           Response loginResponse = loginCourierAndGetResponse(courierAccount);
           registeredAccountsIds.add(getCourierAccountIdFromLoginResponse(loginResponse));

       }
        return response;
    }
    public Response loginCourierAndGetResponse(CourierAccount courierAccount)
    {
        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body(courierAccount)
                .when()
                .post("https://qa-scooter.praktikum-services.ru/api/v1/courier/login");
        return response;


    }
    public String getCourierAccountIdFromLoginResponse(Response loginResponse)
    {
        JsonPath jsonPath = new JsonPath(loginResponse.thenReturn().getBody().asString());
       return jsonPath.getString("id");
    }

    public Response deleteCourierAndGetResponse(String accountId)
    {
        Response response = given().delete("https://qa-scooter.praktikum-services.ru/api/v1/courier/{id}", accountId);
        registeredAccountsIds.remove(accountId);
        return response;
    }

    public void removeAllCreatedAccounts() throws RemoveTestDataException
    {
        while (!registeredAccountsIds.isEmpty())
        {
            Response response = deleteCourierAndGetResponse(registeredAccountsIds.get(0));
            if(response.statusCode()!=200) {
                throw  new RemoveTestDataException("Ошибка при удалении тестовых данных из базы данных");
            }
        }

    }

}
