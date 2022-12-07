package ru.practicum.ewmmain;

import lombok.Data;

import java.util.Collection;

@Data
public class ApiError {
    private Collection<String> errors;
    private String message;
    private String reason;
    private String status;
    private String timestamp;
}
