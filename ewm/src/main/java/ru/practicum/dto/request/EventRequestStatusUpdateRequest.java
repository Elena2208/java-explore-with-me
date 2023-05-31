package ru.practicum.dto.request;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class EventRequestStatusUpdateRequest {
    private List<Long> requestIds;
    private StatusRequest status;
}
 enum StatusRequest {
    CONFIRMED,
    REJECTED
}
