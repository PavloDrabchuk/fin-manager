package com.example.personalfinancemanager.service;

import com.example.personalfinancemanager.model.Category;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface CategoryService {

    void createCategory(Category category);

    List<Category> getAllCategories();

    Page<Category> getAllCategoriesForPage(Integer pageNo);

    Optional<Category> getCategoryById(Long id);

    boolean updateCategoryById(long id, Category newCategory);

    void deleteCategoryById(Long id);

    void deleteAllCategories();
}
