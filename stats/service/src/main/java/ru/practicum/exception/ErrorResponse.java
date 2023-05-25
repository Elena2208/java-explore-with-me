package ru.practicum.exception;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ErrorResponse {
    private final HttpStatus status;
    private final String reason;
    private final String message;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private final String timestamp;

    public ErrorResponse(HttpStatus status, String reason, String message, LocalDateTime timestamp) {
        this.status = status;
        this.message = message;
        this.reason = reason;
        this.timestamp = timestamp.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}
