package ru.practicum.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.practicum.EndpointHitDto;
import ru.practicum.Pattern;
import ru.practicum.StatsClient;
import ru.practicum.ViewStatsDto;
import ru.practicum.model.Event;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.lang.String;


@Service
@RequiredArgsConstructor
public class WorkStatistic {
    private final ObjectMapper objectMapper;
    private final StatsClient statsClient;

    public void addStatistic(HttpServletRequest request) {
        statsClient.addStatistic(EndpointHitDto.builder()
                .app(Pattern.app)
                .uri(request.getRequestURI())
                .ip(request.getRemoteAddr())
                .timestamp(LocalDateTime.now())
                .build());
    }


    public Map<Long, Long> getStatisticFromListEvents(List<Event> events) {
        List<Long> idEvents = events.stream()
                .map(Event::getId)
                .collect(Collectors.toList());
        String end = LocalDateTime.now().format(Pattern.dateFormatter);
        String eventsUri = "/events/";
        List<String> uris = idEvents.stream().map(id -> eventsUri + id).collect(Collectors.toList());
        ResponseEntity<Object> response = statsClient.getStatistic(null, end, uris, true);
        List<ViewStatsDto> viewStatsDto = objectMapper.convertValue(response.getBody(), new TypeReference<>() {
        });
        Map<Long, Long> hits = new HashMap<>();
        for (ViewStatsDto statsDto : viewStatsDto) {
            String uri = statsDto.getUri();
            hits.put(Long.parseLong(uri.substring(eventsUri.length())), statsDto.getHits());
        }
        return hits;
    }
}
