package com.example.personalfinancemanager.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CategoryTest {

    @Test
    void testCategoryConstructor() {
        Category category = new Category("Одяг", "Опис категорії \"Одяг\"");

        assertAll("category",
                () -> assertEquals("Одяг", category.getName()),
                () -> assertEquals("Опис категорії \"Одяг\"", category.getDescription())
        );
    }

    @Test
    void testCategoryDefaultConstructor() {
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
