package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.EndpointHitDto;
import ru.practicum.ViewStatsDto;
import ru.practicum.exception.BadRequestException;
import ru.practicum.model.EndpointHit;
import ru.practicum.model.MapperHit;
import ru.practicum.repository.StatsRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.model.MapperHit.toEndpointHit;
import static ru.practicum.model.MapperHit.toEndpointHitDto;


@Service
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {
    private final StatsRepository repository;


    public EndpointHitDto addStatistic(EndpointHitDto endpointHitDto) {
        EndpointHit endpointHit = toEndpointHit(endpointHitDto);
        return toEndpointHitDto(repository.save(endpointHit));
    }

    public List<ViewStatsDto> getStatistic(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        validDate(start, end);
        if (uris == null) {
            if (unique) {
                return repository.getStatisticsWithUniqueIp(start, end).stream()
                        .map(MapperHit::toViewStatsDto)
                        .collect(Collectors.toList());
            } else {
                return repository.getAllStatistics(start, end).stream()
                        .map(MapperHit::toViewStatsDto)
                        .collect(Collectors.toList());
            }
        } else {
            if (unique) {
                return repository.getStatisticsWithUniqueIpAndUris(start, end, uris).stream()
                        .map(MapperHit::toViewStatsDto)
                        .collect(Collectors.toList());
            } else {
                return repository.getAllStatisticsWithUris(start, end, uris).stream()
                        .map(MapperHit::toViewStatsDto)
                        .collect(Collectors.toList());
            }
        }
    }

    private void validDate(LocalDateTime startTime, LocalDateTime endTime) {
        if (startTime == null || endTime == null) {
            throw new BadRequestException("Error time.");
        }
        if (startTime.isAfter(endTime)) {
            throw new BadRequestException("Error time.");
        }
    }
}
