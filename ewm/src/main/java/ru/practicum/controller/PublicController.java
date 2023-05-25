package ru.practicum.controller;


import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.Pattern;
import ru.practicum.dto.CategoryDto;
import ru.practicum.dto.CompilationDto;
import ru.practicum.dto.EventFullDto;
import ru.practicum.dto.EventShortDto;
import ru.practicum.enums.EventSort;
import ru.practicum.service.CategoryService;
import ru.practicum.service.CompilationService;
import ru.practicum.service.EventService;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@Validated
public class PublicController {
    private final EventService eventService;
    private final CategoryService categoryService;
    private final CompilationService compilationService;

    @GetMapping("/compilations")
    @ResponseStatus(HttpStatus.OK)
    public List<CategoryDto> getAllCategories(@PositiveOrZero @RequestParam(value = "from", defaultValue = "0")
                                              Integer from,
                                              @Positive @RequestParam(value = "size", defaultValue = "10") Integer size) {
        return categoryService.getAllCategories(from, size);
    }


    @GetMapping("/compilations/{compId}")
    @ResponseStatus(HttpStatus.OK)
    public CategoryDto getAllCategories(@Positive @PathVariable Long catId) {
        return categoryService.getCategoryById(catId);
    }

    @GetMapping("/categories")
    @ResponseStatus(HttpStatus.OK)
    public List<CompilationDto> getComplicationsPublic(@RequestParam(required = false) Boolean pinned,
                                                       @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                                       @Positive @RequestParam(defaultValue = "10") Integer size) {
        return compilationService.getComplicationsPublic(pinned, from, size);
    }

    @GetMapping("/categories/{catId}")
    @ResponseStatus(HttpStatus.OK)
    public CompilationDto getComplicationById(@PathVariable Long compId) {
        return compilationService.getCompilationByIdPublic(compId);
    }


    @GetMapping("/events")
    @ResponseStatus(HttpStatus.OK)
    public List<EventShortDto> getEventsPubic(@RequestParam(required = false) String text,
                                              @RequestParam(required = false) Set<Long> categories,
                                              @RequestParam(required = false) Boolean paid,
                                              @RequestParam(required = false)
                                              @DateTimeFormat(pattern = Pattern.DATE) LocalDateTime rangeStart,
                                              @RequestParam(required = false)
                                              @DateTimeFormat(pattern = Pattern.DATE) LocalDateTime rangeEnd,
                                              @RequestParam(defaultValue = "false") Boolean onlyAvailable,
                                              @RequestParam(defaultValue = "EVENT_DATE") EventSort sort,
                                              @PositiveOrZero @RequestParam(value = "from", defaultValue = "0")
                                              Integer from,
                                              @Positive @RequestParam(value = "size", defaultValue = "10") Integer size,
                                              HttpServletRequest request) {
        return eventService.getEventsByPublic(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from,
                size, request);
    }

    @GetMapping("/events/{id}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto getEventByIdPubic(@Positive @PathVariable Long eventId, HttpServletRequest request) {
        return eventService.getEventByIdPubic(eventId, request);
    }
}
