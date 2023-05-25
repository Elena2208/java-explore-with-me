package ru.practicum.service.Impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.dto.CategoryDto;
import ru.practicum.dto.NewCategoryDto;
import ru.practicum.exception.BadRequestException;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.mapper.CategoryMapper;
import ru.practicum.model.Category;
import ru.practicum.repository.CategoryRepository;
import ru.practicum.repository.EventRepository;
import ru.practicum.service.CategoryService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final EventRepository eventRepository;

    @Override
    public List<CategoryDto> getAllCategories(Integer from, Integer size) {
        PageRequest pageRequest = PageRequest.of(from / size, size);
        List<Category> requests = categoryRepository.getAllCategoriesById(pageRequest).getContent();
        List<CategoryDto> categories = requests
                .stream()
                .map(request -> categoryMapper.toCategoryDto(request))
                .collect(Collectors.toList());
        return categories;
    }

    @Override
    public CategoryDto getCategoryById(Long catId) {
        return categoryMapper.toCategoryDto(categoryRepository.findById(catId).orElseThrow(
                () -> new NotFoundException("Category not found.")));
    }

    @Override
    public CategoryDto createCategory(NewCategoryDto newCategory) {
        if (categoryRepository.findFirstByName(newCategory.getName()) != null) {
            throw new ConflictException("The category already exists.");
        }
        Category category = categoryRepository.save(categoryMapper.newCategoryDtoToCategory(newCategory));
        return categoryMapper.toCategoryDto(category);
    }

    @Override
    public void deleteCategory(Long catId) {
        if (categoryRepository.getCategoryById(catId) == null) {
            throw new BadRequestException("The category does not exist.");
        }
        if (eventRepository.findFirstByCategory(catId) != null) {
            throw new ConflictException("The category is not empty.");
        }
        categoryRepository.deleteById(catId);
    }


    @Override
    public CategoryDto patchCategoryById(Long catId, NewCategoryDto updatedCategory) {
        if (categoryRepository.getCategoryById(catId) == null) {
            throw new BadRequestException("The category does not exist.");
        }
        Category categoryById = categoryRepository.getCategoryById(catId);
        Category categoryByName = categoryRepository.findFirstByName(updatedCategory.getName());
        if (categoryByName != null) {
            if (!categoryByName.getId().equals(categoryById.getId())) {
                throw new ConflictException("The category already exists.");
            }
        }
        categoryById.setName(updatedCategory.getName());
        categoryRepository.save(categoryById);
        return categoryMapper.toCategoryDto(categoryById);
    }

    @Override
    public Category getCategoryModelById(Long catId) {
        return categoryRepository.findById(catId).orElseThrow(
                () -> new NotFoundException("Category not found."));
    }
}
