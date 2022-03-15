package com.example.personalfinancemanager.service.impl;

import com.example.personalfinancemanager.model.Category;
import com.example.personalfinancemanager.repository.CategoryRepository;
import com.example.personalfinancemanager.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public void createCategory(Category category) {
        categoryRepository.save(category);
    }

    @Override
    public List<Category> getAllCategories() {
        return (List<Category>) categoryRepository.findAll();
    }

    @Override
    public Optional<Category> getCategoryById(Long id) {
        return categoryRepository.findById(id);
    }

    @Override
    public void updateCategoryById(long id, Category newCategory) {
        Optional<Category> category = categoryRepository.findById(id);

        if (category.isPresent()) {
            category.get().setName(newCategory.getName());
            category.get().setDescription(newCategory.getDescription());

            categoryRepository.save(category.get());
        }
    }

    @Override
    public void deleteCategoryById(Long id) {
        categoryRepository.deleteById(id);
    }

    @Override
    public void deleteAllCategories() {
        categoryRepository.deleteAll();
    }
}
