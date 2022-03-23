package com.example.personalfinancemanager.dto.impl;

import com.example.personalfinancemanager.dto.ReportByCategoriesDTO;
import com.example.personalfinancemanager.model.Category;

public class ReportByCategoriesDTOImpl implements ReportByCategoriesDTO {

    private final Category category;

    private final Double totalSum;

    public ReportByCategoriesDTOImpl(Category category, Double totalSum) {
        this.category = category;
        this.totalSum = totalSum;
    }

    @Override
    public Category getCategory() {
        return category;
    }

    @Override
    public Double getTotalSum() {
        return totalSum;
    }
}
