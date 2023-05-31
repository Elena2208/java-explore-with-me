package ru.practicum.controller.adminController;


import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.Pattern;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.UpdateEvent;
import ru.practicum.service.EventsService;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@Validated
@RestController
@RequestMapping(path = "/admin/events")
@RequiredArgsConstructor
public class AdminEventsController {
    private final EventsService eventsService;

    @GetMapping
    public List<EventFullDto> getEventsForAdmin(@RequestParam(required = false) List<Long> users,
                                                @RequestParam(required = false) List<String> states,
                                                @RequestParam(required = false) List<Long> categories,
                                                @RequestParam(required = false) @DateTimeFormat(pattern = Pattern.DATE)
                                                LocalDateTime rangeStart,
                                                @RequestParam(required = false) @DateTimeFormat(pattern = Pattern.DATE)
                                                LocalDateTime rangeEnd,
                                                @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                                @RequestParam(defaultValue = "10") @Positive Integer size) {
        return eventsService.getEventsForAdmin(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto updateEventByAdmin(@PathVariable Long eventId,
                                           @RequestBody @Valid UpdateEvent dto) {
        return eventsService.updateEventByAdmin(eventId, dto);
    }
}