package ru.praktikum_services.qa_scooter.tests;

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
import static ru.praktikum_services.qa_scooter.model.OrderActions.acceptOrderAndGetResponse;

public class GetOrdersListTests {
    @Test
    public void getOrdersListForCertainCourierByCourierIdAndCheckReceivedListSizeSuccess() throws RemoveTestDataException, CompleteOrderException {
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
            String orderTrackNumber = getOrderTrackNumberFromCreateOrderResponse(createdOrderResponse);
            String orderId = getOrderIdByOrderTrackNumber(orderTrackNumber);
            acceptOrderAndGetResponse(orderId, courierId);
         if (i<(orders.size()/2))
          {
            completeOrderAndGetResponse(orderId);
          }
        }
        // getOrderList and check that list contains correct orders count
        Response getOrdersResponse = getOrderListForCourier(courierId, null, null, null);
        getOrdersResponse.then().assertThat().statusCode(200).and().body("orders.size()", is(10));
       //remove test data from DB
        deleteTestDataFromDB(courierAccount);

    }
    @Test
    public void getOrdersListForAnyCourierAndCheckReceivedListSizeSuccess() throws RemoveTestDataException {
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
        Response getOrdersResponse = getOrderListForCourier("", null, null, null);
        getOrdersResponse.then().assertThat().statusCode(200).and().body("orders.size()", is(greaterThanOrEqualTo(10)));

        //remove test data from DB
        for (String trackNumber : trackNumbers) {
            cancelOrderByTrackNumberAndGetResponse(trackNumber);
        }

    }

    @Test
    public void getOrdersListForCertainCourierNearStationsAndCheckReceivedListSizeSuccess() throws RemoveTestDataException, CompleteOrderException {
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
            String orderTrackNumber = getOrderTrackNumberFromCreateOrderResponse(createdOrderResponse);
            String orderId = getOrderIdByOrderTrackNumber(orderTrackNumber);
            acceptOrderAndGetResponse(orderId, courierId);
            if (i<(orders.size()/2))
            {
               completeOrderAndGetResponse(orderId);

            }
        }

        // getOrderList and check that list contains correct orders count
        Response getOrdersResponse = getOrderListForCourier(courierId, "[\"5\", \"6\"]", null, null);
        getOrdersResponse.then().assertThat().statusCode(200).and().body("orders.size()", is(8));
        //remove test data from DB
        deleteTestDataFromDB(courierAccount);

    }

    @Test
    public void getListWithTenOrdersAvailableForAnyCourierAndCheckReceivedListSizeSuccess() throws RemoveTestDataException {
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
        Response getOrdersResponse = getOrderListForCourier("", null, "10", "0");
        getOrdersResponse.then().assertThat().statusCode(200).and().body("orders.size()", is(10));

        //remove test data from DB
        for (String trackNumber : trackNumbers) {
            cancelOrderByTrackNumberAndGetResponse(trackNumber);
        }

    }

    @Test
    public void getListWithTenOrdersAvailableForAnyCourierNearStationAndCheckReceivedListSizeSuccess() throws RemoveTestDataException {

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
        Response getOrdersResponse = getOrderListForCourier("", "[\"15\"]", "10", "0");
        getOrdersResponse.then().assertThat().statusCode(200).and().body("orders.size()", is(10));

        //remove test data from DB
        for (String trackNumber : trackNumbers) {
            cancelOrderByTrackNumberAndGetResponse(trackNumber);
        }

    }
}
