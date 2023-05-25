package ru.practicum.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.Pattern;
import ru.practicum.dto.*;
import ru.practicum.enums.EventState;
import ru.practicum.service.CategoryService;
import ru.practicum.service.CompilationService;
import ru.practicum.service.EventService;
import ru.practicum.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping(path = "/admin")
@RequiredArgsConstructor
@Validated
class AdminController {
    private final UserService userService;
    private final EventService eventService;
    private final CompilationService compilationService;
    private final CategoryService categoryService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto createUser(
            @Valid @RequestBody NewUser newUser) {
        return userService.createUser(newUser);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<UserDto> getAllUsers(@PositiveOrZero @RequestParam(value = "from", defaultValue = "0") Integer from,
                                     @Positive @RequestParam(value = "size", defaultValue = "10") Integer size,
                                     @RequestParam(value = "ids", required = false) Set<Long> ids) {
        return userService.getAllUsers(from, size, ids);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@Positive @PathVariable Long userId) {
        userService.deleteUser(userId);
    }

    @PostMapping("/categories")
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto createCategory(@Valid @RequestBody NewCategoryDto newCategory) {
        return categoryService.createCategory(newCategory);
    }

    @DeleteMapping("/categories/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@Positive @PathVariable Long catId) {
        categoryService.deleteCategory(catId);
    }


    @PatchMapping("/categories/{catId}")
    @ResponseStatus(HttpStatus.OK)
    public CategoryDto patchCategoryById(@Valid @RequestBody NewCategoryDto updatedCategory,
                                         @Positive @PathVariable Long catId) {
        return categoryService.patchCategoryById(catId, updatedCategory);
    }


    @GetMapping("/events")
    @ResponseStatus(HttpStatus.OK)
    public List<EventFullDto> getAllUsersEvents(@RequestParam(required = false) Set<Long> users,
                                                @RequestParam(required = false) Set<EventState> states,
                                                @RequestParam(required = false) Set<Long> categories,
                                                @RequestParam(required = false)
                                                @DateTimeFormat(pattern = Pattern.DATE) LocalDateTime rangeStart,
                                                @RequestParam(required = false)
                                                @DateTimeFormat(pattern = Pattern.DATE) LocalDateTime rangeEnd,
                                                @PositiveOrZero @RequestParam(value = "from", defaultValue = "1") Integer from,
                                                @Positive @RequestParam(value = "size", defaultValue = "10") Integer size) {
        return eventService.getAllEventsByAdmin(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    @PatchMapping("/events/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto updateEventByAdmin(@Positive @PathVariable Long eventId,
                                           @Valid @RequestBody UpdateEventRequest updatedEventByAdmin) {
        return eventService.updateEventByAdmin(eventId, updatedEventByAdmin);
    }


    @PostMapping("/compilations")
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto createEvent( @Valid @RequestBody NewCompilationDto newCompilation) {
        return compilationService.createCompilation(newCompilation);
    }


    @PatchMapping("/compilations/{compId}")
    @ResponseStatus(HttpStatus.OK)
    public CompilationDto updateCompilationById( @Positive @PathVariable Long compId,
                                                 @Valid @RequestBody UpdateCompilationRequest updatedCompilation) {
        return compilationService.updateCompilationById(compId, updatedCompilation);
    }


    @DeleteMapping("/compilations/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComplication(@PathVariable Long compId) {
        compilationService.deleteComplication(compId);
    }
}

