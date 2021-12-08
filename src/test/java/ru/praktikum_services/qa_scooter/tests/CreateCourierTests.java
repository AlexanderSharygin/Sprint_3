package ru.praktikum_services.qa_scooter.tests;

import io.restassured.response.Response;
import org.junit.Test;
import static org.hamcrest.Matchers.equalTo;

public class CreateCourierTests {

    @Test
    public void registerNewCourierLoginWithUniqueLoginSuccess()
    {
        scooterRegisterCourier scr= new scooterRegisterCourier();
        CourierAccount ca = new CourierAccount(false, false,false);
        Response response = scr.createNewCourierLoginAndCheckResponse(ca);
        response.then().assertThat().statusCode(201).and().body("ok", equalTo(true));

    }
    @Test
    public void registerNewCourierLoginWithEmptyPasswordBadRequest()
    {
        scooterRegisterCourier scr= new scooterRegisterCourier();
        CourierAccount ca = new CourierAccount(false, true,false);
        Response response = scr.createNewCourierLoginAndCheckResponse(ca);
        response.then().assertThat().statusCode(400).and().body("message", equalTo("Недостаточно данных для создания учетной записи"));

    }
    @Test
    public void registerNewCourierLoginWithEmptyLoginBadRequest()
    {
        scooterRegisterCourier scr= new scooterRegisterCourier();
        CourierAccount ca = new CourierAccount(true, false,false);
        Response response = scr.createNewCourierLoginAndCheckResponse(ca);
        response.then().assertThat().statusCode(400).and().body("message", equalTo("Недостаточно данных для создания учетной записи"));

    }

    @Test
    public void registerNewCourierLoginWithEmptyFirstNameSuccess()
    {
        scooterRegisterCourier scr= new scooterRegisterCourier();
        CourierAccount ca = new CourierAccount(false, false,true);
        Response response = scr.createNewCourierLoginAndCheckResponse(ca);
        response.then().assertThat().statusCode(201).and().body("ok", equalTo(true));

    }

    @Test
    public void registerNewCourierLoginWithDuplicatedLoginBadRequest()
    {
        scooterRegisterCourier scr= new scooterRegisterCourier();
        CourierAccount ca = new CourierAccount(false, false,false);
        scr.createNewCourierLoginAndCheckResponse(ca);
        CourierAccount ca2 = new CourierAccount(false, false,false);
        ca2.setLogin(ca.getLogin());
        Response response = scr.createNewCourierLoginAndCheckResponse(ca2);
        response.then().assertThat().statusCode(409).and().body("message", equalTo("Этот логин уже используется"));
    }
}
