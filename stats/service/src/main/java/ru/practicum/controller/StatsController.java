package ru.practicum.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.EndpointHitDto;
import ru.practicum.ViewStatsDto;
import ru.practicum.service.StatService;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping
public class StatsController {
    private final StatService service;
    public  final DateTimeFormatter FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @GetMapping("/stats")
    public List<ViewStatsDto> getStats(@RequestParam String start,
                                       @RequestParam String end,
                                       @RequestParam (required = false, defaultValue = "") List<String> uris,
                                       @RequestParam (required = false, defaultValue = "false")
                                               Boolean unique) {
        return service.getStats(LocalDateTime.parse(start,FORMAT), LocalDateTime.parse(end,FORMAT), uris, unique);
    }

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public EndpointHitDto saveHit(@Valid @RequestBody EndpointHitDto endpointHitDto) {
        return service.saveHit(endpointHitDto);
    }
}
