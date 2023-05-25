package ru.practicum.model;

import org.springframework.stereotype.Component;
import ru.practicum.EndpointHitDto;

@Component
public class HitMapper {
    public EndpointHitDto toEndpointHit(Hit hit) {
        return new EndpointHitDto(
                hit.getApp(),
                hit.getUri(),
                hit.getIp(),
                hit.getTimestamp()
        );
    }
    public Hit toHit(EndpointHitDto endpointHit) {
        return new Hit(
                null,
                endpointHit.getApp(),
                endpointHit.getUri(),
                endpointHit.getIp(),
                endpointHit.getTimestamp()
        );
    }
}
