package ru.praktikum_services.qa_scooter.model;


import com.google.gson.annotations.SerializedName;

public class Order {
    @SerializedName("firstName")
    private final String FIRSTNAME = "Test";
    @SerializedName("lastName")
    private final String LASTNAME = "Test";
    @SerializedName("address")
    private final String ADDRESS = "Konoha, 142 apt.";
    @SerializedName("metroStation")
    private final int METROSTATION = 4;
    @SerializedName("phone")
    private final String PHONE = "+7 800 355 35 35";
    @SerializedName("rentTime")
    private final int RENTTIME = 5;
    @SerializedName("deliveryDate")
    private final String DELIVERYDATE = "2020-06-06";
    @SerializedName("comment")
    private final String COMMENT = "Test order";
    String [] color;

    public Order (String [] color)
    {
        this.color=color;

    }

}
