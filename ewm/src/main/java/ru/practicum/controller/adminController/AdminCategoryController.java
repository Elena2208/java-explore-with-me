package ru.practicum.controller.adminController;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.category.NewCategoryDto;
import ru.practicum.service.CategoryService;

import javax.validation.Valid;

@Validated
@RestController
@RequestMapping(path = "/admin/categories")
@RequiredArgsConstructor
public class AdminCategoryController {
    private final CategoryService categoryService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public NewCategoryDto createCategory(@RequestBody @Valid NewCategoryDto newCategoryDto) {
        return categoryService.createCategory(newCategoryDto);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
    }

    @PatchMapping("{id}")
    public NewCategoryDto updateCategory(@PathVariable Long id,
                                         @RequestBody @Valid NewCategoryDto newCategoryDto) {
        return categoryService.updateCategory(id, newCategoryDto);
    }
}