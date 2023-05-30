package ru.practicum.controller;

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
import ru.practicum.service.RequestService;

import javax.validation.Valid;
import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/users/{userId}")
public class PrivateController {
    private final EventsService eventsService;
    private final RequestService requestService;

    @GetMapping("/events")
    public List<EventsShortDto> getEventsFromUser(@PathVariable Long userId,
                                                  @RequestParam(required = false, defaultValue = "0") Integer from,
                                                  @RequestParam(required = false, defaultValue = "10") Integer size) {
        return eventsService.getEventsFromUser(userId, from, size);
    }


    @PostMapping("/events")
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto createEvent(@PathVariable Long userId,
                                    @RequestBody @Valid NewEventDto dto) {
        return eventsService.createEvent(userId, dto);
    }


    @GetMapping("/events/{eventId}")
    public EventFullDto getEventWithOwner(@PathVariable Long userId,
                                          @PathVariable Long eventId) {
        return eventsService.getEventWithOwner(userId, eventId);
    }


    @PatchMapping("/events/{eventId}")
    public EventFullDto updateEvent(@PathVariable Long userId,
                                    @PathVariable Long eventId,
                                    @RequestBody @Valid UpdateEvent dto) {
        return eventsService.updateEvent(userId, eventId, dto);
    }


    @GetMapping("/events/{eventId}/requests")
    public List<ParticipationRequestDto> getRequestsForUserForThisEvent(@PathVariable Long userId,
                                                                        @PathVariable Long eventId) {
        return eventsService.getRequestsForUserForThisEvent(userId, eventId);
    }


    @PatchMapping("/events/{eventId}/requests")
    public EventRequestStatusUpdateResult changeRequestsStatus(@PathVariable Long userId,
                                                               @PathVariable Long eventId,
                                                               @RequestBody EventRequestStatusUpdateRequest dto) {
        return eventsService.changeRequestsStatus(userId, eventId, dto);
    }


    @GetMapping("/requests")
    public List<ParticipationRequestDto> getRequestsForUser(@PathVariable Long userId) {
        return requestService.getRequestsForUser(userId);
    }


    @PostMapping("/requests")
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDto createRequest(@PathVariable Long userId,
                                                 @RequestParam Long eventId) {
        return requestService.createRequest(userId, eventId);
    }

    @PatchMapping("/requests/{requestId}/cancel")
    public ParticipationRequestDto cancelRequest(@PathVariable Long userId,
                                                 @PathVariable Long requestId) {
        return requestService.cancelRequest(userId, requestId);
    }
}
