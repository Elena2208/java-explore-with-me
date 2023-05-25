package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.enums.RequestStatus;
import ru.practicum.model.Event;
import ru.practicum.model.Request;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Long> {

    List<Request> findAllByRequesterId(Long userId);

    List<Request> findAllByEventId(Long eventId);

    List<Request> getByEventIdAndStatus(Long id, RequestStatus pending);

    List<Request> findAllByIdInAndStatus(List<Long> ids, RequestStatus status);

    Request findFirstByRequesterIdAndEventId(Long userId, Long eventId);

    Long countByEventAndStatus(Event event, RequestStatus confirmed);
}