package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.StatsClient;
import ru.practicum.ViewStatsDto;
import ru.practicum.dto.EventFullDto;
import ru.practicum.dto.EventShortDto;

import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class Client {
    private final StatsClient statisticClient;

    public List<EventShortDto> setViewsEventShortDtoList(List<EventShortDto> events) {
        List<Long> ids = events.stream()
                .map(EventShortDto::getId)
                .collect(Collectors.toList());
        Map<Long, Long> views = getViewsByIds(ids);
        if (!views.isEmpty()) {
            events.forEach(e -> e.setViews(views.get(e.getId())));
        }
        return events;
    }

    public List<EventFullDto> setViewsEventFullDtoList(List<EventFullDto> event) {
        List<Long> ids = event.stream()
                .map(EventFullDto::getId)
                .collect(Collectors.toList());
        Map<Long, Long> views = getViewsByIds(ids);
        if (!views.isEmpty()) {
            event.forEach(e -> e.setViews(views.get(e.getId())));
        }
        return event;
    }

    public EventFullDto setViewsEventFullDto(EventFullDto eventFullDto) {
        Set<String> uri = new HashSet<>();
        uri.add("/events/" + eventFullDto.getId());
        List<ViewStatsDto> views = statisticClient.getViewsByUris(uri);
        if (!views.isEmpty()) {
            eventFullDto.setViews(views.get(0).getHits());
        }
        return eventFullDto;
    }

    public EventShortDto setViewsEventShortDto(EventShortDto eventShortDto) {
        Set<String> uri = new HashSet<>();
        uri.add("/events/" + eventShortDto.getId());
        List<ViewStatsDto> views = statisticClient.getViewsByUris(uri);
        if (!views.isEmpty()) {
            eventShortDto.setViews(views.get(0).getHits());
        }
        return eventShortDto;
    }

    public Map<Long, Long> getViewsByIds(List<Long> ids) {
        Set<String> uri = new HashSet<>();
        for (Long id: ids) {
            uri.add("/events/" + id);
        }
        List<ViewStatsDto> viewStats = statisticClient.getViewsByUris(uri);
        Map<Long,Long> views = new HashMap<>();
        if (!views.isEmpty()) {
            for (ViewStatsDto view : viewStats) {
                String[] eventUrl = view.getUri().split("/");
                views.put(Long.parseLong(eventUrl[2]), view.getHits());
            }
        }
        return views;
    }

}
