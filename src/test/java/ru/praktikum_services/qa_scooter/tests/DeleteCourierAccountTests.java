package ru.praktikum_services.qa_scooter.tests;

import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.Test;
import ru.praktikum_services.qa_scooter.model.CourierAccount;
import ru.praktikum_services.qa_scooter.model.CourierAccountClient;
import ru.praktikum_services.qa_scooter.model.CourierCredentials;
import static org.hamcrest.Matchers.equalTo;



@Feature("Courier accounts management")
@Story("Delete account")

public class DeleteCourierAccountTests {

    private final CourierAccountClient courierAccountClient= new CourierAccountClient();



    @Test
    @DisplayName("Delete courier account by correct ID")
    public void deleteCourierAccountByCorrectIdSuccess()  {

        CourierAccount testAccount = CourierAccount.getRandom();
        CourierCredentials credentials = new CourierCredentials(testAccount.getLogin(),testAccount.getPassword());
        courierAccountClient.registerNewCourierAccount(testAccount);
        courierAccountClient.loginCourierAccount(credentials);
        int courierId = courierAccountClient.loginCourierAccount(credentials).extract().path("id");
        ValidatableResponse response = courierAccountClient.deleteCourierAccount(String.valueOf(courierId));
        response.assertThat().statusCode(200).and().body("ok", equalTo(true));

    }
    @Test
    @DisplayName("Delete courier account without ID")
    public void deleteCourierAccountWithEmptyIdBadRequest() {

        ValidatableResponse response = courierAccountClient.deleteCourierAccount("");
        response.assertThat().statusCode(400).and().body("message", equalTo( "Недостаточно данных для удаления курьера"));

    }
    @Test
    @DisplayName("Delete courier account by wrong ID")
    public void deleteCourierAccountWithWrongIdBadRequest() {

        int courierId  = (1000000 + (int) (Math.random() * 2000000));
        ValidatableResponse response = courierAccountClient.deleteCourierAccount(String.valueOf(courierId));
        response.assertThat().statusCode(404).and().body("message", equalTo( "Курьера с таким id нет."));

    }


}
