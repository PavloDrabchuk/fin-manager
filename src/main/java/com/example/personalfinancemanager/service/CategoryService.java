package com.example.personalfinancemanager.service;

import com.example.personalfinancemanager.model.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryService {

    void createCategory(Category category);

    List<Category> getAllCategories();

    Optional<Category> getCategoryById(Long id);

    boolean updateCategoryById(long id, Category newCategory);

    void deleteCategoryById(Long id);

    void deleteAllCategories();
}
