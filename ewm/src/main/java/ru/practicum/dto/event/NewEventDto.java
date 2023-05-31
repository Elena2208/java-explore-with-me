package ru.practicum.dto.event;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import ru.practicum.Pattern;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;



@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewEventDto {
    @NotNull
    @Length(min = 20, max = 2000)
    private String annotation;
    @NotNull
    private Long category;
    @NotNull
    @Length(min = 20, max = 7000)
    private String description;
    @NotNull
    @JsonFormat
            (shape = JsonFormat.Shape.STRING, pattern = Pattern.DATE)
    private LocalDateTime eventDate;
    @NotNull
    private LocationDto location;
    private boolean paid;
    @PositiveOrZero
    private Long participantLimit;
    private boolean requestModeration;
    @NotNull
    @Length(min = 3, max = 120)
    private String title;
}
