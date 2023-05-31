package ru.practicum.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.EventsShortDto;
import ru.practicum.dto.event.NewEventDto;
import ru.practicum.dto.event.UpdateEvent;
import ru.practicum.dto.request.EventRequestStatusUpdateRequest;
import ru.practicum.dto.request.EventRequestStatusUpdateResult;
import ru.practicum.dto.request.ParticipationRequestDto;
import ru.practicum.enums.Sort;
import ru.practicum.enums.State;
import ru.practicum.enums.StateAction;
import ru.practicum.exception.BadRequestException;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.mapper.EventMapper;
import ru.practicum.mapper.RequestMapper;
import ru.practicum.model.*;
import ru.practicum.repository.*;
import ru.practicum.service.EventsService;
import ru.practicum.service.WorkStatistic;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static ru.practicum.mapper.EventMapper.*;
import static ru.practicum.mapper.RequestMapper.toRequestDto;

@Service
@RequiredArgsConstructor
public class EventsServiceImpl implements EventsService {
    private final EventsRepository eventRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final LocationRepository locationRepository;
    private final RequestRepository requestRepository;
    private final WorkStatistic workStatistic;


    public EventFullDto createEvent(Long userId, NewEventDto dto) {
        if (dto.getParticipantLimit() == null) {
            dto.setParticipantLimit(0L);
        }
        LocalDateTime nowDateTime = LocalDateTime.now();
        validDateTimeForDto(nowDateTime, dto.getEventDate());
        Category category = categoryRepository.findById(dto.getCategory())
                .orElseThrow(() -> new NotFoundException("NotFoundException", "Категория с таким id  не найдена"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("NotFoundException", "Пользователь с таким id  не найден"));
        locationRepository.save(toLocation(dto.getLocation()));
        Event event = toEvent(dto, category, user, nowDateTime);
        return toEventFullDto(eventRepository.save(event));
    }

    public List<EventsShortDto> getEventsFromUser(Long userId, Integer from, Integer size) {
        PageRequest page = PageRequest.of(from, size);
        User user = validUser(userId);
        List<Event> events = eventRepository.findDByInitiator(user, page);
        Map<Long, Long> hits = workStatistic.getStatisticFromListEvents(events);
        events.forEach(event -> event.setViews(hits.get(event.getId())));
        return events.stream()
                .map(EventMapper::toEventShortDto)
                .collect(Collectors.toList());
    }

    public EventFullDto getEventWithOwner(Long userId, Long eventId) {
        validUser(userId);
        Event event = findEventById(eventId);
        Map<Long, Long> hits = workStatistic.getStatisticFromListEvents(List.of(event));
        event.setViews(hits.get(event.getId()));
        return toEventFullDto(event);
    }

    public EventFullDto updateEvent(Long userId, Long eventId, UpdateEvent dto) {
        Event event = findEventById(eventId);
        validUser(userId);
        if (dto.getEventDate() != null) {
            validDateTimeForDto(LocalDateTime.now(), dto.getEventDate());
        }
        if (!(event.getState().equals(State.CANCELED) || event.getState().equals(State.PENDING))) {
            throw new ConflictException("ConflictException", "Некорректный статус. Изменить можно только отмененные " +
                    "события или события в состоянии ожидания модерации.");
        }
        if (dto.getStateAction() != null) {
            switch (dto.getStateAction()) {
                case SEND_TO_REVIEW:
                    event.setState(State.PENDING);
                    break;
                case CANCEL_REVIEW:
                    event.setState(State.CANCELED);
                    break;
                default:
                    throw new ConflictException("ConflictException", "Некорректный статус dto.");
            }
        }
        Event updatedEvent = updateEventFields(event, dto);
        Event updatedEventFromDB = eventRepository.save(updatedEvent);
        Map<Long, Long> hits = workStatistic.getStatisticFromListEvents(List.of(updatedEventFromDB));
        event.setViews(hits.get(event.getId()));
        return toEventFullDto(updatedEventFromDB);
    }

    public List<EventFullDto> getEventsForAdmin(List<Long> users, List<String> states, List<Long> categories,
                                                LocalDateTime rangeStart, LocalDateTime rangeEnd, Integer from,
                                                Integer size) {
        PageRequest page = PageRequest.of(from, size);
        List<State> stateList = null;
        LocalDateTime start = null;
        LocalDateTime end = null;
        if (states != null) {
            stateList = states.stream()
                    .map(State::valueOf)
                    .collect(Collectors.toList());
        }
        if (rangeStart != null) {
            start = rangeStart;
        }
        if (rangeEnd != null) {
            end = rangeEnd;
        }
        List<Event> events = eventRepository.getEventsWithUsersStatesCategoriesDateTime(
                users, stateList, categories, start, end, page);
        Map<Long, Long> hits = workStatistic.getStatisticFromListEvents(events);
        events.forEach(event -> event.setViews(hits.get(event.getId())));
        return events.stream()
                .map(EventMapper::toEventFullDto)
                .collect(Collectors.toList());
    }


    public EventFullDto updateEventByAdmin(Long eventId, UpdateEvent dto) {
        Event event = findEventById(eventId);
        if (dto.getEventDate() != null) {
            if (LocalDateTime.now().plusHours(1).isAfter(dto.getEventDate())) {
                throw new BadRequestException(" BadRequestException", "Ошибка. Дата и время на которые намечено " +
                        "событие не может быть раньше, чем через час от текущего момента");
            }
        } else {
            if (dto.getStateAction() != null) {
                if (dto.getStateAction().equals(StateAction.PUBLISH_EVENT) &&
                        LocalDateTime.now().plusHours(1).isAfter(event.getEventDate())) {
                    throw new ConflictException("ConflictException", "Ошибка. Дата и время публикуемого события " +
                            "не может быть раньше, чем через час от текущего момента");
                }
                if (dto.getStateAction().equals(StateAction.PUBLISH_EVENT) && !(event.getState().equals(State.PENDING))) {
                    throw new ConflictException("ConflictException", "Некорректный статус. Событие можно публиковать, " +
                            "только если оно в состоянии ожидания публикации.");
                }
                if (dto.getStateAction().equals(StateAction.REJECT_EVENT) && event.getState().equals(State.PUBLISHED)) {
                    throw new ConflictException("ConflictException", "Некорректный статус. Событие можно отклонить, " +
                            "только если оно еще не опубликовано.");
                }
            }
        }
        if (dto.getStateAction() != null) {
            switch (dto.getStateAction()) {
                case REJECT_EVENT:
                    event.setState(State.CANCELED);
                    break;
                case PUBLISH_EVENT:
                    event.setState(State.PUBLISHED);
                    event.setPublishedOn(LocalDateTime.now());
                    break;
                default:
                    throw new ConflictException("ConflictException", "Некорректный статус dto.");
            }
        }
        Event updatedEvent = updateEventFields(event, dto);
        Event updatedEventFromDB = eventRepository.save(updatedEvent);
        Map<Long, Long> hits = workStatistic.getStatisticFromListEvents(List.of(updatedEventFromDB));
        updatedEventFromDB.setViews(hits.get(event.getId()));
        return toEventFullDto(updatedEventFromDB);
    }

    public List<EventsShortDto> getEventsWithFilters(String text, List<Long> categories, Boolean paid,
                                                     LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                                     Boolean onlyAvailable, String sort, Integer from,
                                                     Integer size, HttpServletRequest request) {
        PageRequest page = PageRequest.of(from, size);
        List<Event> events = new ArrayList<>();
        if (onlyAvailable) {
            if (sort == null) {
                events = eventRepository.getAvailableEventsWithFiltersDateSorted(
                        text, State.PUBLISHED, categories, paid, rangeStart, rangeEnd, page);
            } else {
                switch (Sort.valueOf(sort)) {
                    case EVENT_DATE:
                        events = eventRepository.getAvailableEventsWithFiltersDateSorted(
                                text, State.PUBLISHED, categories, paid, rangeStart, rangeEnd, page);
                        workStatistic.addStatistic(request);
                        return events.stream()
                                .map(EventMapper::toEventShortDto)
                                .collect(Collectors.toList());
                    case VIEWS:
                        events = eventRepository.getAvailableEventsWithFilters(
                                text, State.PUBLISHED, categories, paid, rangeStart, rangeEnd, page);
                        Map<Long, Long> hits = workStatistic.getStatisticFromListEvents(events);
                        events.forEach(event -> event.setViews(hits.get(event.getId())));
                        workStatistic.addStatistic(request);
                        return events.stream()
                                .map(EventMapper::toEventShortDto)
                                .sorted(Comparator.comparing(EventsShortDto::getViews))
                                .collect(Collectors.toList());
                }
            }
        } else {
            if (sort == null) {
                events = eventRepository.getAllEventsWithFiltersDateSorted(
                        text, State.PUBLISHED, categories, paid, rangeStart, rangeEnd, page);
            } else {
                switch (Sort.valueOf(sort)) {
                    case EVENT_DATE:
                        events = eventRepository.getAllEventsWithFiltersDateSorted(
                                text, State.PUBLISHED, categories, paid, rangeStart, rangeEnd, page);
                        workStatistic.addStatistic(request);
                        return events.stream()
                                .map(EventMapper::toEventShortDto)
                                .collect(Collectors.toList());
                    case VIEWS:
                        events = eventRepository.getAllEventsWithFilters(
                                text, State.PUBLISHED, categories, paid, rangeStart, rangeEnd, page);
                        Map<Long, Long> hits = workStatistic.getStatisticFromListEvents(events);
                        events.forEach(event -> event.setViews(hits.get(event.getId())));
                        workStatistic.addStatistic(request);
                        return events.stream()
                                .map(EventMapper::toEventShortDto)
                                .sorted(Comparator.comparing(EventsShortDto::getViews))
                                .collect(Collectors.toList());
                }
            }
        }
        workStatistic.addStatistic(request);
        return events.stream()
                .map(EventMapper::toEventShortDto)
                .collect(Collectors.toList());
    }


    public EventFullDto getEventWithFullInfoById(Long id, HttpServletRequest request) {
        Event event = findEventById(id);
        if (!event.getState().equals(State.PUBLISHED)) {
            throw new NotFoundException("NotFoundException", "Событие еще не опубликовано");
        }
        eventRepository.save(event);
        workStatistic.addStatistic(request);
        Map<Long, Long> hits = workStatistic.getStatisticFromListEvents(List.of(event));
        event.setViews(hits.get(event.getId()));
        return toEventFullDto(event);
    }

    public List<ParticipationRequestDto> getRequestsForUserForThisEvent(Long userId, Long eventId) {
        validUser(userId);
        findEventById(eventId);
        List<Request> requests = requestRepository.findByEventId(eventId);
        return requests.stream()
                .map(RequestMapper::toRequestDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public EventRequestStatusUpdateResult changeRequestsStatus(Long userId, Long eventId,
                                                               EventRequestStatusUpdateRequest dto) {
        List<ParticipationRequestDto> confirmedRequests = new ArrayList<>();
        List<ParticipationRequestDto> rejectedRequests = new ArrayList<>();
        validUser(userId);
        Event event = findEventById(eventId);
        if (!event.getRequestModeration() || event.getParticipantLimit().equals(0L)) {
            throw new ConflictException("ConflictException", "Подтверждение заявок не требуется");
        }
        long limitBalance = event.getParticipantLimit() - event.getConfirmedRequests();
        if (event.getParticipantLimit() != 0 && limitBalance <= 0) {
            throw new ConflictException("ConflictException", "У события достигнут лимит запросов на участие.");
        }
        if (dto.getStatus().equals(State.REJECTED.toString())) {
            List<Request> requests = requestRepository.findByIdIn(dto.getRequestIds());
            for (Request request : requests) {
                if (request.getStatus().equals(State.PENDING)) {
                    request.setStatus(State.REJECTED);
                    requestRepository.save(request);
                    rejectedRequests.add(toRequestDto(request));
                }
            }
        }
        List<Request> requests = requestRepository.findByIdIn(dto.getRequestIds());
        for (Request request : requests) {
            if (limitBalance != 0) {
                if (request.getStatus().equals(State.PENDING)) {
                    request.setStatus(State.CONFIRMED);
                    event.setConfirmedRequests(event.getConfirmedRequests() + 1);
                    eventRepository.save(event);
                    requestRepository.save(request);
                    confirmedRequests.add(toRequestDto(request));
                    limitBalance--;
                }
            } else {
                if (request.getStatus().equals(State.PENDING)) {
                    request.setStatus(State.REJECTED);
                    requestRepository.save(request);
                    rejectedRequests.add(toRequestDto(request));
                }
            }
        }
        return EventRequestStatusUpdateResult.builder()
                .confirmedRequests(confirmedRequests)
                .rejectedRequests(rejectedRequests)
                .build();
    }


    private Event updateEventFields(Event event, UpdateEvent dto) {
        if (!dto.getAnnotation().isBlank()) {
            event.setAnnotation(dto.getAnnotation());
        }
        if (dto.getCategory() != null) {
            event.setCategory(categoryRepository.findById(dto.getCategory()).orElseThrow(()
                    -> new NotFoundException("NotFoundException", "Категория с таким id  не найдена")));
        }
        if (!dto.getDescription().isBlank()) {
            event.setDescription(dto.getDescription());
        }
        if (dto.getEventDate() != null) {
            event.setEventDate(dto.getEventDate());
        }
        if (dto.getLocation() != null) {
            List<Location> location = locationRepository.findByLatAndLon(dto.getLocation().getLat(),
                    dto.getLocation().getLon());
            if (location.isEmpty()) {
                locationRepository.save(toLocation(dto.getLocation()));
            }
            event.setLocation(toLocation(dto.getLocation()));
        }
        if (dto.getPaid() != null) {
            event.setPaid(dto.getPaid());
        }
        event.setParticipantLimit(dto.getParticipantLimit());
        if (dto.getRequestModeration() != null) {
            event.setRequestModeration(dto.getRequestModeration());
        }
        if (!dto.getTitle().isBlank()) {
            event.setTitle(dto.getTitle());
        }
        return event;
    }

    private void validDateTimeForDto(LocalDateTime nowDateTime, LocalDateTime dtoDateTime) {
        if (nowDateTime.plusHours(2).isAfter(dtoDateTime)) {
            throw new BadRequestException("BadRequestException", "Ошибка. Дата и время на которые намечено событие " +
                    "не может быть раньше, чем через два часа от текущего момента");
        }
    }

    private User validUser(Long idUser) {
        return userRepository.findById(idUser)
                .orElseThrow(() -> new NotFoundException("NotFoundException", "Пользователь с таким id  не найден"));
    }

    private Event findEventById(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("NotFoundException", "Событие с таким id не найдено"));
    }
}
