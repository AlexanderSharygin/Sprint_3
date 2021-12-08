package ru.praktikum_services.qa_scooter.tests;

import com.google.gson.annotations.Expose;
import org.apache.commons.lang3.RandomStringUtils;

public class CourierAccount {

    private String login=null;
    private String password=null;
    private String firstName=null;
    public CourierAccount(boolean isLoginNull, boolean isPasswordNull, boolean isFirstNameNull)
    {
        if (!isLoginNull) {
            login = RandomStringUtils.randomAlphabetic(10);
        }
        if (!isPasswordNull)
        {
            password = RandomStringUtils.randomAlphabetic(10);
        }

        if (!isFirstNameNull)
        {
            firstName=RandomStringUtils.randomAlphabetic(10);
        }


    }

    public String getLogin()
    {
        return  login;
    }
    public void setLogin(String login)
    {
        this.login=login;
    }
    public String getPassword()
    {
        return  password;
    }
    public String getFirstNamed()
    {
        return  firstName;
    }
}
