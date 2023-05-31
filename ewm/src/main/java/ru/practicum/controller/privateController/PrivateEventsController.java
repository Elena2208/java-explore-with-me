package ru.practicum.controller.privateController;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.EventsShortDto;
import ru.practicum.dto.event.NewEventDto;
import ru.practicum.dto.event.UpdateEvent;
import ru.practicum.dto.request.EventRequestStatusUpdateRequest;
import ru.practicum.dto.request.EventRequestStatusUpdateResult;
import ru.practicum.dto.request.ParticipationRequestDto;
import ru.practicum.service.EventsService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Validated
@RestController
@RequestMapping(path = "/users/{userId}/events")
@RequiredArgsConstructor
public class PrivateEventsController {
    private final EventsService eventsService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto createEvent(@PathVariable Long userId,
                                    @RequestBody @Valid NewEventDto dto) {
        return eventsService.createEvent(userId, dto);
    }

    @GetMapping
    public List<EventsShortDto> getEventsFromUser(@PathVariable Long userId,
                                                  @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                                  @RequestParam(defaultValue = "10") @Positive Integer size) {
        return eventsService.getEventsFromUser(userId, from, size);
    }


    @GetMapping("/{eventId}")
    public EventFullDto getEventWithOwner(@PathVariable Long userId,
                                          @PathVariable Long eventId) {
        return eventsService.getEventWithOwner(userId, eventId);
    }


    @PatchMapping("/{eventId}")
    public EventFullDto updateEvent(@PathVariable Long userId,
                                    @PathVariable Long eventId,
                                    @RequestBody @Valid UpdateEvent dto) {
        return eventsService.updateEvent(userId, eventId, dto);
    }

    @GetMapping("/{eventId}/requests")
    public List<ParticipationRequestDto> getRequestsForUserForThisEvent(@PathVariable Long userId,
                                                                        @PathVariable Long eventId) {
        return eventsService.getRequestsForUserForThisEvent(userId, eventId);
    }


    @PatchMapping("/{eventId}/requests")
    public EventRequestStatusUpdateResult changeRequestsStatus(@PathVariable Long userId,
                                                               @PathVariable Long eventId,
                                                               @RequestBody EventRequestStatusUpdateRequest dto) {
        return eventsService.changeRequestsStatus(userId, eventId, dto);
    }
}