package ru.practicum.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Category findFirstByName(String name);

    @Query(value = "SELECT * " +
            "FROM categories AS c " +
            "WHERE c.category_id = ?1 " +
            "FETCH FIRST 1 ROWS ONLY ", nativeQuery = true)
    Category getCategoryById(Long catId);

    @Query(value = "SELECT * " +
            "FROM categories AS c " +
            "ORDER BY c.category_id ASC ", nativeQuery = true)
    Page<Category> getAllCategoriesById(PageRequest pageRequest);
}