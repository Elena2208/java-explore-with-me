package ru.practicum.service;


import ru.practicum.dto.NewUser;
import ru.practicum.dto.UserDto;
import ru.practicum.model.User;

import java.util.List;
import java.util.Set;

public interface UserService {
    UserDto createUser(NewUser newUser);

    List<UserDto> getAllUsers(Integer from, Integer size, Set<Long> usersIds);

    void deleteUser(Long userId);

    User getUserById(Long userId);

    boolean findUserByEmail(String email);

  //  void isUserPresent(Long userId);
}
