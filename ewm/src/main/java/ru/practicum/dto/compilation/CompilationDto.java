package ru.practicum.dto.compilation;

import lombok.Builder;
import lombok.Getter;
import ru.practicum.dto.event.EventsShortDto;

import java.util.List;

@Getter
@Builder
public class CompilationDto {
    private Long id;
    private List<EventsShortDto> events;
    private Boolean pinned;
    private String title;
}
