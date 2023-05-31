package ru.practicum.controller.publicController;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.compilation.CompilationDto;
import ru.practicum.service.CompilationService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;


@RestController
@RequestMapping(path = "/compilations")
@RequiredArgsConstructor
@Validated
public class PublicCompilationController {
    private final CompilationService compilationService;

    @GetMapping("/{compId}")
    public CompilationDto getCompilation(@PathVariable Long compId) {
        return compilationService.getCompilation(compId);
    }


    @GetMapping
    public List<CompilationDto> getCompilationsByFilters(@RequestParam(required = false) Boolean pinned,
                                                         @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                                         @RequestParam(defaultValue = "10") @Positive Integer size) {
        return compilationService.getCompilationsByFilters(pinned, from, size);
    }
}