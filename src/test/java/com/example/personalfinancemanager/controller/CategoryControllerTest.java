package com.example.personalfinancemanager.controller;

import com.example.personalfinancemanager.model.Category;
import com.example.personalfinancemanager.repository.CategoryRepository;
import com.example.personalfinancemanager.service.impl.CategoryServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class CategoryControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CategoryServiceImpl categoryService;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryController categoryController;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(categoryController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
    }

    @Test
    void testGetAllCategories() throws Exception {
        List<Category> categories = new ArrayList<>();

        Category category1 = new Category("Одяг", "Опис категорії \"Одяг\"");
        Category category2 = new Category("Харчування", "Опис категорії \"Харчування\"");

        categories.add(category1);
        categories.add(category2);

        Page<Category> categoryPage = new PageImpl<>(categories);

        when(categoryService.getAllCategoriesForPage(0)).thenReturn(categoryPage);

        mockMvc.perform(get("/categories"))
                .andExpect(status().isOk())
                .andExpect(view().name("category/categories"))
                .andExpect(forwardedUrl("category/categories"))
                .andExpect(model().attribute("categories", is(categoryPage)))
                .andExpect(model().attribute("page", is(0)));

        verify(categoryService, times(2)).getAllCategoriesForPage(0);
        verifyNoMoreInteractions(categoryService);
    }

    @Test
    void testCategoryForm() throws Exception {

        mockMvc.perform(get("/categories/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("category/new-update-category"))
                .andExpect(forwardedUrl("category/new-update-category"));

        verifyNoMoreInteractions(categoryService);
    }

    @Test
    void testCategorySubmit() throws Exception {
        Category category = new Category("Одяг", "Опис категорії \"Одяг\"");

        when(categoryService.createCategory(category)).thenReturn(true);

        mockMvc.perform(post("/categories/new")
                        .flashAttr("category", category)
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/categories"))
                .andExpect(flash().attribute("successCategorySubmitMessage",
                        "Категорію \"" + category.getName() + "\" додано."));

        verify(categoryService, times(2)).createCategory(category);
    }

    @Test
    void testCategorySubmitWithErrors() throws Exception {
        Category category = new Category("О", "Опис категорії \"Одяг\"");

        when(categoryService.createCategory(category)).thenReturn(false);

        mockMvc.perform(post("/categories/new")
                        .flashAttr("category", category)
                )
                .andExpect(status().isOk())
                .andExpect(view().name("category/new-update-category"))
                .andExpect(forwardedUrl("category/new-update-category"))
                .andExpect(model().attributeHasFieldErrors("category", "name"));
    }

    @Test
    void testCategorySubmitWithErrorsDuplicateNames() throws Exception {

        Category category1 = new Category("Одяг", "Опис категорії \"Одяг\"");

        mockMvc.perform(post("/categories/new")

                        .flashAttr("category", new Category("Одяг", "Опис другої категорії \"Одяг\""))
                )
                .andExpect(status().isOk())
                .andExpect(view().name("category/new-update-category"))
                .andExpect(forwardedUrl("category/new-update-category"))
                .andExpect(model().attribute("nameAlreadyExistError", is("Дана категорія вже існує.")));
    }

    @Test
    void testUpdateCategoryForm() throws Exception {
        Optional<Category> category = Optional.of(new Category("Одяг", "Опис категорії \"Одяг\""));

        when(categoryService.getCategoryById(1L)).thenReturn(category);

        mockMvc.perform(get("/categories/{id}/update", 1L))
                .andExpect(status().isOk())
                .andExpect(view().name("category/new-update-category"))
                .andExpect(forwardedUrl("category/new-update-category"))
                .andExpect(model().attribute("category", hasProperty("name", is("Одяг"))))
                .andExpect(model().attribute("category", hasProperty("description", is("Опис категорії \"Одяг\""))))
                .andExpect(model().attribute("updateCategory", is(true)));

        verify(categoryService, times(1)).getCategoryById(1L);
        verifyNoMoreInteractions(categoryService);
    }

    @Test
    void testFailedUpdateCategoryForm() throws Exception {
        mockMvc.perform(get("/categories/{id}/update", 1L))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/categories"))
                .andExpect(flash().attribute("failureCategoryMessage", is("Сталась помилка, спробуйте пізніше.")));

        verify(categoryService, times(1)).getCategoryById(1L);
        verifyNoMoreInteractions(categoryService);
    }

    @Test
    void testUpdateCategory() throws Exception {
        Category category = new Category("Одяг", "Опис категорії \"Одяг\"");
        Category newCategory = new Category("Одяг для риболовлі", "Опис категорії \"Одяг для риболовлі\"");

        when(categoryService.updateCategoryById(1L, newCategory)).thenReturn(1);

        mockMvc.perform(put("/categories/{id}/update", 1L)
                        .flashAttr("category", newCategory)
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/categories"))
                .andExpect(flash().attribute("successCategoryUpdateMessage",
                        is("Категорію \"" + newCategory.getName() + "\" оновлено.")));

        verify(categoryService, times(1)).updateCategoryById(1L, newCategory);
    }

    @Test
    void testUpdateCategoryWithErrors() throws Exception {
        Category category = new Category("Одяг", "Опис категорії \"Одяг\"");
        Category newCategory = new Category("О", "Опис категорії \"Одяг для риболовлі\"");

        mockMvc.perform(put("/categories/{id}/update", 1L)
                        .flashAttr("category", newCategory)
                )
                .andExpect(status().isOk())
                .andExpect(view().name("category/new-update-category"))
                .andExpect(forwardedUrl("category/new-update-category"))
                .andExpect(model().attributeHasFieldErrors("category", "name"))
                .andExpect(model().attribute("category", is(newCategory)))
                .andExpect(model().attribute("updateCategory", is(true)));
    }

    @Test
    void testUpdateCategoryWithErrorsDuplicateNames() throws Exception {
        List<Category> categories = new ArrayList<>();

        Category category1 = new Category("Одяг", "Опис категорії \"Одяг\"");
        Category category2 = new Category("Одяг для риболовлі", "Опис категорії \"Одяг для риболовлі\"");
        Category newCategory = new Category("Одяг для риболовлі", "Опис категорії \"Одяг для риболовлі\"");

        when(categoryService.updateCategoryById(1L, newCategory)).thenReturn(2);
        when(categoryService.getAllCategories()).thenReturn(categories);

        mockMvc.perform(put("/categories/{id}/update", 1L)
                        .flashAttr("category", newCategory)
                )
                .andExpect(status().isOk())
                .andExpect(view().name("category/new-update-category"))
                .andExpect(forwardedUrl("category/new-update-category"))
                .andExpect(model().attribute("updateCategory", is(true)))
                .andExpect(model().attribute("nameAlreadyExistError", is("Дана категорія вже існує.")));
    }

    @Test
    void testFailedUpdateCategory() throws Exception {
        Category category = new Category("Одяг", "Опис категорії \"Одяг\"");

        mockMvc.perform(put("/categories/{id}/update", 1L)
                        .flashAttr("category", category)
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/categories"))
                .andExpect(flash().attribute("failureCategoryMessage",
                        is("Сталась помилка, спробуйте пізніше.")));
    }

    @Test
    void testDeleteCategory() throws Exception {

        Optional<Category> category = Optional.of(new Category("Одяг", "Опис категорії \"Одяг\""));

        when(categoryService.getCategoryById(1L)).thenReturn(category);

        mockMvc.perform(delete("/categories/{id}/delete", 1L))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/categories"))
                .andExpect(flash().attribute("successCategoryDeleteMessage", is("Категорію \"" + category.get().getName() + "\" видалено.")));

        verify(categoryService, times(1)).deleteCategoryById(1L);
    }

    @Test
    void testFailedDeleteCategory() throws Exception {
        mockMvc.perform(delete("/categories/{id}/delete", 1L))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/categories"))
                .andExpect(flash().attribute("failureCategoryMessage", is("Сталась помилка, спробуйте пізніше.")));

        verify(categoryService, times(1)).getCategoryById(1L);
        verifyNoMoreInteractions(categoryService);
    }
}
