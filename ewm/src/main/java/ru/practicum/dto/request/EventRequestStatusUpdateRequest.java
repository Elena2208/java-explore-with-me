package ru.practicum.dto.request;

import lombok.Builder;
import lombok.Getter;
import ru.practicum.enums.StatusRequest;

import java.util.List;

@Getter
@Builder
public class EventRequestStatusUpdateRequest {
    private List<Long> requestIds;
    private StatusRequest status;
}

