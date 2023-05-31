package ru.practicum.dto.event;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class LocationDto {
    @NotNull
    private Float lat;
    @NotNull
    private Float lon;
}
