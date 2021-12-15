package ru.praktikum_services.qa_scooter.tests;

import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;
import ru.praktikum_services.qa_scooter.model.Order;
import ru.praktikum_services.qa_scooter.model.RemoveTestDataException;
import static org.hamcrest.Matchers.equalTo;
import static ru.praktikum_services.qa_scooter.model.OrderActions.*;

@Feature("Orders management")
@Story("Gets single order")
public class GetOrderTest {

    @Test
    @DisplayName("Get orders by correct TrackNumber")

    public void getOrderByCorrectTrackNumberSuccess() throws RemoveTestDataException
    {
        Order order = new Order(new String[]{"BLACK"},4);
        Response createdOrderResponse = createNewOrderAndGetResponse(order);
        String orderTrackNumber = getOrderTrackNumberFromCreatedOrderResponse(createdOrderResponse);
        Response getOrderResponse = getOrderByTrackNumberAndGetResponse(orderTrackNumber);
        getOrderResponse.then().assertThat().statusCode(200).and().body("order.track", equalTo(Integer.valueOf(orderTrackNumber)));
        cancelOrderByTrackNumberAndGetResponse(orderTrackNumber);
    }

    @Test
    @DisplayName("Get orders by empty TrackNumber")
    public void getOrderByEmptyTrackNumberBadRequest()
    {
        Response getOrderResponse = getOrderByTrackNumberAndGetResponse("");
        getOrderResponse.then().assertThat().statusCode(400).and().body("message", equalTo("Недостаточно данных для поиска"));
    }

    @Test
    @DisplayName("Get orders by wrong TrackNumber")
    public void getOrderByWrongTrackNumberNotFound()
    {
        int id  = (1000000 + (int) (Math.random() * 2000000));
        Response getOrderResponse = getOrderByTrackNumberAndGetResponse(String.valueOf(id));
        getOrderResponse.then().assertThat().statusCode(404).and().body("message", equalTo("Заказ не найден"));
    }

}
