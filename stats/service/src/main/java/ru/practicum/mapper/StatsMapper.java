package ru.practicum.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.EndpointHitDto;
import ru.practicum.ViewStatsDto;
import ru.practicum.model.EndpointHit;
import ru.practicum.model.ViewStats;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class StatsMapper {
    public static EndpointHit toEndpointHit(EndpointHitDto endpointHitDto) {
        EndpointHit endpointHit = new EndpointHit();
        endpointHit.setApp(endpointHitDto.getApp());
        endpointHit.setIp(endpointHitDto.getIp());
        endpointHit.setUri(endpointHitDto.getUri());
        endpointHit.setTimestamp(LocalDateTime.parse(
                endpointHitDto.getTimestamp(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        return endpointHit;
    }

    public static EndpointHitDto toEndpointHitDto(EndpointHit endpointHit) {
        EndpointHitDto endpointHitDto = new EndpointHitDto();
        endpointHitDto.setApp(endpointHit.getApp());
        endpointHitDto.setIp(endpointHit.getIp());
        endpointHitDto.setUri(endpointHit.getUri());
        endpointHitDto.setTimestamp(String.valueOf(endpointHit.getTimestamp()));
        return endpointHitDto;
    }

    public static ViewStatsDto toViewStatsDto(ViewStats viewStats) {
        return new ViewStatsDto(viewStats.getApp(),
                viewStats.getUri(),
                viewStats.getHits());
    }
}
