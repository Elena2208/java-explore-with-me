package ru.practicum.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.dto.user.NewUserDto;
import ru.practicum.exception.ConflictException;
import ru.practicum.mapper.UserMapper;
import ru.practicum.model.User;
import ru.practicum.repository.UserRepository;
import ru.practicum.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.mapper.UserMapper.toUser;
import static ru.practicum.mapper.UserMapper.toUserDto;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;

    public NewUserDto createUser(NewUserDto newUserDto) {
        if (repository.existsByName(newUserDto.getName())) {
            throw new ConflictException("ConflictException", "Имя уже занято");
        }
        User user = toUser(newUserDto);
        return toUserDto(repository.save(user));
    }

    public List<NewUserDto> getUsers(List<Long> ids, Integer from, Integer size) {
        PageRequest page = PageRequest.of(from, size);
        if (ids == null) {
            return repository.findAll(page).stream()
                    .map(UserMapper::toUserDto)
                    .collect(Collectors.toList());
        } else {
            return repository.findByIdIn(ids, page).stream()
                    .map(UserMapper::toUserDto)
                    .collect(Collectors.toList());
        }
    }

    public void deleteUser(Long id) {
        repository.deleteById(id);
    }
}
