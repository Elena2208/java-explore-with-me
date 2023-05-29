package ru.practicum.service;

import ru.practicum.dto.CategoryDto;
import ru.practicum.dto.NewCategoryDto;
import ru.practicum.model.Category;

import java.util.List;

public interface CategoryService {

    List<CategoryDto> getAllCategories(Integer from, Integer size);

    CategoryDto getCategoryById(Long catId);

    CategoryDto createCategory(NewCategoryDto newCategory);

    void deleteCategory(Long catId);

    CategoryDto patchCategoryById(Long catId, NewCategoryDto updatedCategory);

    Category getCategoryModelById(Long catId);
}
