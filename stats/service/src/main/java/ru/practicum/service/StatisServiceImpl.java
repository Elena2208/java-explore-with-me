package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.EndpointHitDto;
import ru.practicum.ViewStatsDto;
import ru.practicum.exception.BadRequestException;
import ru.practicum.model.Hit;
import ru.practicum.model.HitMapper;
import ru.practicum.repository.StatisRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class StatisServiceImpl implements StatisService {
    private final StatisRepository repository;
    private final HitMapper mapper;

    @Override
    public EndpointHitDto save(EndpointHitDto endpointHit) {
        Hit save = mapper.toHit(endpointHit);
        repository.save(save);
        return mapper.toEndpointHit(save);
    }

    @Override
    public List<ViewStatsDto> getStatistic(Map<String, String> params, Set<String> uris) {
        boolean isUnique = params.containsKey("unique") ? Boolean.parseBoolean(params.get("unique")) : false;
        uris = uris == null ? new HashSet<>() : uris;
        LocalDateTime startStat = parseToLocalDate(params.get("start"));
        LocalDateTime endStat = parseToLocalDate(params.get("end"));
        if (startStat.isAfter(endStat)) {
            throw new BadRequestException("Incorrect start date and end date.");
        }
        if (uris.size() > 0 && !isUnique) {
            return repository.getUrisViewsFromSet(uris, startStat, endStat);
        } else if (uris.size() > 0 && isUnique) {
            return repository.getUrisViewsFromSetUnique(uris, startStat, endStat);
        } else if (isUnique) {
            return repository.getUrisViewsUnique(startStat, endStat);
        } else {
            return repository.getUrisViews(startStat, endStat);
        }
    }
    private LocalDateTime parseToLocalDate(String date) {
        return LocalDateTime.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}
