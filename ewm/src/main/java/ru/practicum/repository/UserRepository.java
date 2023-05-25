package ru.practicum.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.model.User;

import java.util.Set;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query(value = "SELECT * " +
            "FROM users AS u " +
            "WHERE u.user_id IN ?1 " +
            "ORDER BY u.user_id DESC ", nativeQuery = true)
    Page<User> getAllUsersById(PageRequest pageRequest, Set<Long> usersIds);

    User findFirstByEmail(String email);

}
