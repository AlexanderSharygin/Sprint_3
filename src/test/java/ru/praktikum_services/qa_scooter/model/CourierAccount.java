package ru.praktikum_services.qa_scooter.model;

import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.RandomStringUtils;

public class CourierAccount  {
    @SerializedName("login")
    private String username =null;
    private String password=null;
    private String firstName=null;

    public CourierAccount(boolean isLoginNull, boolean isPasswordNull, boolean isFirstNameNull)
    {
        if (!isLoginNull) {
            username = RandomStringUtils.randomAlphabetic(10);
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

    public String getUsername()
    {
        return username;
    }
    public String getPassword() { return password; }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public void setPassword(String password)
    {
        this.password=password;
    }



}
