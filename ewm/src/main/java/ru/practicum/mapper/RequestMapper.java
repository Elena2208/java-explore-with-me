package ru.practicum.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.Pattern;
import ru.practicum.dto.request.ParticipationRequestDto;
import ru.practicum.model.Request;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RequestMapper {

    public static ParticipationRequestDto toRequestDto(Request request) {
        return ParticipationRequestDto.builder()
                .id(request.getId())
                .event(request.getEventId())
                .created(request.getCreated().format(Pattern.dateFormatter))
                .requester(request.getRequester().getId())
                .status(request.getStatus())
                .build();
    }
}
