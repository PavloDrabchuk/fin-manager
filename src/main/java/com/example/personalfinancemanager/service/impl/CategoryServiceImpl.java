package com.example.personalfinancemanager.service.impl;

import com.example.personalfinancemanager.model.Category;
import com.example.personalfinancemanager.repository.CategoryRepository;
import com.example.personalfinancemanager.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final Integer PAGE_SIZE_PAGINATION = 3;

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
    public Page<Category> getAllCategoriesForPage(Integer pageNo) {
        if (pageNo < 0) pageNo = 0;

        Pageable paging = PageRequest.of(pageNo, PAGE_SIZE_PAGINATION);

        return categoryRepository.findAll(paging);
    }

    @Override
    public Optional<Category> getCategoryById(Long id) {
        return categoryRepository.findById(id);
    }

    @Override
    public boolean updateCategoryById(long id, Category newCategory) {
        Optional<Category> category = categoryRepository.findById(id);

        if (category.isPresent()) {
            category.get().setName(newCategory.getName());
            category.get().setDescription(newCategory.getDescription());

            categoryRepository.save(category.get());
            return true;
        }
        return false;
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
