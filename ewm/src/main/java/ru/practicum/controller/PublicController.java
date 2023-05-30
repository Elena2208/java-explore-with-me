package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.Pattern;
import ru.practicum.dto.category.NewCategoryDto;
import ru.practicum.dto.compilation.CompilationDto;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.EventsShortDto;
import ru.practicum.service.CategoryService;
import ru.practicum.service.CompilationService;
import ru.practicum.service.EventsService;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class PublicController {
    private final CompilationService compilationService;
    private final CategoryService categoryService;
    private final EventsService eventsService;

    @GetMapping("/compilations")
    public List<CompilationDto> getCompilationsByFilters(@RequestParam(required = false) Boolean pinned,
                                                         @RequestParam(required = false, defaultValue = "0")
                                                         Integer from,
                                                         @RequestParam(required = false, defaultValue = "10")
                                                         Integer size) {
        return compilationService.getCompilationsByFilters(pinned, from, size);
    }

    @GetMapping("/compilations/{compId}")
    public CompilationDto getCompilation(@PathVariable Long compId) {
        return compilationService.getCompilation(compId);
    }

    @GetMapping("/categories")
    public List<NewCategoryDto> getCategories(@RequestParam(required = false, defaultValue = "0") Integer from,
                                              @RequestParam(required = false, defaultValue = "10") Integer size) {
        return categoryService.getCategories(from, size);
    }

    @GetMapping("/categories/{catId}")
    public NewCategoryDto getCategory(@PathVariable Long id) {
        return categoryService.getCategory(id);
    }

    @GetMapping("/events")
    public List<EventsShortDto> getEventsWithFilters(
            @RequestParam(required = false) String text,
            @RequestParam(required = false) List<Long> categories,
            @RequestParam(required = false) Boolean paid,
            @RequestParam(required = false) @DateTimeFormat(pattern = Pattern.DATE) LocalDateTime rangeStart,
            @RequestParam(required = false) @DateTimeFormat(pattern = Pattern.DATE) LocalDateTime rangeEnd,
            @RequestParam(required = false, defaultValue = "false") Boolean onlyAvailable,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false, defaultValue = "0") Integer from,
            @RequestParam(required = false, defaultValue = "10") Integer size,
            HttpServletRequest request) {
        return eventsService.getEventsWithFilters(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort,
                from, size, request);
    }


    @GetMapping("/events/{id}")
    public EventFullDto getEventWithFullInfoById(@PathVariable Long id, HttpServletRequest request) {
        return eventsService.getEventWithFullInfoById(id, request);
    }
}
