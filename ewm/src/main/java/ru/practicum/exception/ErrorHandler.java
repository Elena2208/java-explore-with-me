package ru.practicum.exception;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.Pattern;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleBadRequestException(final BadRequestException e) {
        log.error("Ошибка: " + e.getMessage());
        return ApiError.builder()
                .errors(Arrays.stream(e.getStackTrace())
                        .map(Object::toString)
                        .collect(Collectors.toList()))
                .message(e.getMessage())
                .reason(e.getReason())
                .status(HttpStatus.BAD_REQUEST.toString())
                .timestamp(LocalDateTime.now().format(Pattern.dateFormatter))
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleNotFound(final NotFoundException e) {
        log.error("Ошибка: " + e.getMessage());
        return ApiError.builder()
                .errors(Arrays.stream(e.getStackTrace())
                        .map(Object::toString)
                        .collect(Collectors.toList()))
                .message(e.getMessage())
                .reason(e.getReason())
                .status(HttpStatus.NOT_FOUND.toString())
                .timestamp(LocalDateTime.now().format(Pattern.dateFormatter))
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleConflict(final ConflictException e) {
        log.error("Ошибка: " + e.getMessage());
        return ApiError.builder()
                .errors(Arrays.stream(e.getStackTrace())
                        .map(Object::toString)
                        .collect(Collectors.toList()))
                .message(e.getMessage())
                .reason(e.getReason())
                .status(HttpStatus.CONFLICT.toString())
                .timestamp(LocalDateTime.now().format(Pattern.dateFormatter))
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError handleThrowable(final Throwable e) {
        log.error("Ошибка: {}", e.getMessage(), e.getStackTrace());
        return ApiError.builder()
                .errors(Arrays.stream(e.getStackTrace())
                        .map(Object::toString)
                        .collect(Collectors.toList()))
                .message(e.getMessage())
                .reason("INTERNAL_SERVER_ERROR")
                .status(HttpStatus.INTERNAL_SERVER_ERROR.toString())
                .timestamp(LocalDateTime.now().format(Pattern.dateFormatter))
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleMethodArgumentNotValidException(final MethodArgumentNotValidException e) {
        log.error("Ошибка: {}", e.getMessage(), e.getStackTrace());
        return ApiError.builder()
                .errors(Arrays.stream(e.getStackTrace())
                        .map(Object::toString)
                        .collect(Collectors.toList()))
                .message(e.getMessage())
                .reason("BAD_REQUEST")
                .status(HttpStatus.BAD_REQUEST.toString())
                .timestamp(LocalDateTime.now().format(Pattern.dateFormatter))
                .build();
    }


    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleConstraintViolationException(final ConstraintViolationException e) {
        log.error("Ошибка: {}", e.getMessage(), e.getStackTrace());
        return ApiError.builder()
                .errors(Arrays.stream(e.getStackTrace())
                        .map(Object::toString)
                        .collect(Collectors.toList()))
                .message(e.getMessage())
                .reason("BAD_REQUEST")
                .status(HttpStatus.BAD_REQUEST.toString())
                .timestamp(LocalDateTime.now().format(Pattern.dateFormatter))
                .build();
    }
}