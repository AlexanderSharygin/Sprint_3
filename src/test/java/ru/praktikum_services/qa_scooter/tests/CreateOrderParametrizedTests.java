package ru.praktikum_services.qa_scooter.tests;

import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.praktikum_services.qa_scooter.model.Order;

import static org.hamcrest.Matchers.notNullValue;
import static ru.praktikum_services.qa_scooter.model.OrderActions.*;


@Feature("Orders management")
@Story("Create new order")
@RunWith(Parameterized.class)
public class CreateOrderParametrizedTests {

    private final String[] color;
    private final int expectedStatus;
    private final String expectedBody;

    public CreateOrderParametrizedTests(String [] color, int expectedStatus, String expectedBody)
    {
        this.color=color;
        this.expectedStatus = expectedStatus;
        this.expectedBody= expectedBody;

    }

    @Parameterized.Parameters
    public static Object[][] getDataForTests() {
        return new Object[][]{
                {new String[]{"BLACK"}, 201, "track"},
                {new String[]{"GREY"}, 201, "track"},
                {new String[]{"BLACK, GREY"}, 201, "track"},
                {new String[]{"BLACK, GREY"}, 201, "track"},
                {null, 201, "track"}
        };
    }


    @Test
    @DisplayName("Create orders with different Colors value")
    public void createNewOrderSuccess()  {
        Order order = new Order(color, 4);
      Response createResponse = createNewOrderAndGetResponse(order);
        createResponse.then().assertThat().statusCode(expectedStatus).and().body(expectedBody, notNullValue());
        if (createResponse.statusCode()==201)
        {
            JsonPath jsonPath = new JsonPath(createResponse.thenReturn().getBody().asString());
            String orderTrackNumber = jsonPath.getString("track");
            cancelOrderByTrackNumberAndGetResponse(orderTrackNumber);

        }
    }




}
