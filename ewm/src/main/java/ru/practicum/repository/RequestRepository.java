package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.model.Request;
import ru.practicum.model.User;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Long> {

    List<Request> findByRequester(User user);

    List<Request> findByEventId(Long eventId);
    List<Request> findByIdIn(List<Long> ids);
}
