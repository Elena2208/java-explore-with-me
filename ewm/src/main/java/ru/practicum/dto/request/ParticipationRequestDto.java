package ru.practicum.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import ru.practicum.enums.State;

@Getter
@Builder
public class ParticipationRequestDto {
    private Long id;
    private Long event;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private String created;
    private Long requester;
    private State status;
}
