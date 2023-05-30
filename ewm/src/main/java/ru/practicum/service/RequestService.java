package ru.practicum.service;

import ru.practicum.dto.request.ParticipationRequestDto;

import java.util.List;

public interface RequestService {
    ParticipationRequestDto createRequest(Long userId, Long requestDto);

    List<ParticipationRequestDto> getRequestsForUser(Long userId);

    ParticipationRequestDto cancelRequest(Long userId, Long requestId);
}
