package ru.praktikum_services.qa_scooter.tests;

import io.restassured.response.Response;
import org.junit.Test;
import ru.praktikum_services.qa_scooter.model.CourierAccount;

import static org.hamcrest.Matchers.equalTo;
import static ru.praktikum_services.qa_scooter.model.CourierAccountActions.*;



public class DeleteCourierAccountTests {



    @Test
    public void deleteCourierAccountByCorrectIdSuccess()  {

        CourierAccount testAccount = new CourierAccount(false, false, true);
        registerNewCourierAccountAndGetResponse(testAccount);
        Response loginResponse = loginCourierAndGetResponse(testAccount);
        String accountId =getCourierAccountIdFromLoginResponse(loginResponse);
        Response deleteResponse = deleteCourierAndGetResponse(accountId);
        deleteResponse.then().assertThat().statusCode(200).and().body("ok", equalTo(true));

    }
    @Test
    public void deleteCourierAccountWithEmptyIdBadRequest() {


        Response response = deleteCourierAndGetResponse("");
        response.then().assertThat().statusCode(400).and().body("message", equalTo( "Недостаточно данных для удаления курьера"));

    }
    @Test
    public void deleteCourierAccountWithWrongIdBadRequest() {

        int id  = (1000000 + (int) (Math.random() * 2000000));
        Response response = deleteCourierAndGetResponse(Integer.toString(id));
        response.then().assertThat().statusCode(404).and().body("message", equalTo( "Курьера с таким id нет."));

    }


}
