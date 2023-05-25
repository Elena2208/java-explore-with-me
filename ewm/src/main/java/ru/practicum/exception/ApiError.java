package ru.practicum.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ApiError {
    private String message;
    private String reason;
    private String status;
    private String timestamp;
}