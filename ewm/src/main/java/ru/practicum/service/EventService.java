package ru.practicum.service;

import ru.practicum.dto.*;
import ru.practicum.enums.EventSort;
import ru.practicum.enums.EventState;
import ru.practicum.model.Event;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface EventService {
    Event getEventById(Long eventId);

    EventFullDto createEvent(Long userId, NewEventDto newEvent);

    List<EventFullDto> getAllEventsByAdmin(Set<Long> users, Set<EventState> states, Set<Long> categories,
                                           LocalDateTime rangeStart, LocalDateTime rangeEnd, Integer from, Integer size);

    

    List<EventShortDto> getAllUsersEvents(Integer from, Integer size, Long userId);

    EventFullDto getFullEventById(Long userId, Long eventId);

    List<ParticipationRequestDto> getRequestsOnEvent(Long userId, Long eventId);

    EventRequestStatusUpdateResult updateWithEventsRequests(Long userId, Long eventId,
                                                            EventRequestStatusUpdateRequest requests);

    List<EventShortDto> getEventsByPublic(String text, Set<Long> categories, Boolean paid, LocalDateTime rangeStart,
                                          LocalDateTime rangeEnd, Boolean onlyAvailable, EventSort sort, Integer from,
                                          Integer size, HttpServletRequest request);

    EventFullDto getEventByIdPubic(Long eventId, HttpServletRequest request);



    EventFullDto updateEventByUser(Long userId, Long eventId, UpdateEventRequest updatedEventByUser);

    EventFullDto updateEventByAdmin(Long eventId, UpdateEventRequest updatedEventByAdmin);
}
