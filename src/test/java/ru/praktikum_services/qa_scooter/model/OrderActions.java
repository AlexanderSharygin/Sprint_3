package ru.praktikum_services.qa_scooter.model;

import io.qameta.allure.Step;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;



import static io.restassured.RestAssured.given;

public class OrderActions {
    @Step("Create new order, return response for request")
    public static Response createNewOrderAndGetResponse(Order order)
    {

        return  given()
            .header("Content-type", "application/json")
            .and()
            .body(order)
            .when()
            .post("https://qa-scooter.praktikum-services.ru/api/v1/orders");
    }
    @Step("Get order by TrackNumber, return response for request")
    public static Response getOrderByTrackNumberAndGetResponse(String orderTrackNumber)
    {

        return  given()
                .header("Content-type", "application/json")
                .and()
                .when().queryParam("t", orderTrackNumber)
                .get("https://qa-scooter.praktikum-services.ru/api/v1/orders/track");
    }
    @Step("Get order list, return response for request")
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
    @Step("Accept order by Id, return response for request")
    public static Response acceptOrderByIdAndGetResponse(String orderId, String courierId)
    {

        return given()
                .header("Content-type", "application/json")
                .and()
                .when().queryParam("courierId", courierId)
                .put("https://qa-scooter.praktikum-services.ru/api/v1/orders/accept/{orderId}", orderId);
    }
    @Step("Complete order by Id, return response for request")
    public static void completeOrderByOrderIdAndGetResponse(String orderId) throws CompleteOrderException {
        Response response =given()
                .header("Content-type", "application/json")
                .and()
                .put("https://qa-scooter.praktikum-services.ru/api/v1/orders/finish/{orderId}", orderId);

        if(response.statusCode()!=200) {
            throw  new CompleteOrderException("Ошибка при завершении заказа");
        }
    }
    @Step("Delete order that was created for testing purpose")
    public static void cancelOrderByTrackNumberAndGetResponse(String orderTrackNumber)
    {
       Response response = given()
                .and()
                .when()
                .put("https://qa-scooter.praktikum-services.ru/api/v1/orders/cancel/?track={track}", orderTrackNumber);

    }

    @Step("Get order id by order TrackNumber")
    public static String getOrderIdByOrderTrackNumber(String orderTrackNumber)
    {

        Response response = getOrderByTrackNumberAndGetResponse(orderTrackNumber);
        JsonPath jsonPath = new JsonPath(response.thenReturn().getBody().asString());
        return jsonPath.getString("order.id");

    }

    @Step("Get order TrackNumber from CreateOrder request response")
    public static String getOrderTrackNumberFromCreatedOrderResponse(Response response)
    {
        JsonPath jsonPath = new JsonPath(response.thenReturn().getBody().asString());
        return jsonPath.getString("track");
    }





}
