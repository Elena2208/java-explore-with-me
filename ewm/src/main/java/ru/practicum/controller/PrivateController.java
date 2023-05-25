package ru.practicum.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.*;
import ru.practicum.service.EventService;
import ru.practicum.service.RequestService;

import java.util.List;

@Validated
@RestController
@RequestMapping("/users/{userId}")
@RequiredArgsConstructor
public class PrivateController {
    private final RequestService requestService;
    private final EventService eventService;


    @GetMapping("/events")
    @ResponseStatus(HttpStatus.OK)
    public List<EventShortDto> getAllUsersEvents(
            @Positive @PathVariable Long userId,
            @PositiveOrZero @RequestParam(value = "from", defaultValue = "0") Integer from,
            @Positive @RequestParam(value = "size", defaultValue = "10") Integer size) {
        return eventService.getAllUsersEvents(from, size, userId);
    }

    @PostMapping("/events")
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto createEvent(
            @Positive @PathVariable Long userId,
            @Valid @RequestBody NewEventDto newEvent) {
        return eventService.createEvent(userId, newEvent);
    }


    @GetMapping("/events/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto getFullEventById(
            @Positive @PathVariable Long userId,
            @Positive @PathVariable Long eventId) {
        return eventService.getFullEventById(userId, eventId);
    }


    @PatchMapping("/events/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto updateEventByUser(
            @Positive @PathVariable Long userId,
            @Positive @PathVariable Long eventId,
            @Valid @RequestBody UpdateEventRequest updatedEventByUser) {
        return eventService.updateEventByUser(userId, eventId, updatedEventByUser);
    }


    @GetMapping("/events/{eventId}/requests")
    @ResponseStatus(HttpStatus.OK)
    public List<ParticipationRequestDto> getRequestsOnEvent(
            @Positive @PathVariable Long userId,
            @Positive @PathVariable Long eventId) {
        return eventService.getRequestsOnEvent(userId, eventId);
    }


    @PatchMapping("/events/{eventId}/requests")
    @ResponseStatus(HttpStatus.OK)
    public EventRequestStatusUpdateResult updateWithEventsRequests(
            @Valid @RequestBody EventRequestStatusUpdateRequest requests,
            @Positive @PathVariable Long userId,
            @Positive @PathVariable Long eventId) {
        return eventService.updateWithEventsRequests(userId, eventId, requests);
    }


    @GetMapping("/requests")
    @ResponseStatus(HttpStatus.OK)
    public List<ParticipationRequestDto> getAllUsersRequests(@Positive @PathVariable Long userId) {
        return requestService.getAllUsersRequests(userId);
    }

    @PostMapping("/requests")
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDto createRequest(@Positive @PathVariable Long userId,
                                                 @Positive @RequestParam(value = "eventId", required = true)
                                                 Long eventId) {
        return requestService.createRequest(userId, eventId);
    }

    @PatchMapping("/requests/{requestId}/cancel")
    @ResponseStatus(HttpStatus.OK)
    public ParticipationRequestDto cancelRequestByRequester(@Positive @PathVariable Long userId,
                                                            @Positive @PathVariable Long requestId) {
        return requestService.cancelRequestByRequester(userId, requestId);
    }
}
