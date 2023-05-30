package ru.practicum.exception;

import lombok.Getter;

@Getter
public class BadRequestException extends IllegalArgumentException {
    private String reason;

    public BadRequestException(String s, String reason) {
        super(s);
        this.reason = reason;
    }
}
