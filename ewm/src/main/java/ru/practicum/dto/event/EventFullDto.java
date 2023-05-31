package ru.practicum.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.Pattern;
import ru.practicum.dto.category.NewCategoryDto;
import ru.practicum.dto.user.UserShortDto;
import ru.practicum.enums.State;

import java.time.LocalDateTime;



@Getter
@Setter
@Builder
public class EventFullDto {
    private Long id;
    private String annotation;
    private NewCategoryDto category;
    private String description;
    private Long confirmedRequests;
    @JsonFormat
            (shape = JsonFormat.Shape.STRING, pattern = Pattern.DATE)
    private LocalDateTime createdOn;
    @JsonFormat
            (shape = JsonFormat.Shape.STRING, pattern = Pattern.DATE)
    private LocalDateTime eventDate;
    private UserShortDto initiator;
    private LocationDto location;
    private Boolean paid;
    private Long participantLimit;
    @JsonFormat
            (shape = JsonFormat.Shape.STRING, pattern = Pattern.DATE)
    private LocalDateTime publishedOn;
    private Boolean requestModeration;
    private State state;
    private String title;
    private Long views;
}
