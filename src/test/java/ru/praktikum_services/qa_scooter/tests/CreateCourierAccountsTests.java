package ru.praktikum_services.qa_scooter.tests;

import io.restassured.response.Response;
import org.junit.Test;
import ru.praktikum_services.qa_scooter.model.CourierAccount;
import static ru.praktikum_services.qa_scooter.model.CourierAccountActions.*;
import ru.praktikum_services.qa_scooter.model.RemoveTestDataException;
import static org.hamcrest.Matchers.equalTo;

public class CreateCourierAccountsTests {


    @Test
    public void registerNewCourierAccountWithUniqueUsernameSuccess() throws RemoveTestDataException {

        CourierAccount testAccount = new CourierAccount(false, false, false);
        Response response = registerNewCourierAccountAndGetResponse(testAccount);
        response.then().assertThat().statusCode(201).and().body("ok", equalTo(true));
        if (response.statusCode()==201)
        {
            deleteTestDataFromDB(testAccount);
        }
    }

    @Test
    public void registerNewCourierAccountWithEmptyPasswordBadRequest()  {

        CourierAccount testAccount = new CourierAccount(false, true, false);
        Response response = registerNewCourierAccountAndGetResponse(testAccount);
        response.then().assertThat().statusCode(400).and().body("message", equalTo("Недостаточно данных для создания учетной записи"));

    }

    @Test
    public void registerNewCourierAccountWithEmptyUsernameBadRequest()  {

        CourierAccount testAccount = new CourierAccount(true, false, false);
        Response response = registerNewCourierAccountAndGetResponse(testAccount);
        response.then().assertThat().statusCode(400).and().body("message", equalTo("Недостаточно данных для создания учетной записи"));

    }

    @Test
    public void registerNewCourierAccountWithEmptyFirstNameSuccess() throws RemoveTestDataException {

        CourierAccount testAccount = new CourierAccount(false, false, true);
        Response response = registerNewCourierAccountAndGetResponse(testAccount);
        response.then().assertThat().statusCode(201).and().body("ok", equalTo(true));
        if (response.statusCode()==201)
        {
            deleteTestDataFromDB(testAccount);
        }

    }

    @Test
    public void registerNewCourierAccountWithDuplicatedUsernameBadRequest()  {

        CourierAccount testAccount = new CourierAccount(false, false, false);
        registerNewCourierAccountAndGetResponse(testAccount);
        CourierAccount accountWithTheSameLogin = new CourierAccount(false, false, false);
        accountWithTheSameLogin.setUsername(testAccount.getUsername());
        Response response = registerNewCourierAccountAndGetResponse(accountWithTheSameLogin);
        response.then().assertThat().statusCode(409).and().body("message", equalTo("Этот логин уже используется"));
    }


}

