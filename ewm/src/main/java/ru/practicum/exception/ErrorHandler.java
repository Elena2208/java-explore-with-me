package ru.practicum.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.Pattern;

import java.time.LocalDateTime;

@RestControllerAdvice
public class ErrorHandler {


    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    @ResponseBody
    public ApiError handleUAlreadyExistException(final AlreadyExistException exception) {
        return new ApiError(exception.getMessage(), "Integrity constraint has been violated.",
                HttpStatus.CONFLICT.getReasonPhrase().toUpperCase(),
                LocalDateTime.now().format(Pattern.dateFormatter));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ApiError handleNotFoundException(final NotFoundException exception) {
        return new ApiError(exception.getMessage(), "Not found.",
                HttpStatus.NOT_FOUND.getReasonPhrase().toUpperCase(),
                LocalDateTime.now().format(Pattern.dateFormatter));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    @ResponseBody
    public ApiError handleConflictException(final ConflictException exception) {
        return new ApiError(exception.getMessage(), "Integrity constraint has been violated.",
                HttpStatus.CONFLICT.getReasonPhrase().toUpperCase(),
                LocalDateTime.now().format(Pattern.dateFormatter));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ApiError handleBadRequestException(final BadRequestException exception) {
        return new ApiError(exception.getMessage(), "BadRequest.",
                HttpStatus.BAD_REQUEST.getReasonPhrase().toUpperCase(),
                LocalDateTime.now().format(Pattern.dateFormatter));
    }
}
