package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.EndpointHitDto;
import ru.practicum.ViewStatsDto;
import ru.practicum.service.StatisService;

import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequiredArgsConstructor
public class StatController {
    private final StatisService service;

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public EndpointHitDto addHit(@RequestBody EndpointHitDto endpointHit) {
        return service.save(endpointHit);
    }

    @GetMapping("/stats")
    @ResponseStatus(HttpStatus.OK)
    public List<ViewStatsDto> getStatistic(@RequestParam(value = "start") String start,
                                           @RequestParam(value = "end") String end,
                                           @RequestParam(value = "unique", defaultValue = "false") String unique,
                                           @RequestParam(value = "uris", required = false) Set<String> uris) {
        Map<String, String> params = Map.of(
                "start", start,
                "end", end,
                "unique", unique);
        return service.getStatistic(params, uris);
    }
}
