package ru.practicum.controller.adminController;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.user.NewUserDto;
import ru.practicum.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Validated
@RestController
@RequestMapping(path = "/admin/users")
@RequiredArgsConstructor
public class AdminUserController {
    private final UserService userService;

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public NewUserDto createUser(@RequestBody @Valid NewUserDto newUserDto) {
        return userService.createUser(newUserDto);
    }

    @GetMapping
    public List<NewUserDto> getUsers(@RequestParam(required = false) List<Long> ids,
                                     @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                     @RequestParam(defaultValue = "10") @Positive Integer size) {
        return userService.getUsers(ids, from, size);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }
}