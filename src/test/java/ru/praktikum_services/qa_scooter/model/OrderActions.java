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
    public static Response getOrderByTrackNumberAndGetResponse(String orderTrackNumber)
    {

        return  given()
                .header("Content-type", "application/json")
                .and()
                .when().queryParam("t", orderTrackNumber)
                .get("https://qa-scooter.praktikum-services.ru/api/v1/orders/track");
    }

    public static Response getOrdersListForCourier(String courierId, String stationsId, String limit, String page)
    {

        return given()
                .and()
                .when().queryParam("courierId", courierId)
                .queryParam("nearestStation", stationsId)
                .queryParam("limit", limit)
                .queryParam("page", page)
                .get("https://qa-scooter.praktikum-services.ru/api/v1/orders");

    }
    public static Response acceptOrderByIdAndGetResponse(String orderId, String courierId)
    {

        return given()
                .header("Content-type", "application/json")
                .and()
                .when().queryParam("courierId", courierId)
                .put("https://qa-scooter.praktikum-services.ru/api/v1/orders/accept/{orderId}", orderId);
    }

    public static void completeOrderByOrderIdAndGetResponse(String orderId) throws CompleteOrderException {
        Response response =given()
                .header("Content-type", "application/json")
                .and()
                .put("https://qa-scooter.praktikum-services.ru/api/v1/orders/finish/{orderId}", orderId);

        if(response.statusCode()!=200) {
            throw  new CompleteOrderException("Ошибка при завершении заказа");
        }
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

        Response response = getOrderByTrackNumberAndGetResponse(orderTrackNumber);
        JsonPath jsonPath = new JsonPath(response.thenReturn().getBody().asString());
        return jsonPath.getString("order.id");

    }


    public static String getOrderTrackNumberFromCreatedOrderResponse(Response response)
    {
        JsonPath jsonPath = new JsonPath(response.thenReturn().getBody().asString());
        return jsonPath.getString("track");
    }





}
