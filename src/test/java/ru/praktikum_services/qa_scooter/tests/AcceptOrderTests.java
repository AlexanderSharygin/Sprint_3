package ru.praktikum_services.qa_scooter.tests;


import io.restassured.response.Response;
import org.junit.Test;
import ru.praktikum_services.qa_scooter.model.CourierAccount;
import ru.praktikum_services.qa_scooter.model.Order;
import ru.praktikum_services.qa_scooter.model.RemoveTestDataException;
import static org.hamcrest.Matchers.equalTo;
import static ru.praktikum_services.qa_scooter.model.CourierAccountActions.*;
import static ru.praktikum_services.qa_scooter.model.OrderActions.*;

public class AcceptOrderTests {
    @Test
    public void acceptNewCorrectOrderSuccess() throws RemoveTestDataException {
        Order order = new Order(new String[]{"BLACK"});
        CourierAccount courierAccount = new CourierAccount(false, false, false);
        Response createdOrderResponse = createNewOrderAndGetResponse(order);
        String orderTrackNumber = getOrderTrackNumberFromCreateOrderResponse(createdOrderResponse);
        String orderId = getOrderIdByOrderTrackNumber(orderTrackNumber);
        registerNewCourierAccountAndGetResponse(courierAccount);
        Response loginAccountResponse = loginCourierAndGetResponse(courierAccount);
        String courierId = getCourierAccountIdFromLoginResponse(loginAccountResponse);
        Response acceptOrderResponse = acceptOrderAndGetResponse(orderId, courierId);
        acceptOrderResponse.then().assertThat().statusCode(200).and().body("ok", equalTo(true));
        deleteTestDataFromDB(courierAccount);

    }
    @Test
    public void acceptOrderWithoutOrderNumberBadRequest() throws RemoveTestDataException {


        CourierAccount courierAccount = new CourierAccount(false, false, false);
        registerNewCourierAccountAndGetResponse(courierAccount);
        Response loginAccountResponse = loginCourierAndGetResponse(courierAccount);
        String courierId = getCourierAccountIdFromLoginResponse(loginAccountResponse);
        Response acceptOrderResponse = acceptOrderAndGetResponse("", courierId);
        acceptOrderResponse.then().assertThat().statusCode(400).and().body("message", equalTo("Недостаточно данных для поиска"));
        deleteTestDataFromDB(courierAccount);

    }
    @Test
    public void acceptNewCorrectOrderWithoutCourierIdConflict()  {
        Order order = new Order(new String[]{"BLACK"});
        Response createdOrderResponse = createNewOrderAndGetResponse(order);
        String orderTrackNumber = getOrderTrackNumberFromCreateOrderResponse(createdOrderResponse);
        String orderId = getOrderIdByOrderTrackNumber(orderTrackNumber);
        String courierId =  "";
        Response acceptedOrderResponse = acceptOrderAndGetResponse(orderId, courierId);
        acceptedOrderResponse.then().assertThat().statusCode(400).and().body("message", equalTo("Недостаточно данных для поиска"));


    }

    @Test
    public void acceptOrderWithWrongOrderNumberNotFound() throws RemoveTestDataException {

        String orderId =  String.valueOf(1000000 + (int) (Math.random() * 2000000));
        CourierAccount courierAccount = new CourierAccount(false, false, false);
        registerNewCourierAccountAndGetResponse(courierAccount);
        Response loginAccountResponse = loginCourierAndGetResponse(courierAccount);
        String courierId = getCourierAccountIdFromLoginResponse(loginAccountResponse);
        Response acceptedOrderResponse = acceptOrderAndGetResponse(orderId, courierId);
        acceptedOrderResponse.then().assertThat().statusCode(404).and().body("message", equalTo("Заказа с таким id не существует"));
        deleteTestDataFromDB(courierAccount);

    }

    @Test
    public void acceptNewCorrectOrderWithWrongCourierIdNotFound()  {
        Order order = new Order(new String[]{"BLACK"});
        Response createdOrderResponse = createNewOrderAndGetResponse(order);
        String orderTrackNumber = getOrderTrackNumberFromCreateOrderResponse(createdOrderResponse);
        String orderId = getOrderIdByOrderTrackNumber(orderTrackNumber);
        String courierId =  String.valueOf(1000000 + (int) (Math.random() * 2000000));
        Response acceptedOrderResponse = acceptOrderAndGetResponse(orderId, courierId);
        acceptedOrderResponse.then().assertThat().statusCode(404).and().body("message", equalTo("Курьера с таким id не существует"));
    }


    @Test
    public void acceptAlreadyAcceptedOrderConflict() throws RemoveTestDataException {
        Order order = new Order(new String[]{"BLACK"});
        CourierAccount courierAccount = new CourierAccount(false, false, false);
        Response createdOrderResponse = createNewOrderAndGetResponse(order);
        String orderTrackNumber =getOrderTrackNumberFromCreateOrderResponse(createdOrderResponse);
        String orderId = getOrderIdByOrderTrackNumber(orderTrackNumber);
        registerNewCourierAccountAndGetResponse(courierAccount);
        Response loginAccountResponse = loginCourierAndGetResponse(courierAccount);
        String courierId = getCourierAccountIdFromLoginResponse(loginAccountResponse);
        acceptOrderAndGetResponse(orderId, courierId);
        Response acceptedOrderresponse = acceptOrderAndGetResponse(orderId, courierId);
        acceptedOrderresponse.then().assertThat().statusCode(409).and().body("message", equalTo("Этот заказ уже в работе"));
        deleteTestDataFromDB(courierAccount);

    }
}
