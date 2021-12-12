package ru.praktikum_services.qa_scooter.tests;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.Test;
import ru.praktikum_services.qa_scooter.model.CourierAccount;
import ru.praktikum_services.qa_scooter.model.Order;
import ru.praktikum_services.qa_scooter.model.RemoveTestDataException;
import java.util.ArrayList;

import static org.hamcrest.Matchers.*;
import static ru.praktikum_services.qa_scooter.model.CourierAccountActions.*;
import static ru.praktikum_services.qa_scooter.model.CourierAccountActions.deleteTestDataFromDB;
import static ru.praktikum_services.qa_scooter.model.OrderActions.*;
import static ru.praktikum_services.qa_scooter.model.OrderActions.acceptOrderAndGetResponse;

public class OrdersListTests {
    @Test
    public void getOrdersListForCertainCourierByCourierIdAndCheckReceivedListSizeSuccess() throws RemoveTestDataException {
        ArrayList<Order> orders = new ArrayList<>(10);
        for (int i = 0; i < 10; i++) {
            orders.add(new Order(new String[]{"BLACK"},1));
        }

        CourierAccount courierAccount = new CourierAccount(false, false, false);
        registerNewCourierAccountAndGetResponse(courierAccount);
        Response loginAccountResponse = loginCourierAndGetResponse(courierAccount);
        String courierId = getCourierAccountIdFromLoginResponse(loginAccountResponse);
        for (int i = 0; i < orders.size(); i++) {
            Response createdOrderResponse = createNewOrderAndGetResponse(orders.get(i));
            String orderTrackNumber = getOrderTrackNumberFromCreateOrderResponse(createdOrderResponse);
            String orderId = getOrderIdByOrderTrackNumber(orderTrackNumber);
            acceptOrderAndGetResponse(orderId, courierId);
         if (i<(orders.size()/2))
          {
            Response r = completeOrderAndGetResponse(orderId);
           r.then().assertThat().statusCode(200);

          }
        }
        Response response = getOrderListForCourier(courierId, null, null, null);
        response.then().assertThat().statusCode(200).and().body("orders.size()", is(10));
        deleteTestDataFromDB(courierAccount);

    }
    @Test
    public void getOrdersListForAnyCourierAndCheckReceivedListSizeSuccess() throws RemoveTestDataException {
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
        Response response = getOrderListForCourier("", null, null, null);
        response.then().assertThat().statusCode(200).and().body("orders.size()", is(greaterThanOrEqualTo(10)));

        for (String trackNumber : trackNumbers) {
            cancelOrderByTrackNumberAndGetResponse(trackNumber);
        }

    }

    @Test
    public void getOrdersListForCertainCourierNearStationsAndCheckReceivedListSizeSuccess() throws RemoveTestDataException {
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
        for (int i = 0; i < orders.size(); i++) {
            Response createdOrderResponse = createNewOrderAndGetResponse(orders.get(i));
            String orderTrackNumber = getOrderTrackNumberFromCreateOrderResponse(createdOrderResponse);
            String orderId = getOrderIdByOrderTrackNumber(orderTrackNumber);
            acceptOrderAndGetResponse(orderId, courierId);
            if (i<(orders.size()/2))
            {
                Response r = completeOrderAndGetResponse(orderId);
                r.then().assertThat().statusCode(200);
            }
        }

        Response response = getOrderListForCourier(courierId, "[\"5\", \"6\"]", null, null);

       response.then().assertThat().statusCode(200).and().body("orders.size()", is(8));
        deleteTestDataFromDB(courierAccount);

    }

    @Test
    public void getListWithTenOrdersAvailableForAnyCourierAndCheckReceivedListSizeSuccess() throws RemoveTestDataException {
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
        Response response = getOrderListForCourier("", null, "10", "0");
        response.then().assertThat().statusCode(200).and().body("orders.size()", is(10));

        for (String trackNumber : trackNumbers) {
            cancelOrderByTrackNumberAndGetResponse(trackNumber);
        }

    }

    @Test
    public void getListWithTenOrdersAvailableForAnyCourierNearStationAndCheckReceivedListSizeSuccess() throws RemoveTestDataException {
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
        Response response = getOrderListForCourier("", "[\"15\"]", "10", "0");
        response.then().assertThat().statusCode(200).and().body("orders.size()", is(10));

        for (String trackNumber : trackNumbers) {
            cancelOrderByTrackNumberAndGetResponse(trackNumber);
        }

    }
}
