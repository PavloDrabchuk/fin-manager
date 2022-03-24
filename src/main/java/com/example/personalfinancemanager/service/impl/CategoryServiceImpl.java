package com.example.personalfinancemanager.service.impl;

import com.example.personalfinancemanager.model.Category;
import com.example.personalfinancemanager.repository.CategoryRepository;
import com.example.personalfinancemanager.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final Integer PAGE_SIZE_PAGINATION = 7;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public boolean createCategory(Category category) {

        if (!categoryRepository.findAllNames().contains(category.getName())) {
            categoryRepository.save(category);
            return true;
        }
        return false;
    }


    @Override
    public List<Category> getAllCategories() {
        return (List<Category>) categoryRepository.findAll();
    }

    @Override
    public Page<Category> getAllCategoriesForPage(Integer pageNo) {
        Pageable paging = PageRequest.of(pageNo, PAGE_SIZE_PAGINATION);

        return categoryRepository.findAll(paging);
    }

    @Override
    public Optional<Category> getCategoryById(Long id) {
        return categoryRepository.findById(id);
    }

    @Override
    public int updateCategoryById(long id, Category newCategory) {
        Optional<Category> category = categoryRepository.findById(id);

        if (getAllCategoriesNames().contains(newCategory.getName())
                && category.isPresent()
                && !Objects.equals(category.get().getName(), newCategory.getName())) {
            return 2;
        }

        if (category.isPresent()) {
            category.get().setName(newCategory.getName());
            category.get().setDescription(newCategory.getDescription());

            categoryRepository.save(category.get());
            return 1;
        }
        return 0;
    }

    @Override
    public void deleteCategoryById(Long id) {
        categoryRepository.deleteById(id);
    }

    @Override
    public void deleteAllCategories() {
        categoryRepository.deleteAll();
    }

    @Override
    public List<String> getAllCategoriesNames() {
        return categoryRepository.findAllNames();
    }
}
