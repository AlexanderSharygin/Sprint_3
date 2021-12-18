package ru.praktikum_services.qa_scooter.tests;

import io.qameta.allure.*;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.praktikum_services.qa_scooter.model.CourierAccount;
import ru.praktikum_services.qa_scooter.model.CourierAccountClient;
import ru.praktikum_services.qa_scooter.model.CourierCredentials;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@Feature("Courier accounts management")
@Story("Login under Courier account")
public class LoginCourierAccountTests {

    private final CourierAccountClient courierAccountClient = new CourierAccountClient();
    private int courierId=0;
    private CourierAccount courierAccount;

    @Before
    public void setup() {
        courierAccount = CourierAccount.getRandom();
        courierAccountClient.registerNewCourierAccount(courierAccount);
    }

    @Test
    @DisplayName("Login with correct username/password")
    public void loginCourierAccountCorrectCreditsSuccess() {

        CourierCredentials credentials = new CourierCredentials(courierAccount.getLogin(), courierAccount.getPassword());
        ValidatableResponse response = courierAccountClient.loginCourierAccount(credentials);
        response.assertThat().statusCode(200).and().body("id", notNullValue());
        courierId = response.assertThat().extract().path("id");
    }
    @Test
    @DisplayName("Login without password")
    public void loginCourierAccountWithoutPasswordBadRequest()  {

        CourierCredentials credentials = new CourierCredentials(courierAccount.getLogin(), null);
        ValidatableResponse response = courierAccountClient.loginCourierAccount(credentials);
        response.assertThat().statusCode(400).and().body("message", equalTo("Недостаточно данных для входа"));
        credentials.setPassword(courierAccount.getPassword());
        courierId = courierAccountClient.loginCourierAccount(credentials).assertThat().statusCode(200).extract().path("id");
    }

    @Test
    @DisplayName("Login with Empty password")
    public void loginCourierAccountWithEmptyPasswordBadRequest()  {

        CourierCredentials credentials = new CourierCredentials(courierAccount.getLogin(), "");
        ValidatableResponse response = courierAccountClient.loginCourierAccount(credentials);
        response.assertThat().statusCode(400).and().body("message", equalTo("Недостаточно данных для входа"));

        credentials.setPassword(courierAccount.getPassword());
        courierId = courierAccountClient.loginCourierAccount(credentials).assertThat().statusCode(200).extract().path("id");
    }

    @Test
    @DisplayName("Login without username")
    public void loginCourierAccountWithoutUserNameBadRequest()  {

        CourierCredentials credentials = new CourierCredentials(null, courierAccount.getPassword());
        ValidatableResponse response = courierAccountClient.loginCourierAccount(credentials);
        response.assertThat().statusCode(400).and().body("message", equalTo("Недостаточно данных для входа"));

        credentials.setLogin(courierAccount.getLogin());
        courierId = courierAccountClient.loginCourierAccount(credentials).assertThat().statusCode(200).extract().path("id");
    }

    @Test
    @DisplayName("Login with Empty username")
    public void loginCourierAccountWithEmptyUserNameBadRequest()  {

        CourierCredentials credentials = new CourierCredentials("", courierAccount.getPassword());
        ValidatableResponse response = courierAccountClient.loginCourierAccount(credentials);
        response.assertThat().statusCode(400).and().body("message", equalTo("Недостаточно данных для входа"));

        credentials.setLogin(courierAccount.getLogin());
        courierId = courierAccountClient.loginCourierAccount(credentials).assertThat().statusCode(200).extract().path("id");
    }

    @Test
    @DisplayName("Login with wrong password")
    public void loginCourierAccountWithWrongPasswordBadRequest()  {

        CourierCredentials credentials = new CourierCredentials(courierAccount.getLogin(), RandomStringUtils.randomAlphabetic(10));
        ValidatableResponse response = courierAccountClient.loginCourierAccount(credentials);
        response.assertThat().statusCode(404).and().body("message", equalTo("Учетная запись не найдена"));

        credentials.setPassword(courierAccount.getPassword());
        courierId = courierAccountClient.loginCourierAccount(credentials).assertThat().statusCode(200).extract().path("id");


    }

    @Test
    @DisplayName("Login with not existed username")
    public void loginCourierAccountWithWrongUserNameBadRequest()  {

        CourierCredentials credentials = new CourierCredentials(RandomStringUtils.randomAlphabetic(10), courierAccount.getPassword());
        ValidatableResponse response = courierAccountClient.loginCourierAccount(credentials);
        response.assertThat().statusCode(404).and().body("message", equalTo("Учетная запись не найдена"));

        credentials.setLogin(courierAccount.getLogin());
        courierId = courierAccountClient.loginCourierAccount(credentials).assertThat().statusCode(200).extract().path("id");
    }

    @After
    public void tearDown() {
        courierAccountClient.deleteCourierAccount(String.valueOf(courierId));
    }

}
