package ru.practicum.ewmmain.exception;

public class ErrorResponse {
    // название ошибки
    private final String status;
    private final String error;


    public ErrorResponse(String status, String error) {
        this.status = status;
        this.error = error;
    }

    // геттеры необходимы, чтобы Spring Boot мог получить значения полей
    public String getError() {
        return error;
    }

    public String getStatus() {
        return status;
    }
}