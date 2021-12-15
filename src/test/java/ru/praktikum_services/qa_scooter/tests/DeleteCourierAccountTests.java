package ru.praktikum_services.qa_scooter.tests;

import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;
import ru.praktikum_services.qa_scooter.model.CourierAccount;

import static org.hamcrest.Matchers.equalTo;
import static ru.praktikum_services.qa_scooter.model.CourierAccountActions.*;


@Feature("Courier accounts management")
@Story("Delete account")

public class DeleteCourierAccountTests {



    @Test
    @DisplayName("Delete courier account by correct ID")
    public void deleteCourierAccountByCorrectIdSuccess()  {

        CourierAccount testAccount = new CourierAccount(false, false, true);
        registerNewCourierAccountAndGetResponse(testAccount);
        Response loginResponse = loginCourierAndGetResponse(testAccount);
        String accountId =getCourierAccountIdFromLoginResponse(loginResponse);
        Response deleteResponse = deleteCourierAndGetResponse(accountId);
        deleteResponse.then().assertThat().statusCode(200).and().body("ok", equalTo(true));

    }
    @Test
    @DisplayName("Delete courier account without ID")
    public void deleteCourierAccountWithEmptyIdBadRequest() {


        Response response = deleteCourierAndGetResponse("");
        response.then().assertThat().statusCode(400).and().body("message", equalTo( "Недостаточно данных для удаления курьера"));

    }
    @Test
    @DisplayName("Delete courier account by wrong ID")
    public void deleteCourierAccountWithWrongIdBadRequest() {

        int id  = (1000000 + (int) (Math.random() * 2000000));
        Response response = deleteCourierAndGetResponse(Integer.toString(id));
        response.then().assertThat().statusCode(404).and().body("message", equalTo( "Курьера с таким id нет."));

    }


}
