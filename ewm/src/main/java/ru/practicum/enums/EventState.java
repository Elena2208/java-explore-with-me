package ru.practicum.enums;

import java.util.Arrays;
import java.util.Optional;

public enum EventState {
    PENDING,
    PUBLISHED,
    CANCELED;

    public static Optional<EventState> from(String stringState) {
        return Arrays.stream(EventState.values())
                .filter(state -> state.name().equalsIgnoreCase(stringState))
                .findFirst();
    }
}
