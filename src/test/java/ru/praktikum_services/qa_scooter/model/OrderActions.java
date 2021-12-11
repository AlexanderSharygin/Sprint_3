package ru.praktikum_services.qa_scooter.model;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class OrderActions {

    public static Response createNewOrderAndGetResponse(Order order)
    {

        return  given()
            .header("Content-type", "application/json")
            .and()
            .body(order)
            .when()
            .post("https://qa-scooter.praktikum-services.ru/api/v1/orders");
    }
    public static void cancelOrderByTrackNumberAndGetResponse(String orderTrackNumber) throws RemoveTestDataException
    {
       Response response = given()
                .and()
                .when()
                .put("https://qa-scooter.praktikum-services.ru/api/v1/orders/cancel/?track={track}", orderTrackNumber);
        if(response.statusCode()!=200) {
            throw  new RemoveTestDataException("Ошибка при удалении тестовых данных из базы данных");
        }
    }

    public static String getOrderIdByOrderTrackNumber(String orderTrackNumber)
    {

        Response response =  given()
                .header("Content-type", "application/json")
                .and()
                .when().queryParam("t", orderTrackNumber)
                .get("https://qa-scooter.praktikum-services.ru/api/v1/orders/track");
        JsonPath jsonPath = new JsonPath(response.thenReturn().getBody().asString());
        return jsonPath.getString("order.id");

    }

    public static Response acceptOrderAndGetResponse(String orderId, String courierId)
    {

        Response response =  given()
                .header("Content-type", "application/json")
                .and()
                .when().queryParam("courierId", courierId)
                .put("https://qa-scooter.praktikum-services.ru/api/v1/orders/accept/{orderId}", orderId);
      return response;

    }
}
