package ru.practicum.controller.publicController;


import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.category.NewCategoryDto;
import ru.practicum.service.CategoryService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping(path = "/categories")
@RequiredArgsConstructor
@Validated
public class PublicCategoryController {
    private final CategoryService categoryService;
    @GetMapping
    public List<NewCategoryDto> getCategories(@RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                              @RequestParam(defaultValue = "10") @Positive Integer size) {
        return categoryService.getCategories(from, size);
    }

    @GetMapping("/{id}")
    public NewCategoryDto getCategory(@PathVariable Long id) {
        return categoryService.getCategory(id);
    }
}