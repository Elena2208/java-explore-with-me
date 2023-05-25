package ru.practicum.service.Impl;

import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.StatsClient;
import ru.practicum.dto.*;
import ru.practicum.enums.EventSort;
import ru.practicum.enums.EventState;
import ru.practicum.enums.EventStateAction;
import ru.practicum.enums.RequestStatus;
import ru.practicum.exception.BadRequestException;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.mapper.CategoryMapper;
import ru.practicum.mapper.EventMapper;
import ru.practicum.mapper.RequestMapper;
import ru.practicum.model.Event;
import ru.practicum.model.QEvent;
import ru.practicum.model.Request;
import ru.practicum.model.User;
import ru.practicum.repository.CategoryRepository;
import ru.practicum.repository.EventRepository;
import ru.practicum.repository.RequestRepository;
import ru.practicum.service.CategoryService;
import ru.practicum.service.Client;
import ru.practicum.service.EventService;
import ru.practicum.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final StatsClient statisticClient;
    private final EventRepository eventRepository;
    private final RequestRepository requestRepository;
    private final Client client;
    private final EventMapper eventMapper;
    private final UserService userService;
    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;
    private final CategoryRepository categoryRepository;
    private final RequestMapper requestMapper;

    @Override
    public Event getEventById(Long eventId) {
        return eventRepository.findById(eventId).orElseThrow(
                () -> new NotFoundException("Event not found.")
        );
    }

    @Override
    public EventFullDto createEvent(Long userId, NewEventDto newEvent) {
        if (LocalDateTime.now().plusHours(2).isAfter(newEvent.getEventDate())) {
            throw new BadRequestException("Bad request.");
        }
        User user = userService.getUserById(userId);
        CategoryDto categoryDto = categoryService.getCategoryById(newEvent.getCategory());
        Event event = eventMapper.toEvent(newEvent);
        event.setInitiator(user);
        event.setCategory(categoryMapper.categoryDtoToCategory(categoryDto));
        eventRepository.save(event);
        return client.setViewsEventFullDto(eventMapper.toEventFullDto(event));
    }

    @Override
    public List<EventFullDto> getAllEventsByAdmin(Set<Long> users, Set<EventState> states, Set<Long> categories,
                                                  LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                                  Integer from, Integer size) {
        users = users == null ? new HashSet<>() : users;
        states = states == null ? new HashSet<>() : states;
        categories = categories == null ? new HashSet<>() : categories;
        rangeStart = rangeStart == null ? LocalDateTime.now() : rangeStart;
        rangeEnd = rangeEnd == null ? rangeStart.plusYears(10) : rangeEnd;
        BooleanExpression byUsers;
        BooleanExpression byStates;
        BooleanExpression byCategories;
        BooleanExpression byDate = QEvent.event.eventDate.between(rangeStart, rangeEnd);
        if (users.isEmpty()) {
            byUsers = QEvent.event.initiator.id.notIn(users);
        } else {
            byUsers = QEvent.event.initiator.id.in(users);
        }
        if (states.isEmpty()) {
            byStates = QEvent.event.state.notIn(states);
        } else {
            byStates = QEvent.event.state.in(states);
        }
        if (categories.isEmpty()) {
            byCategories = QEvent.event.category.id.notIn(categories);
        } else {
            byCategories = QEvent.event.category.id.in(categories);
        }
        int page = from / size;
        PageRequest pageRequest = PageRequest.of(page, size);
        Iterable<Event> foundEvents = eventRepository.findAll(
                byUsers.and(byStates).and(byCategories).and(byDate), pageRequest);
        return client.setViewsEventFullDtoList(
                eventMapper.iterableToList(foundEvents));
    }

    private Event updateEvent(Event updatedEvent, UpdateEventRequest updateEventRequest, Boolean isAdmin) {
        Optional.ofNullable(updateEventRequest.getAnnotation()).ifPresent(updatedEvent::setAnnotation);
        Optional.ofNullable(updateEventRequest.getCategory()).ifPresent(
                c -> updatedEvent.setCategory(categoryService.getCategoryModelById(c)));
        Optional.ofNullable(updateEventRequest.getDescription()).ifPresent(updatedEvent::setDescription);
        Optional.ofNullable(updateEventRequest.getEventDate()).ifPresent(updatedEvent::setEventDate);
        if (updateEventRequest.getLocation() != null) {
            if (updateEventRequest.getLocation().getLat() != null) {
                updatedEvent.setLat(updateEventRequest.getLocation().getLat());
            }
            if (updateEventRequest.getLocation().getLon() != null) {
                updatedEvent.setLon(updateEventRequest.getLocation().getLon());
            }
        }
        Optional.ofNullable(updateEventRequest.getPaid()).ifPresent(updatedEvent::setPaid);
        Optional.ofNullable(updateEventRequest.getParticipantLimit()).ifPresent(updatedEvent::setParticipantLimit);
        Optional.ofNullable(updateEventRequest.getRequestModeration()).ifPresent(updatedEvent::setRequestModeration);
        if (isAdmin) {
            if (updateEventRequest.getStateAction() != null) {
                if (updatedEvent.getState().equals(EventState.PENDING)) {
                    setEventStateByEventStateAction(updatedEvent, updateEventRequest.getStateAction());
                } else {
                    throw new ConflictException("Мероприятие с ID = " + updatedEvent.getId() + " уже опубликовано/отменено.");
                }
            }
        } else {
            Optional.ofNullable(updateEventRequest.getStateAction()).ifPresent(
                    s -> setEventStateByEventStateAction(updatedEvent, updateEventRequest.getStateAction())
            );
        }
        Optional.ofNullable(updateEventRequest.getTitle()).ifPresent(updatedEvent::setTitle);
        return updatedEvent;
    }

    private void setEventStateByEventStateAction(Event event, EventStateAction eventStateAction) {
        switch (eventStateAction) {
            case PUBLISH_EVENT:
                event.setState(EventState.PUBLISHED);
                break;
            case SEND_TO_REVIEW:
                event.setState(EventState.PENDING);
                break;
            default:
                event.setState(EventState.CANCELED);
                break;
        }
    }

    @Override
    public List<EventShortDto> getAllUsersEvents(Integer from, Integer size, Long userId) {
        Integer page = from / size;
        PageRequest pageRequest = PageRequest.of(page, size);
        List<Event> requests = eventRepository.getAllEventsByUserId(userId, pageRequest).getContent();
        List<EventShortDto> requestsDto = eventMapper.toEventShortDto(requests);
        return client.setViewsEventShortDtoList(requestsDto);
    }

    @Override
    public EventFullDto getFullEventById(Long userId, Long eventId) {
        userService.getUserById(userId);
        return client.setViewsEventFullDto(eventMapper.toEventFullDto(getEventById(eventId)));
    }

    @Override
    public List<ParticipationRequestDto> getRequestsOnEvent(Long userId, Long eventId) {
        userService.getUserById(userId);
        Event event = getEventById(eventId);
        if (!event.getInitiator().getId().equals(userId)) {
            throw new BadRequestException("Bad request.");
        } else {
            List<Request> request = requestRepository.findAllByEventId(eventId);
            return requestMapper.toParticipationRequestDto(request);
        }
    }

    @Override
    public EventRequestStatusUpdateResult updateWithEventsRequests(Long userId, Long eventId,
                                                                   EventRequestStatusUpdateRequest requests) {
        userService.getUserById(userId);
        Event event = getEventById(eventId);
        if (!event.getInitiator().getId().equals(userId)) {
            throw new BadRequestException("Bad request.");
        }
        if (!event.getRequestModeration()) {
            throw new BadRequestException("Bad request.");
        }
        if (event.getParticipantLimit() == 0) {
            throw new BadRequestException("Bad request.");
        }
        if (event.getParticipantLimit() == event.getParticipants().size()) {
            throw new ConflictException("Bad request.");
        }
        EventRequestStatusUpdateResult eventRequestStatusUpdateResult = new EventRequestStatusUpdateResult();
        List<Request> requestsList = requestRepository.findAllByIdInAndStatus(
                requests.getRequestIds(), RequestStatus.PENDING);
        if (requests.getStatus().equals(RequestStatus.CONFIRMED)) {
            int freePlaces = event.getParticipantLimit() - event.getParticipants().size();
            int count = 0;
            for (Request request : requestsList) {
                checkRequestBeforeUpdate(event, request);

                if (freePlaces != count) {
                    request.setStatus(RequestStatus.CONFIRMED);
                    eventRequestStatusUpdateResult.getConfirmedRequests()
                            .add(requestMapper.toParticipationRequestDto(request));
                    count++;
                } else {
                    request.setStatus(RequestStatus.REJECTED);
                    eventRequestStatusUpdateResult.getRejectedRequests()
                            .add(requestMapper.toParticipationRequestDto(request));
                }
            }
        } else {
            for (Request request : requestsList) {
                checkRequestBeforeUpdate(event, request);
                request.setStatus(RequestStatus.REJECTED);
                eventRequestStatusUpdateResult.getRejectedRequests()
                        .add(requestMapper.toParticipationRequestDto(request));
            }
        }
        requestRepository.saveAll(requestsList);
        return eventRequestStatusUpdateResult;
    }

    void checkRequestBeforeUpdate(Event event, Request request) {
        if (!request.getEvent().getId().equals(event.getId())) {
            throw new BadRequestException("Bad request.");
        }
        if (!request.getStatus().equals(RequestStatus.PENDING)) {
            throw new BadRequestException("Bad request.");
        }
    }

    @Override
    public List<EventShortDto> getEventsByPublic(String text, Set<Long> categories, Boolean paid,
                                                 LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                                 Boolean onlyAvailable, EventSort sort, Integer from,
                                                 Integer size, HttpServletRequest request) {
        categories = categories == null ? new HashSet<>() : categories;
        rangeStart = rangeStart == null ? LocalDateTime.now() : rangeStart;
        rangeEnd = rangeEnd == null ? rangeStart.plusYears(100) : rangeEnd;
        if (rangeStart.isAfter(rangeEnd)) {
            throw new BadRequestException("Bad request.");
        }
        BooleanExpression booleanExpression = QEvent.event.state.eq(EventState.PUBLISHED);
        if (text != null) {
            booleanExpression = booleanExpression.and(QEvent.event.description.containsIgnoreCase(text)
                    .or(QEvent.event.annotation.containsIgnoreCase(text)));
        }
        if (categories != null) {
            booleanExpression = booleanExpression.and(QEvent.event.category.id.in(categories));
        }
        if (paid != null) {
            booleanExpression = booleanExpression.and(QEvent.event.paid.eq(paid));
        }
        booleanExpression = booleanExpression.and(QEvent.event.eventDate.between(rangeStart, rangeEnd));
        Integer page = from / size;
        Pageable pageable = PageRequest.of(page, size);
        Page<Event> eventsPage;
        eventsPage = eventRepository.findAll(booleanExpression, pageable);
        List<Event> events = eventsPage.getContent();
        if (onlyAvailable) {
            events.removeIf(event -> event.getParticipants().size() == event.getParticipantLimit());
        }
        statisticClient.createHit(request.getRequestURI(), request.getRemoteAddr());
        return client.setViewsEventShortDtoList(eventMapper.toEventShortDto(events));
    }

    @Override
    public EventFullDto getEventByIdPubic(Long eventId, HttpServletRequest request) {
        Event event = eventRepository.findFirstByIdAndState(eventId, EventState.PUBLISHED);
        if (event != null) {
            statisticClient.createHit(request.getRequestURI(), request.getRemoteAddr());
            return client.setViewsEventFullDto(eventMapper.toEventFullDto(event));
        } else {
            throw new NotFoundException("Event not found.");
        }
    }

    @Override
    public EventFullDto updateEventByUser(Long userId, Long eventId, UpdateEventRequest updatedEventByUser) {
        if (updatedEventByUser.getCategory() != null) {
           categoryService.getCategoryById(updatedEventByUser.getCategory());
        }
        Event eventForUpdate = getEventById(eventId);
        User user = userService.getUserById(userId);
        if (!eventForUpdate.getInitiator().getId().equals(user.getId())) {
            throw new BadRequestException("Только инициатор или администратор могут менять мероприятие.");
        }
        if (updatedEventByUser.getEventDate() != null) {
            if (LocalDateTime.now().plusHours(2).isAfter(updatedEventByUser.getEventDate())) {
                throw new BadRequestException("Некорректная дата начала мероприятия. (Меньше 2-х часов до начала).");
            }
        }
        if (eventForUpdate.getState().equals(EventState.PUBLISHED)) {
            throw new ConflictException("Только мероприятия со статусом PENDING или CANCELED могут быть изменены.");
        }
        return client.setViewsEventFullDto(eventMapper.toEventFullDto(eventRepository.save(updateEvent(eventForUpdate,
                updatedEventByUser, false))));
    }


    @Override
    public EventFullDto updateEventByAdmin(Long eventId, UpdateEventRequest updatedEventByAdmin) {
        if (updatedEventByAdmin.getCategory() != null) {
            if (categoryRepository.getCategoryById(updatedEventByAdmin.getCategory()) == null) {
                throw new BadRequestException("The category does not exist. ");
            }
            ;
        }
        Event eventForUpdate = getEventById(eventId);
        if (updatedEventByAdmin.getEventDate() != null) {
            if (LocalDateTime.now().plusHours(2).isAfter(updatedEventByAdmin.getEventDate())) {
                throw new BadRequestException("Bad request.");
            }
        }

        return client.setViewsEventFullDto(eventMapper.toEventFullDto(
                eventRepository.save(updateEvent(eventForUpdate, updatedEventByAdmin, true))));
    }
}

