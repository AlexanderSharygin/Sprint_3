package ru.praktikum_services.qa_scooter.tests;

import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import ru.praktikum_services.qa_scooter.model.CourierAccount;
import ru.praktikum_services.qa_scooter.model.CourierAccountActions;
import ru.praktikum_services.qa_scooter.model.RemoveTestDataException;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class LoginCourierTests {



    private static CourierAccountActions courierAccountActions = new CourierAccountActions();

    @Test
    public void loginCourierAccountCorrectCreditsSuccess() throws CloneNotSupportedException {

        CourierAccount testAccount = new CourierAccount(false, false, true);
       courierAccountActions.registerNewCourierLoginAndGetResponse(testAccount);
        Response response = courierAccountActions.loginCourierAndGetResponse(testAccount);
        response.then().assertThat().statusCode(200).and().body("id", notNullValue());

    }
    @Test
    public void loginCourierAccountWithoutPasswordBadRequest() throws CloneNotSupportedException {

        CourierAccount testAccount = new CourierAccount(false, false, true);
        courierAccountActions.registerNewCourierLoginAndGetResponse(testAccount);
        testAccount.setPassword("");
        Response response = courierAccountActions.loginCourierAndGetResponse(testAccount);
        response.then().assertThat().statusCode(400).and().body("message", equalTo("Недостаточно данных для входа"));

    }
    @Test
    public void loginCourierAccountWithoutUserNameBadRequest() throws CloneNotSupportedException {

        CourierAccount testAccount = new CourierAccount(false, false, true);
        courierAccountActions.registerNewCourierLoginAndGetResponse(testAccount);
        testAccount.setLogin("");
        Response response = courierAccountActions.loginCourierAndGetResponse(testAccount);
        response.then().assertThat().statusCode(400).and().body("message", equalTo("Недостаточно данных для входа"));

    }

    @Test
    public void loginCourierAccountWithWrongPasswordBadRequest() throws CloneNotSupportedException {

        CourierAccount testAccount = new CourierAccount(false, false, true);
        courierAccountActions.registerNewCourierLoginAndGetResponse(testAccount);
        testAccount.setPassword(RandomStringUtils.randomAlphabetic(10));
        Response response = courierAccountActions.loginCourierAndGetResponse(testAccount);
        response.then().assertThat().statusCode(404).and().body("message", equalTo("Учетная запись не найдена"));

    }
    @Test
    public void loginCourierAccountWithWrongUserNameBadRequest() throws CloneNotSupportedException {

        CourierAccount testAccount = new CourierAccount(false, false, true);
        courierAccountActions.registerNewCourierLoginAndGetResponse(testAccount);
        testAccount.setLogin(RandomStringUtils.randomAlphabetic(10));
        Response response = courierAccountActions.loginCourierAndGetResponse(testAccount);
        response.then().assertThat().statusCode(404).and().body("message", equalTo("Учетная запись не найдена"));

    }
    @AfterClass
    public static void cleanDB() throws RemoveTestDataException {
        courierAccountActions.removeAllCreatedAccounts();
    }
}
