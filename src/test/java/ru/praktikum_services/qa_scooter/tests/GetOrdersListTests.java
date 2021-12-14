package ru.praktikum_services.qa_scooter.tests;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.Test;
import ru.praktikum_services.qa_scooter.model.CompleteOrderException;
import ru.praktikum_services.qa_scooter.model.CourierAccount;
import ru.praktikum_services.qa_scooter.model.Order;
import ru.praktikum_services.qa_scooter.model.RemoveTestDataException;
import java.util.ArrayList;
import static org.hamcrest.Matchers.*;

import static ru.praktikum_services.qa_scooter.model.CourierAccountActions.*;
import static ru.praktikum_services.qa_scooter.model.CourierAccountActions.deleteTestDataFromDB;
import static ru.praktikum_services.qa_scooter.model.OrderActions.*;
import static ru.praktikum_services.qa_scooter.model.OrderActions.acceptOrderByIdAndGetResponse;

public class GetOrdersListTests {
    @Test
    @DisplayName("Get orders list for courier with correct ID and check result")
    public void getOrdersListForCertainCourierByCourierIdAndCheckReceivedListSuccess() throws RemoveTestDataException, CompleteOrderException {
        //add new test data (orders list and the new courier account)
        ArrayList<Order> orders = new ArrayList<>(10);
        for (int i = 0; i < 10; i++) {
            orders.add(new Order(new String[]{"BLACK"},1));
        }
        CourierAccount courierAccount = new CourierAccount(false, false, false);
        registerNewCourierAccountAndGetResponse(courierAccount);
        Response loginAccountResponse = loginCourierAndGetResponse(courierAccount);
        String courierId = getCourierAccountIdFromLoginResponse(loginAccountResponse);

        //accept/complete orders (to assign orders for the certain courier)
        for (int i = 0; i < orders.size(); i++) {
            Response createdOrderResponse = createNewOrderAndGetResponse(orders.get(i));
            String orderTrackNumber = getOrderTrackNumberFromCreatedOrderResponse(createdOrderResponse);
            String orderId = getOrderIdByOrderTrackNumber(orderTrackNumber);
            acceptOrderByIdAndGetResponse(orderId, courierId);
         if (i<(orders.size()/2))
          {
            completeOrderByOrderIdAndGetResponse(orderId);
          }
        }
        // getOrderList and check that list contains correct orders count
        Response getOrdersResponse = getOrdersListForCourier(courierId, null, null, null);
        getOrdersResponse.then().assertThat().statusCode(200).and().body("orders.size()", is(10));
       //remove test data from DB
        deleteTestDataFromDB(courierAccount);

    }
    @Test
    @DisplayName("Get available orders list for any courier and check result")
    public void getOrdersListForAnyCourierAndCheckReceivedListSuccess() throws RemoveTestDataException {
        //add new test data (orders list)
       ArrayList<Order> orders = new ArrayList<>(10);
       ArrayList<String> trackNumbers = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            orders.add(new Order(new String[]{"BLACK"},1));
        }
        for (Order order : orders) {
            Response createdOrderResponse = createNewOrderAndGetResponse(order);
            JsonPath jsonPath = new JsonPath(createdOrderResponse.thenReturn().getBody().asString());
            trackNumbers.add(jsonPath.getString("track"));
        }

        // getOrderList and check that list contains correct orders count
        Response getOrdersResponse = getOrdersListForCourier("", null, null, null);
        getOrdersResponse.then().assertThat().statusCode(200).and().body("orders.size()", is(greaterThanOrEqualTo(10)));

        //remove test data from DB
        for (String trackNumber : trackNumbers) {
            cancelOrderByTrackNumberAndGetResponse(trackNumber);
        }

    }

    @Test
    @DisplayName("Get orders list for courier with correct ID near metrostations and check result")
    public void getOrdersListForCertainCourierNearStationsAndCheckReceivedListSuccess() throws RemoveTestDataException, CompleteOrderException {
        //add new test data (orders list and the new courier account)
        ArrayList<Order> orders = new ArrayList<>(10);
        for (int i = 0; i < 10; i++) {
            if (i<3) {
                orders.add(new Order(new String[]{"BLACK"}, 5));
            }
            else if (i<8)
            {
                orders.add(new Order(new String[]{"BLACK"}, 6));
            }
            else
            {
                orders.add(new Order(new String[]{"BLACK"}, 7));
            }
        }
        CourierAccount courierAccount = new CourierAccount(false, false, false);
        registerNewCourierAccountAndGetResponse(courierAccount);
        Response loginAccountResponse = loginCourierAndGetResponse(courierAccount);
        String courierId = getCourierAccountIdFromLoginResponse(loginAccountResponse);

        //accept/complete orders (to assign orders for the certain courier)
        for (int i = 0; i < orders.size(); i++) {
            Response createdOrderResponse = createNewOrderAndGetResponse(orders.get(i));
            String orderTrackNumber = getOrderTrackNumberFromCreatedOrderResponse(createdOrderResponse);
            String orderId = getOrderIdByOrderTrackNumber(orderTrackNumber);
            acceptOrderByIdAndGetResponse(orderId, courierId);
            if (i<(orders.size()/2))
            {
               completeOrderByOrderIdAndGetResponse(orderId);

            }
        }

        // getOrderList and check that list contains correct orders count
        Response getOrdersResponse = getOrdersListForCourier(courierId, "[\"5\", \"6\"]", null, null);
        getOrdersResponse.then().assertThat().statusCode(200).and().body("orders.size()", is(8));
        //remove test data from DB
        deleteTestDataFromDB(courierAccount);

    }

    @Test
    @DisplayName("Get orders list available for any courier and check result")
    public void getOrdersListAvailableForAnyCourierAndCheckReceivedListSuccess() throws RemoveTestDataException {
        //add new test data (orders list and the new courier account)
        ArrayList<Order> orders = new ArrayList<>(10);
        ArrayList<String> trackNumbers = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            orders.add(new Order(new String[]{"BLACK"},1));
        }
        for (Order order : orders) {
            Response createdOrderResponse = createNewOrderAndGetResponse(order);
            JsonPath jsonPath = new JsonPath(createdOrderResponse.thenReturn().getBody().asString());
            trackNumbers.add(jsonPath.getString("track"));
        }

        // getOrderList and check that list contains correct orders count
        Response getOrdersResponse = getOrdersListForCourier("", null, "10", "0");
        getOrdersResponse.then().assertThat().statusCode(200).and().body("orders.size()", is(10));

        //remove test data from DB
        for (String trackNumber : trackNumbers) {
            cancelOrderByTrackNumberAndGetResponse(trackNumber);
        }

    }

    @Test
    @DisplayName("Get orders list available for any courier near metrostations and check result")
    public void getTenOrdersAvailableForAnyCourierNearStationAndCheckReceivedListSuccess() throws RemoveTestDataException {

        //add new test data (orders list)
        ArrayList<Order> orders = new ArrayList<>(10);
        ArrayList<String> trackNumbers = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            orders.add(new Order(new String[]{"BLACK"},15));
        }
        for (Order order : orders) {
            Response createdOrderResponse = createNewOrderAndGetResponse(order);
            JsonPath jsonPath = new JsonPath(createdOrderResponse.thenReturn().getBody().asString());
            trackNumbers.add(jsonPath.getString("track"));
        }

        // getOrderList and check that list contains correct orders count
        Response getOrdersResponse = getOrdersListForCourier("", "[\"15\"]", "10", "0");
        getOrdersResponse.then().assertThat().statusCode(200).and().body("orders.size()", is(10));

        //remove test data from DB
        for (String trackNumber : trackNumbers) {
            cancelOrderByTrackNumberAndGetResponse(trackNumber);
        }

    }
}
