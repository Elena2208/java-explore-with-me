package ru.practicum.enums;

import java.util.Arrays;
import java.util.Optional;

public enum EventSort {
    EVENT_DATE,
    VIEWS;
    public static Optional<EventSort> from(String stringState) {
        return Arrays.stream(EventSort.values())
                .filter(state -> state.name().equalsIgnoreCase(stringState))
                .findFirst();
    }
}
