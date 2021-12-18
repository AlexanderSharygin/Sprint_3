package ru.praktikum_services.qa_scooter.tests;

import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.praktikum_services.qa_scooter.model.CourierAccount;

import ru.praktikum_services.qa_scooter.model.CourierAccountClient;
import ru.praktikum_services.qa_scooter.model.CourierCredentials;

import static org.hamcrest.Matchers.equalTo;

@Feature("Courier accounts management")
@Story("Create new account")
public class CreateCourierAccountsTests {

    private final CourierAccountClient courierAccountClient = new CourierAccountClient();
    private int courierId=0;
    private CourierAccount courierAccount;

    @Before
    public void setup() {
        courierAccount = CourierAccount.getRandom();
    }

    @Test
    @DisplayName("Register account with unique  username and with password")
    public void registerNewCourierAccountWithUniqueUsernameSuccess()  {

        CourierCredentials credentials = new CourierCredentials(courierAccount.getLogin(), courierAccount.getPassword());
        ValidatableResponse response = courierAccountClient.registerNewCourierAccount(courierAccount);
        response.assertThat().statusCode(201).and().body("ok", equalTo(true));
        courierId = courierAccountClient.loginCourierAccount(credentials).assertThat().statusCode(200).extract().path("id");

    }

    @Test
    @DisplayName("Register account with unique username and without password")
    public void registerNewCourierAccountWithEmptyPasswordBadRequest() {

        courierAccount.setPassword(null);
        ValidatableResponse response = courierAccountClient.registerNewCourierAccount(courierAccount);
        response.assertThat().statusCode(400).and().body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Register account with empty username and with password")
    public void registerNewCourierAccountWithEmptyUsernameBadRequest() {
        courierAccount.setLogin(null);
        ValidatableResponse response = courierAccountClient.registerNewCourierAccount(courierAccount);
        response.assertThat().statusCode(400).and().body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }


    @Test
    @DisplayName("Register account with empty FirstName and with unique username/password")
    public void registerNewCourierAccountWithEmptyFirstNameSuccess()  {

        courierAccount.setFirstName(null);
        CourierCredentials credentials = new CourierCredentials(courierAccount.getLogin(), courierAccount.getPassword());
        ValidatableResponse response = courierAccountClient.registerNewCourierAccount(courierAccount);
        response.assertThat().statusCode(201).and().body("ok", equalTo(true));
        courierId = courierAccountClient.loginCourierAccount(credentials).assertThat().statusCode(200).extract().path("id");

    }

    @Test
    @DisplayName("Register account with duplicated username and with password")
    public void registerNewCourierAccountWithDuplicatedUsernameConflict() {

        CourierCredentials credentials = new CourierCredentials(courierAccount.getLogin(), courierAccount.getPassword());
        courierAccountClient.registerNewCourierAccount(courierAccount);
        CourierAccount secondAccount = CourierAccount.getRandom();
        secondAccount.setLogin(courierAccount.getLogin());
        ValidatableResponse secondAccountResponse = courierAccountClient.registerNewCourierAccount(secondAccount);
        secondAccountResponse.assertThat().statusCode(409).and().body("message", equalTo("Этот логин уже используется"));

        courierId = courierAccountClient.loginCourierAccount(credentials).assertThat().statusCode(200).extract().path("id");


    }

    @After
    public void tearDown() {
        courierAccountClient.deleteCourierAccount(String.valueOf(courierId));
    }

}

