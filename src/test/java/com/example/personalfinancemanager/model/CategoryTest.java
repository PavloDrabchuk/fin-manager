package com.example.personalfinancemanager.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CategoryTest {
//    private final Category category = new Category("Одяг", "Опис категорії \"Одяг\"");
//    private final Category category2 = new Category("Харчування", "Опис категорії \"Харчування\"");

    /*@Test
    void getAllCategories(){
        List<Category> categories = List.of(new Category("name1","desc1"), new Category("name2","desc2"));

        categoryRepository.saveAll(categories);

        when(categoryRepository.findAll()).thenReturn(categories);
        //categoryService.getAllCategories();
    }*/
    @Test
    void assertCategoryConstructor() {
        Category category = new Category("Одяг", "Опис категорії \"Одяг\"");

        assertAll("category",
                () -> assertEquals("Одяг", category.getName()),
                () -> assertEquals("Опис категорії \"Одяг\"", category.getDescription())
        );
    }

    @Test
    void assertCategoryDefaultConstructor() {
        Category category = new Category();

        category.setId(1L);
        category.setName("Одяг");
        category.setDescription("Опис категорії \"Одяг\"");

        assertAll("category",
                () -> assertEquals(1L, category.getId()),
                () -> assertEquals("Одяг", category.getName()),
                () -> assertEquals("Опис категорії \"Одяг\"", category.getDescription())
        );
    }
}
