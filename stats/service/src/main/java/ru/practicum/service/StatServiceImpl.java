package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.EndpointHitDto;
import ru.practicum.ViewStatsDto;
import ru.practicum.mapper.StatsMapper;
import ru.practicum.model.EndpointHit;
import ru.practicum.repository.StatsRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatServiceImpl implements StatService {
    private final StatsRepository statsRepository;

    @Override
    public EndpointHitDto saveHit(EndpointHitDto endpointHitDto) {
        EndpointHit endpointHit = statsRepository.save(StatsMapper.toEndpointHit(endpointHitDto));
        return StatsMapper.toEndpointHitDto(endpointHit);
    }

    @Override
    public List<ViewStatsDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        if (uris == null || uris.isEmpty()) {
            return Collections.emptyList();
        }
        if (unique) {
            return statsRepository.getStatsUnique(start, end, uris)
                    .stream()
                    .map(StatsMapper::toViewStatsDto)
                    .collect(Collectors.toList());
        } else {
            return statsRepository.getStatsNotUnique(start, end, uris)
                    .stream()
                    .map(StatsMapper::toViewStatsDto)
                    .collect(Collectors.toList());
        }
    }
}
