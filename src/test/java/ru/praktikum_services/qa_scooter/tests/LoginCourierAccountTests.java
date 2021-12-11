package ru.praktikum_services.qa_scooter.tests;

import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import ru.praktikum_services.qa_scooter.model.CourierAccount;
import ru.praktikum_services.qa_scooter.model.RemoveTestDataException;
import static ru.praktikum_services.qa_scooter.model.CourierAccountActions.*;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class LoginCourierAccountTests {


    @Test
    public void loginCourierAccountCorrectCreditsSuccess() throws RemoveTestDataException {

        CourierAccount testAccount = new CourierAccount(false, false, true);
       registerNewCourierAccountAndGetResponse(testAccount);
        Response response = loginCourierAndGetResponse(testAccount);
        response.then().assertThat().statusCode(200).and().body("id", notNullValue());
        if (response.statusCode()==201)
        {
            deleteTestDataFromDB(testAccount);
        }
    }

    @Test
    public void loginCourierAccountWithoutPasswordBadRequest() throws RemoveTestDataException {

        CourierAccount testAccount = new CourierAccount(false, false, true);
        registerNewCourierAccountAndGetResponse(testAccount);
        String correctPassword = testAccount.getPassword();
        testAccount.setPassword("");
        Response loginResponse = loginCourierAndGetResponse(testAccount);
        loginResponse.then().assertThat().statusCode(400).and().body("message", equalTo("Недостаточно данных для входа"));
        testAccount.setPassword(correctPassword);
        deleteTestDataFromDB(testAccount);
    }

    @Test
    public void loginCourierAccountWithoutUserNameBadRequest() throws RemoveTestDataException {

        CourierAccount testAccount = new CourierAccount(false, false, true);
        registerNewCourierAccountAndGetResponse(testAccount);
        String correctUsername = testAccount.getUsername();
        testAccount.setUsername("");
        Response loginResponse = loginCourierAndGetResponse(testAccount);
        loginResponse.then().assertThat().statusCode(400).and().body("message", equalTo("Недостаточно данных для входа"));
        testAccount.setUsername(correctUsername);
        deleteTestDataFromDB(testAccount);

    }

    @Test
    public void loginCourierAccountWithWrongPasswordBadRequest() throws RemoveTestDataException {

        CourierAccount testAccount = new CourierAccount(false, false, true);
        registerNewCourierAccountAndGetResponse(testAccount);
        String correctPassword = testAccount.getPassword();
        testAccount.setPassword(RandomStringUtils.randomAlphabetic(10));
        Response loginResponse = loginCourierAndGetResponse(testAccount);
        loginResponse.then().assertThat().statusCode(404).and().body("message", equalTo("Учетная запись не найдена"));
        testAccount.setPassword(correctPassword);
        deleteTestDataFromDB(testAccount);
    }

    @Test
    public void loginCourierAccountWithWrongUserNameBadRequest() throws RemoveTestDataException {

        CourierAccount testAccount = new CourierAccount(false, false, true);
        registerNewCourierAccountAndGetResponse(testAccount);
        String correctUsername = testAccount.getUsername();
        testAccount.setUsername(RandomStringUtils.randomAlphabetic(10));
        Response loginResponse = loginCourierAndGetResponse(testAccount);
        loginResponse.then().assertThat().statusCode(404).and().body("message", equalTo("Учетная запись не найдена"));
        testAccount.setUsername(correctUsername);
        deleteTestDataFromDB(testAccount);
    }

}
