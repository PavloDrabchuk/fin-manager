package com.example.personalfinancemanager.dto;

import com.example.personalfinancemanager.dto.impl.ReportByCategoriesDTOImpl;
import com.example.personalfinancemanager.model.Category;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReportByCategoriesDTOImplTest {

    @Test
    void testReportByCategoriesDTOConstructor() {
        Category category = new Category("Одяг", "Опис категорії \"Одяг\"");

        ReportByCategoriesDTO reportByCategories = new ReportByCategoriesDTOImpl(category, 123.45);

        assertAll("reportByCategories",
                () -> assertEquals(category, reportByCategories.getCategory()),
                () -> assertEquals(123.45, reportByCategories.getTotalSum())
        );
    }
}
