package ru.practicum.service;

import ru.practicum.EndpointHitDto;
import ru.practicum.ViewStatsDto;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface StatisService {
    EndpointHitDto save(EndpointHitDto endpointHit);

    List<ViewStatsDto> getStatistic(Map<String, String> params, Set<String> uris);
}
