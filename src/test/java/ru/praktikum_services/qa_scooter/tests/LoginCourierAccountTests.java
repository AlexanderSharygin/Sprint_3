package ru.praktikum_services.qa_scooter.tests;

import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.AfterClass;
import org.junit.Test;
import ru.praktikum_services.qa_scooter.model.CourierAccount;
import ru.praktikum_services.qa_scooter.model.CourierAccountActions;
import ru.praktikum_services.qa_scooter.model.RemoveTestDataException;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class LoginCourierAccountTests {



    private static CourierAccountActions courierAccountActions = new CourierAccountActions();

    @Test
    public void loginCourierAccountCorrectCreditsSuccess() {

        CourierAccount testAccount = new CourierAccount(false, false, true);
        courierAccountActions.registerNewCourierAccountAndGetResponse(testAccount);
        Response response = courierAccountActions.loginCourierAndGetResponse(testAccount);
        response.then().assertThat().statusCode(200).and().body("id", notNullValue());

    }
    @Test
    public void loginCourierAccountWithoutPasswordBadRequest() {

        CourierAccount testAccount = new CourierAccount(false, false, true);
        courierAccountActions.registerNewCourierAccountAndGetResponse(testAccount);
        testAccount.setPassword("");
        Response response = courierAccountActions.loginCourierAndGetResponse(testAccount);
        response.then().assertThat().statusCode(400).and().body("message", equalTo("Недостаточно данных для входа"));

    }
    @Test
    public void loginCourierAccountWithoutUserNameBadRequest() throws CloneNotSupportedException {

        CourierAccount testAccount = new CourierAccount(false, false, true);
        courierAccountActions.registerNewCourierAccountAndGetResponse(testAccount);
        testAccount.setUsername("");
        Response response = courierAccountActions.loginCourierAndGetResponse(testAccount);
        response.then().assertThat().statusCode(400).and().body("message", equalTo("Недостаточно данных для входа"));

    }

    @Test
    public void loginCourierAccountWithWrongPasswordBadRequest()  {

        CourierAccount testAccount = new CourierAccount(false, false, true);
        courierAccountActions.registerNewCourierAccountAndGetResponse(testAccount);
        testAccount.setPassword(RandomStringUtils.randomAlphabetic(10));
        Response response = courierAccountActions.loginCourierAndGetResponse(testAccount);
        response.then().assertThat().statusCode(404).and().body("message", equalTo("Учетная запись не найдена"));

    }
    @Test
    public void loginCourierAccountWithWrongUserNameBadRequest()  {

        CourierAccount testAccount = new CourierAccount(false, false, true);
        courierAccountActions.registerNewCourierAccountAndGetResponse(testAccount);
        testAccount.setUsername(RandomStringUtils.randomAlphabetic(10));
        Response response = courierAccountActions.loginCourierAndGetResponse(testAccount);
        response.then().assertThat().statusCode(404).and().body("message", equalTo("Учетная запись не найдена"));

    }
    @AfterClass
    public static void cleanDB() throws RemoveTestDataException {
        courierAccountActions.removeAllCreatedAccounts();
    }
}
