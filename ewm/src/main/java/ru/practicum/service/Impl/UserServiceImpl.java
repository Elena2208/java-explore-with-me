package ru.practicum.service.Impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.dto.NewUser;
import ru.practicum.dto.UserDto;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.mapper.UserMapper;
import ru.practicum.model.User;
import ru.practicum.repository.UserRepository;
import ru.practicum.service.UserService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserDto createUser(NewUser newUser) {
        if (!findUserByEmail(newUser.getEmail())) {
            User user = userRepository.save(userMapper.toUser(newUser));
            return userMapper.toUserDto(user);
        } else {
            throw new ConflictException("Mail is already in use.");
        }
    }

    @Override
    public List<UserDto> getAllUsers(Integer from, Integer size, Set<Long> usersIds) {
        usersIds = usersIds == null ? new HashSet<>() : usersIds;
        PageRequest pageRequest = PageRequest.of(from / size, size);
        if (usersIds.size() > 0) {
            List<User> users = userRepository.getAllUsersById(pageRequest, usersIds).getContent();
            return users
                    .stream()
                    .map(user -> userMapper.toUserDto(user))
                    .collect(Collectors.toList());
        } else {
            List<User> users = userRepository.findAll(pageRequest).getContent();
            return users
                    .stream()
                    .map(user -> userMapper.toUserDto(user))
                    .collect(Collectors.toList());
        }
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.delete(getUserById(userId));
    }

    @Override
    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("User not found."));
    }

    @Override
    public boolean findUserByEmail(String email) {
        return userRepository.findFirstByEmail(email) != null;
    }
}
