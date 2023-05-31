package ru.practicum.controller.privateController;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.request.ParticipationRequestDto;
import ru.practicum.service.RequestService;

import java.util.List;


@Validated
@RestController
@RequestMapping(path = "/users/{userId}/requests")
@RequiredArgsConstructor
public class PrivateRequestController {
    private final RequestService requestService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDto createRequest(@PathVariable Long userId,
                                                 @RequestParam Long eventId) {
        return requestService.createRequest(userId, eventId);
    }

    @GetMapping
    public List<ParticipationRequestDto> getRequestsForUser(@PathVariable Long userId) {
        return requestService.getRequestsForUser(userId);
    }


    @PatchMapping("/{requestId}/cancel")
    public ParticipationRequestDto cancelRequest(@PathVariable Long userId,
                                                 @PathVariable Long requestId) {
        return requestService.cancelRequest(userId, requestId);
    }
}