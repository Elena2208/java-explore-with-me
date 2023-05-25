package ru.practicum.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.dto.ParticipationRequestDto;
import ru.practicum.model.Request;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class RequestMapper {
    public ParticipationRequestDto toParticipationRequestDto(Request request) {
        return ParticipationRequestDto.builder()
                .created(request.getCreated())
                .event((request.getEvent().getId()))
                .status(request.getStatus())
                .id(request.getId())
                .requester(request.getRequester().getId())
                .build();
    }

    public List<ParticipationRequestDto> toParticipationRequestDto(List<Request> requests) {
        return requests
                .stream()
                .map(request -> toParticipationRequestDto(request))
                .collect(Collectors.toList());
    }
}