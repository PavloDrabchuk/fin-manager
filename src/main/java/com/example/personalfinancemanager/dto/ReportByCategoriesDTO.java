package com.example.personalfinancemanager.dto;

import com.example.personalfinancemanager.model.Category;

public interface ReportByCategoriesDTO {
    Category getCategory();

    Double getTotalSum();
}
