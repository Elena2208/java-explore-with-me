package ru.practicum.exception;

import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResponse onConstraintViolationException(BadRequestException e) {
        return new ErrorResponse(HttpStatus.BAD_REQUEST,
                "Некорректный запрос.",
                e.getMessage(),
                LocalDateTime.now());
    }
}

@Getter
class ErrorResponse {
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