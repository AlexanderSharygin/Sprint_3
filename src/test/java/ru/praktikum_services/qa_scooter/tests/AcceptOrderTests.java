package ru.praktikum_services.qa_scooter.tests;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.Test;
import ru.praktikum_services.qa_scooter.model.CourierAccount;
import ru.praktikum_services.qa_scooter.model.CourierAccountActions;
import ru.praktikum_services.qa_scooter.model.Order;
import ru.praktikum_services.qa_scooter.model.RemoveTestDataException;
import static org.hamcrest.Matchers.equalTo;
import static ru.praktikum_services.qa_scooter.model.CourierAccountActions.*;
import static ru.praktikum_services.qa_scooter.model.OrderActions.*;

public class AcceptOrderTests {
    @Test
    public void acceptNewCorrectOrderSuccess() throws RemoveTestDataException {
        Order order = new Order(new String[]{"BLACK"});
        Response createResponse = createNewOrderAndGetResponse(order);
        JsonPath jsonPath = new JsonPath(createResponse.thenReturn().getBody().asString());
        String orderTrackNumber = jsonPath.getString("track");
        String orderId = getOrderIdByOrderTrackNumber(orderTrackNumber);
        CourierAccount courierAccount = new CourierAccount(false, false, false);
        CourierAccountActions.registerNewCourierAccountAndGetResponse(courierAccount);
        Response loginResponse = loginCourierAndGetResponse(courierAccount);
        String courierId = getCourierAccountIdFromLoginResponse(loginResponse);
        Response response = acceptOrderAndGetResponse(orderId, courierId);
        response.then().assertThat().statusCode(200).and().body("ok", equalTo(true));
        deleteTestDataFromDB(courierAccount);

    }
    @Test
    public void acceptOrderWithoutOrderNumberBadRequest() throws RemoveTestDataException {


        CourierAccount courierAccount = new CourierAccount(false, false, false);
        CourierAccountActions.registerNewCourierAccountAndGetResponse(courierAccount);
        Response loginResponse = loginCourierAndGetResponse(courierAccount);
        String courierId = getCourierAccountIdFromLoginResponse(loginResponse);
        Response response = acceptOrderAndGetResponse("", courierId);
        response.then().assertThat().statusCode(400).and().body("message", equalTo("Недостаточно данных для поиска"));
        deleteTestDataFromDB(courierAccount);

    }
    @Test
    public void acceptNewCorrectOrderWithoutCourierIdConflict()  {
        Order order = new Order(new String[]{"BLACK"});
        Response createResponse = createNewOrderAndGetResponse(order);
        JsonPath jsonPath = new JsonPath(createResponse.thenReturn().getBody().asString());
        String orderTrackNumber = jsonPath.getString("track");
        String orderId = getOrderIdByOrderTrackNumber(orderTrackNumber);
        String courierId =  "";
        Response response = acceptOrderAndGetResponse(orderId, courierId);
        response.then().assertThat().statusCode(400).and().body("message", equalTo("Недостаточно данных для поиска"));


    }

    @Test
    public void acceptOrderWithWrongOrderNumberNotFound() throws RemoveTestDataException {

        String orderId =  String.valueOf(1000000 + (int) (Math.random() * 2000000));
        CourierAccount courierAccount = new CourierAccount(false, false, false);
        CourierAccountActions.registerNewCourierAccountAndGetResponse(courierAccount);
        Response loginResponse = loginCourierAndGetResponse(courierAccount);
        String courierId = getCourierAccountIdFromLoginResponse(loginResponse);
        Response response = acceptOrderAndGetResponse(orderId, courierId);
        response.then().assertThat().statusCode(404).and().body("message", equalTo("Заказа с таким id не существует"));
        deleteTestDataFromDB(courierAccount);

    }

    @Test
    public void acceptNewCorrectOrderWithWrongCourierIdNotFound()  {
        Order order = new Order(new String[]{"BLACK"});
        Response createResponse = createNewOrderAndGetResponse(order);
        JsonPath jsonPath = new JsonPath(createResponse.thenReturn().getBody().asString());
        String orderTrackNumber = jsonPath.getString("track");
        String orderId = getOrderIdByOrderTrackNumber(orderTrackNumber);
        String courierId =  String.valueOf(1000000 + (int) (Math.random() * 2000000));
        Response response = acceptOrderAndGetResponse(orderId, courierId);
        response.then().assertThat().statusCode(404).and().body("message", equalTo("Курьера с таким id не существует"));


    }


    @Test
    public void acceptAlreadyAcceptedOrderConflict() throws RemoveTestDataException {
        Order order = new Order(new String[]{"BLACK"});
        Response createResponse = createNewOrderAndGetResponse(order);
        JsonPath jsonPath = new JsonPath(createResponse.thenReturn().getBody().asString());
        String orderTrackNumber = jsonPath.getString("track");
        String orderId = getOrderIdByOrderTrackNumber(orderTrackNumber);
        CourierAccount courierAccount = new CourierAccount(false, false, false);
        CourierAccountActions.registerNewCourierAccountAndGetResponse(courierAccount);
        Response loginResponse = loginCourierAndGetResponse(courierAccount);
        String courierId = getCourierAccountIdFromLoginResponse(loginResponse);
        acceptOrderAndGetResponse(orderId, courierId);
        Response response = acceptOrderAndGetResponse(orderId, courierId);
        response.then().assertThat().statusCode(409).and().body("message", equalTo("Этот заказ уже в работе"));
        deleteTestDataFromDB(courierAccount);

    }
}
