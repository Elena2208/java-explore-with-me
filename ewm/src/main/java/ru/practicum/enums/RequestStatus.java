package ru.practicum.enums;

import java.util.Arrays;
import java.util.Optional;

public enum RequestStatus {
    PENDING,
    CONFIRMED,
    REJECTED,
    CANCELED;

    public static Optional<RequestStatus> from(String stringState) {
        return Arrays.stream(RequestStatus.values())
                .filter(state -> state.name().equalsIgnoreCase(stringState))
                .findFirst();
    }
}
