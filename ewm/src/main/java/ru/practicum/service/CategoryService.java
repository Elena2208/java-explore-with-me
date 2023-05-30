package ru.practicum.service;

import ru.practicum.dto.category.NewCategoryDto;

import java.util.List;

public interface CategoryService {
    NewCategoryDto createCategory(NewCategoryDto newCategoryDto);

    void deleteCategory(Long id);

    NewCategoryDto updateCategory(Long id, NewCategoryDto newCategoryDto);

    List<NewCategoryDto> getCategories(Integer from, Integer size);

    NewCategoryDto getCategory(Long id);
}
