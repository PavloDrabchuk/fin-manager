package com.example.personalfinancemanager.service;

import com.example.personalfinancemanager.model.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryService {

    public void createCategory(Category category);

    public List<Category> getAllCategories();

    public Optional<Category> getCategoryById(Long id);

    public boolean updateCategoryById(long id, Category newCategory);

    public void deleteCategoryById(Long id);

    public void deleteAllCategories();
}
