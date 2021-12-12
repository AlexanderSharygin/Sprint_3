package ru.praktikum_services.qa_scooter.model;

public class CompleteOrderException extends Throwable {
    public CompleteOrderException(String message) {
        super(message);
    }
}