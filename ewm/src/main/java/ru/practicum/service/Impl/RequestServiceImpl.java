package ru.practicum.service.Impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.dto.ParticipationRequestDto;
import ru.practicum.enums.EventState;
import ru.practicum.enums.RequestStatus;
import ru.practicum.exception.BadRequestException;
import ru.practicum.exception.ConflictException;
import ru.practicum.mapper.RequestMapper;
import ru.practicum.model.Event;
import ru.practicum.model.Request;
import ru.practicum.model.User;
import ru.practicum.repository.RequestRepository;
import ru.practicum.service.EventService;
import ru.practicum.service.RequestService;
import ru.practicum.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;
    private final RequestMapper requestMapper;
    private final UserService userService;
    private final EventService eventService;


    @Override
    public ParticipationRequestDto createRequest(Long userId, Long eventId) {
        Event event = eventService.getEventById(eventId);
        User user = userService.getUserById(userId);
        Boolean isUnlimited = event.getParticipantLimit().equals(0);
        validUserAndEvent(user, event, isUnlimited);
        Request participantsRequests = requestRepository.findFirstByRequesterIdAndEventId(userId, eventId);
        if (participantsRequests != null) {
            throw new ConflictException("The request has already been created.");
        }
        RequestStatus requestStatus = RequestStatus.CONFIRMED;
        if (event.getRequestModeration() && !event.getParticipantLimit().equals(0)) {
            requestStatus = RequestStatus.PENDING;
        }
        Request request = requestRepository.save(Request
                .builder()
                .created(LocalDateTime.now())
                .requester(user)
                .status(requestStatus)
                .event(event)
                .build());
        return requestMapper.toParticipationRequestDto(request);
    }

    @Override
    public List<ParticipationRequestDto> getAllUsersRequests(Long userId) {
        userService.getUserById(userId);
        return requestMapper.toParticipationRequestDto(
                requestRepository.findAllByRequesterId(userId));
    }

    @Override
    public ParticipationRequestDto cancelRequestByRequester(Long userId, Long requestId) {
        userService.getUserById(userId);
        Request request = getRequestById(requestId);
        if (!request.getRequester().getId().equals(userId)) {
            throw new BadRequestException("Cancellation of the application is not possible.");
        }
        request.setStatus(RequestStatus.CANCELED);
        return requestMapper.toParticipationRequestDto(request);
    }

    @Override
    public Request getRequestById(Long requestId) {
        return null;
    }

    private void validUserAndEvent(User user, Event event, Boolean isUnlimited) {
        if (event.getInitiator().getId().equals(user.getId())) {
            throw new ConflictException("The user is the organizer.");
        }
        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new ConflictException("The event has not been published yet.");
        }
        if (!isUnlimited) {
            if (requestRepository.getByEventIdAndStatus(event.getId(), RequestStatus.CONFIRMED).size() ==
                    event.getParticipantLimit()) {
                throw new ConflictException("There are no empty seats.");
            }
        }
    }
}
