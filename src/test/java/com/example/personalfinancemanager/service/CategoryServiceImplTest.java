package com.example.personalfinancemanager.service;

import com.example.personalfinancemanager.model.Category;
import com.example.personalfinancemanager.repository.CategoryRepository;
import com.example.personalfinancemanager.service.impl.CategoryServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class CategoryServiceImplTest {

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Mock
    private CategoryRepository categoryRepository;

    @Test
    void testCreateOrSaveCategory() {
        Category category = new Category("Харчування", "Опис категорії \"Харчування\"");

        assertTrue(categoryService.createCategory(category));

        verify(categoryRepository, times(1)).save(category);
    }

    @Test
    void testGetAllCategories() {
        List<Category> categories = new ArrayList<>();
        Category category1 = new Category("Одяг", "Опис категорії \"Одяг\"");
        Category category2 = new Category("Харчування", "Опис категорії \"Харчування\"");

        categories.add(category1);
        categories.add(category2);

        when(categoryRepository.findAll()).thenReturn(categories);

        List<Category> categoryList = categoryService.getAllCategories();

        assertEquals(2, categoryList.size());
        verify(categoryRepository, times(1)).findAll();
    }

    @Test
    void testGetAllCategoriesForPage() {
        List<Category> categories = new ArrayList<>();
        Category category1 = new Category("Одяг", "Опис категорії \"Одяг\"");
        Category category2 = new Category("Харчування", "Опис категорії \"Харчування\"");

        categories.add(category1);
        categories.add(category2);

        Page<Category> categoryPage = new PageImpl<>(categories);

        Pageable paging = PageRequest.of(0, 7);

        when(categoryRepository.findAll(paging)).thenReturn(categoryPage);

        Page<Category> categoriesForPage = categoryService.getAllCategoriesForPage(0);

        assertAll("categoriesForPage",
                () -> assertEquals(1, categoriesForPage.getTotalPages()),
                () -> assertEquals(2, categoriesForPage.getTotalElements()),
                () -> assertEquals(categories, categoriesForPage.getContent()));

        verify(categoryRepository, times(1)).findAll(paging);
    }

    @Test
    void testGetCategoryById() {
        Optional<Category> category1 = Optional.of(new Category("Одяг", "Опис категорії \"Одяг\""));

        when(categoryRepository.findById(1L)).thenReturn(category1);

        Optional<Category> category = categoryService.getCategoryById(1L);

        category.ifPresent(value -> assertAll("category",
                () -> assertEquals("Одяг", value.getName()),
                () -> assertEquals("Опис категорії \"Одяг\"", value.getDescription())
        ));

        verify(categoryRepository, times(1)).findById(1L);
    }


    @Test
    void testUpdateCategoryById() {
        Optional<Category> category = Optional.of(new Category("Одяг", "Опис категорії \"Одяг\""));
        Category newCategory = new Category("Одяг_upd", "Опис категорії \"Одяг\"_upd");

        when(categoryRepository.findById(1L)).thenReturn(category);

        int updateAnswer = categoryService.updateCategoryById(1L, newCategory);

        Optional<Category> updatedCategory = categoryService.getCategoryById(1L);


        updatedCategory.ifPresent(value -> assertAll("updatedCategory",
                () -> assertEquals(1, updateAnswer),
                () -> assertEquals("Одяг_upd", value.getName()),
                () -> assertEquals("Опис категорії \"Одяг\"_upd", value.getDescription())
        ));
        verify(categoryRepository, times(2)).findById(1L);
    }

    @Test
    void testFailedUpdateCategoryById() {
        Category newCategory = new Category("Одяг_upd", "Опис категорії \"Одяг\"_upd");

        int updateAnswer = categoryService.updateCategoryById(2L, newCategory);

        assertEquals(0, updateAnswer);
    }

    @Test
    void testDeleteCategoryById() {
        Category category = new Category("Одяг", "Опис категорії \"Одяг\"");

        categoryService.deleteCategoryById(1L);

        Optional<Category> category1 = categoryService.getCategoryById(1L);

        assertTrue(category1.isEmpty());

        verify(categoryRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteAllCategoriesById() {
        Category category1 = new Category("Одяг", "Опис категорії \"Одяг\"");
        Category category2 = new Category("Харчування", "Опис категорії \"Харчування\"");

        categoryService.deleteAllCategories();

        List<Category> categoriesAfterDeletion = categoryService.getAllCategories();

        assertTrue(categoriesAfterDeletion.isEmpty());

        verify(categoryRepository, times(1)).deleteAll();
    }

    @Test
    void testGetAllCategoriesNames() {
        Category category1 = new Category("Одяг", "Опис категорії \"Одяг\"");
        Category category2 = new Category("Харчування", "Опис категорії \"Харчування\"");

        List<String> categoriesNames = new ArrayList<>();
        categoriesNames.add("Одяг");
        categoriesNames.add("Харчування");

        when(categoryRepository.findAllNames()).thenReturn(categoriesNames);

        List<String> categoriesNamesQueried = categoryService.getAllCategoriesNames();

        assertAll("categoriesNamesQueried",
                () -> assertEquals(2, categoriesNamesQueried.size()),
                () -> assertFalse(categoriesNamesQueried.isEmpty()),
                () -> assertEquals("Одяг", categoriesNamesQueried.get(0)),
                () -> assertEquals("Харчування", categoriesNamesQueried.get(1)));

        verify(categoryRepository, times(1)).findAllNames();
    }
}
