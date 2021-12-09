package ru.praktikum_services.qa_scooter.tests;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.Test;
import ru.praktikum_services.qa_scooter.model.CourierAccount;
import ru.praktikum_services.qa_scooter.model.CourierAccountActions;
import static org.hamcrest.Matchers.equalTo;




public class DeleteCourierAccountTests {

    private static CourierAccountActions courierAccountActions = new CourierAccountActions();

    @Test
    public void deleteCourierAccountByCorrectIdSuccess()  {

        CourierAccount testAccount = new CourierAccount(false, false, true);
        courierAccountActions.registerNewCourierAccountAndGetResponse(testAccount);
        Response loginResponse = courierAccountActions.loginCourierAndGetResponse(testAccount);
        String accountId = courierAccountActions.getCourierAccountIdFromLoginResponse(loginResponse);
        Response deleteResponse = courierAccountActions.deleteCourierAndGetResponse(accountId);
        deleteResponse.then().assertThat().statusCode(200).and().body("ok", equalTo(true));

    }
    @Test
    public void deleteCourierAccountWithEmptyIdBadRequest() {


        Response response = courierAccountActions.deleteCourierAndGetResponse("");
        response.then().assertThat().statusCode(400).and().body("message", equalTo( "Недостаточно данных для удаления курьера"));

    }
    @Test
    public void deleteCourierAccountWithWrongIdBadRequest() {

        Integer id  = (1000000 + (int) (Math.random() * 2000000));
        Response response = courierAccountActions.deleteCourierAndGetResponse(id.toString());
        response.then().assertThat().statusCode(404).and().body("message", equalTo( "Курьера с таким id нет."));

    }


}
