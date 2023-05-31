package ru.practicum.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.EventsShortDto;
import ru.practicum.dto.event.LocationDto;
import ru.practicum.dto.event.NewEventDto;
import ru.practicum.enums.State;
import ru.practicum.model.Category;
import ru.practicum.model.Event;
import ru.practicum.model.Location;
import ru.practicum.model.User;

import java.time.LocalDateTime;

import static ru.practicum.mapper.CategoryMapper.toCategoryDto;
import static ru.practicum.mapper.UserMapper.toUserShortDto;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EventMapper {

    public static EventFullDto toEventFullDto(Event event) {
        return EventFullDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(toCategoryDto(event.getCategory()))
                .confirmedRequests(event.getConfirmedRequests())
                .createdOn(event.getCreatedOn())
                .description(event.getDescription())
                .eventDate(event.getEventDate())
                .initiator(toUserShortDto(event.getInitiator()))
                .location(toLocationDto(event.getLocation()))
                .paid(event.getPaid())
                .participantLimit(event.getParticipantLimit())
                .publishedOn((event.getPublishedOn() == null) ? null : event.getPublishedOn())
                .requestModeration(event.getRequestModeration())
                .state(event.getState())
                .title(event.getTitle())
                .views(event.getViews())
                .build();
    }

    public static Event toEvent(NewEventDto newEventDto, Category category, User user, LocalDateTime dateTime) {
        return Event.builder()
                .annotation(newEventDto.getAnnotation())
                .category(category)
                .confirmedRequests(0L)
                .description(newEventDto.getDescription())
                .createdOn(dateTime)
                .eventDate(newEventDto.getEventDate())
                .initiator(user)
                .location(toLocation(newEventDto.getLocation()))
                .paid(newEventDto.isPaid())
                .participantLimit(newEventDto.getParticipantLimit())
                .requestModeration(newEventDto.isRequestModeration())
                .state(State.PENDING)
                .title(newEventDto.getTitle())
                .views(0L)
                .build();
    }

    public static EventsShortDto toEventShortDto(Event event) {
        return EventsShortDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(toCategoryDto(event.getCategory()))
                .confirmedRequests(event.getConfirmedRequests())
                .eventDate(event.getEventDate())
                .initiator(toUserShortDto(event.getInitiator()))
                .paid(event.getPaid())
                .title(event.getTitle())
                .views(event.getViews())
                .build();
    }

    public static Location toLocation(LocationDto locationDto){
       return Location.builder()
                .lat(locationDto.getLat())
                .lon(locationDto.getLon()).build();
    }

    public static LocationDto toLocationDto(Location location){
        return LocationDto.builder()
                .lat(location.getLat())
                .lon(location.getLon()).build();
    }
}
