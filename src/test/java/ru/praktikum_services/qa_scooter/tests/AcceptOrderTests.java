package ru.praktikum_services.qa_scooter.tests;


import io.qameta.allure.Feature;
import io.qameta.allure.Story;

@Feature("Orders management")
@Story("Accept order")
public class AcceptOrderTests {
   /* @Test
    @DisplayName("Accept new order with correct order ID for courier with correct ID")
    public void acceptNewCorrectOrderSuccess() throws RemoveTestDataException {
        Order order = new Order(new String[]{"BLACK"},4);
        CourierAccount courierAccount = new CourierAccount(false, false, false);
        Response createdOrderResponse = createNewOrderAndGetResponse(order);
        String orderTrackNumber = getOrderTrackNumberFromCreatedOrderResponse(createdOrderResponse);
        String orderId = getOrderIdByOrderTrackNumber(orderTrackNumber);
        registerNewCourierAccountAndGetResponse(courierAccount);
        Response loginAccountResponse = loginCourierAndGetResponse(courierAccount);
        String courierId = getCourierAccountIdFromLoginResponse(loginAccountResponse);
        Response acceptOrderResponse = acceptOrderByIdAndGetResponse(orderId, courierId);
        acceptOrderResponse.then().assertThat().statusCode(200).and().body("ok", equalTo(true));
        deleteTestDataFromDB(courierAccount);

    }
    @Test
    @DisplayName("Accept new order with empty order ID for courier with correct ID")
    public void acceptOrderWithoutOrderNumberBadRequest() throws RemoveTestDataException {


        CourierAccount courierAccount = new CourierAccount(false, false, false);
        registerNewCourierAccountAndGetResponse(courierAccount);
        Response loginAccountResponse = loginCourierAndGetResponse(courierAccount);
        String courierId = getCourierAccountIdFromLoginResponse(loginAccountResponse);
        Response acceptOrderResponse = acceptOrderByIdAndGetResponse("", courierId);
        acceptOrderResponse.then().assertThat().statusCode(400).and().body("message", equalTo("Недостаточно данных для поиска"));
        deleteTestDataFromDB(courierAccount);

    }
    @Test
    @DisplayName("Accept new order with correct order ID without courier")
    public void acceptNewCorrectOrderWithoutCourierIdConflict()  {
        Order order = new Order(new String[]{"BLACK"},4);
        Response createdOrderResponse = createNewOrderAndGetResponse(order);
        String orderTrackNumber = getOrderTrackNumberFromCreatedOrderResponse(createdOrderResponse);
        String orderId = getOrderIdByOrderTrackNumber(orderTrackNumber);
        String courierId =  "";
        Response acceptedOrderResponse = acceptOrderByIdAndGetResponse(orderId, courierId);
        acceptedOrderResponse.then().assertThat().statusCode(400).and().body("message", equalTo("Недостаточно данных для поиска"));


    }

    @Test
    @DisplayName("Accept new order with wrong order ID for courier with correct ID")
   public void acceptOrderWithWrongOrderNumberNotFound() throws RemoveTestDataException {

        String orderId =  String.valueOf(1000000 + (int) (Math.random() * 2000000));
        CourierAccount courierAccount = new CourierAccount(false, false, false);
        registerNewCourierAccountAndGetResponse(courierAccount);
        Response loginAccountResponse = loginCourierAndGetResponse(courierAccount);
        String courierId = getCourierAccountIdFromLoginResponse(loginAccountResponse);
        Response acceptedOrderResponse = acceptOrderByIdAndGetResponse(orderId, courierId);
        acceptedOrderResponse.then().assertThat().statusCode(404).and().body("message", equalTo("Заказа с таким id не существует"));
        deleteTestDataFromDB(courierAccount);

    }

    @Test
    @DisplayName("Accept new order with correct order ID for courier with wrong ID")
    public void acceptNewCorrectOrderWithWrongCourierIdNotFound()  {
        Order order = new Order(new String[]{"BLACK"},4);
        Response createdOrderResponse = createNewOrderAndGetResponse(order);
        String orderTrackNumber = getOrderTrackNumberFromCreatedOrderResponse(createdOrderResponse);
        String orderId = getOrderIdByOrderTrackNumber(orderTrackNumber);
        String courierId =  String.valueOf(1000000 + (int) (Math.random() * 2000000));
        Response acceptedOrderResponse = acceptOrderByIdAndGetResponse(orderId, courierId);
        acceptedOrderResponse.then().assertThat().statusCode(404).and().body("message", equalTo("Курьера с таким id не существует"));
    }


    @Test
    @DisplayName("Accept already accepted order")
   public void acceptAlreadyAcceptedOrderConflict() throws RemoveTestDataException {
        Order order = new Order(new String[]{"BLACK"},4);
        CourierAccount courierAccount = new CourierAccount(false, false, false);
        Response createdOrderResponse = createNewOrderAndGetResponse(order);
        String orderTrackNumber = getOrderTrackNumberFromCreatedOrderResponse(createdOrderResponse);
        String orderId = getOrderIdByOrderTrackNumber(orderTrackNumber);
        registerNewCourierAccountAndGetResponse(courierAccount);
        Response loginAccountResponse = loginCourierAndGetResponse(courierAccount);
        String courierId = getCourierAccountIdFromLoginResponse(loginAccountResponse);
        acceptOrderByIdAndGetResponse(orderId, courierId);
        Response acceptedOrderResponse = acceptOrderByIdAndGetResponse(orderId, courierId);
        acceptedOrderResponse.then().assertThat().statusCode(409).and().body("message", equalTo("Этот заказ уже в работе"));
        deleteTestDataFromDB(courierAccount);

    }*/
}
