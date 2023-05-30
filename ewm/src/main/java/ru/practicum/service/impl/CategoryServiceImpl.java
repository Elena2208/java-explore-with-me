package ru.practicum.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.dto.category.NewCategoryDto;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.mapper.CategoryMapper;
import ru.practicum.model.Category;
import ru.practicum.model.Event;
import ru.practicum.repository.CategoryRepository;
import ru.practicum.repository.EventsRepository;
import ru.practicum.service.CategoryService;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;
import static ru.practicum.mapper.CategoryMapper.toCategory;
import static ru.practicum.mapper.CategoryMapper.toCategoryDto;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final EventsRepository eventsRepository;

    public NewCategoryDto createCategory(NewCategoryDto newCategoryDto) {
        Category category = toCategory(newCategoryDto);
        return toCategoryDto(categoryRepository.save(category));
    }

    public void deleteCategory(Long id) {
        Category category = validCategory(id);
        List<Event> events = eventsRepository.findByCategory(category);
        if (!events.isEmpty()) {
            throw new ConflictException("ConflictException",
                    "Нельзя удалить категорию. Существуют события, связанные с категорией.");
        }
        categoryRepository.deleteById(id);
    }

    public NewCategoryDto updateCategory(Long id, NewCategoryDto newCategoryDto) {
        Category category = validCategory(id);
        ofNullable(newCategoryDto.getName()).ifPresent(category::setName);
        return toCategoryDto(categoryRepository.save(category));
    }

    public List<NewCategoryDto> getCategories(Integer from, Integer size) {
        PageRequest page = PageRequest.of(from, size);
        return categoryRepository.findAll(page).stream()
                .map(CategoryMapper::toCategoryDto)
                .collect(Collectors.toList());
    }

    public NewCategoryDto getCategory(Long id) {
        Category category = validCategory(id);
        return toCategoryDto(category);
    }

    private Category validCategory(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("NotFoundException", "Категории с таким id не найдено"));
    }
}
