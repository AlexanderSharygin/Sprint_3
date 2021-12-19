package ru.praktikum_services.qa_scooter.model;


import org.apache.commons.lang3.RandomStringUtils;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class Order {

    private final String firstName;
    private final String lastName;
    private final String address;
    private final int metroStation;
    private final String phone;
    private final int rentTime;
    private final String deliveryDate;
    private final String comment;
    private final String[] color;

    public Order(String[] color, int metroStationId) {
        this.color = color;
        this.metroStation = metroStationId;
        this.firstName = RandomStringUtils.randomAlphabetic(10);
        this.lastName = RandomStringUtils.randomAlphabetic(10);
        this.address = RandomStringUtils.randomAlphabetic(10);
        this.phone = "+" + RandomStringUtils.randomNumeric(10);
        this.deliveryDate = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
        this.comment = RandomStringUtils.randomAlphabetic(20);
        this.rentTime = (int) (Math.random() * 6) + 1;

    }

}
