package ru.practicum.controller.publicController;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.Pattern;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.EventsShortDto;
import ru.practicum.service.EventsService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;


@RestController
@RequestMapping(path = "/events")
@RequiredArgsConstructor
@Validated
public class PublicEventsController {
    private final EventsService eventsService;

    @GetMapping
    public List<EventsShortDto> getEventsWithFilters(
            @RequestParam(required = false) String text,
            @RequestParam(required = false) List<Long> categories,
            @RequestParam(required = false) Boolean paid,
            @RequestParam(required = false) @DateTimeFormat(pattern = Pattern.DATE) LocalDateTime rangeStart,
            @RequestParam(required = false) @DateTimeFormat(pattern = Pattern.DATE) LocalDateTime rangeEnd,
            @RequestParam(defaultValue = "false") Boolean onlyAvailable,
            @RequestParam(required = false) String sort,
            @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
            @RequestParam(defaultValue = "10") @Positive Integer size,
            HttpServletRequest request) {
        return eventsService.getEventsWithFilters(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort,
                from, size, request);
    }

    @GetMapping("{id}")
    public EventFullDto getEventWithFullInfoById(@PathVariable Long id, HttpServletRequest request) {
        return eventsService.getEventWithFullInfoById(id, request);
    }
}
