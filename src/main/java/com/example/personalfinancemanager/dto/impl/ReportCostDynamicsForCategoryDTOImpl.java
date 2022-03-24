package com.example.personalfinancemanager.dto.impl;

import com.example.personalfinancemanager.dto.ReportCostDynamicsForCategoryDTO;

import java.time.Month;
import java.time.Year;

public class ReportCostDynamicsForCategoryDTOImpl implements ReportCostDynamicsForCategoryDTO {

    private final Integer year;

    private final Integer month;

    private final Double totalSum;

    public ReportCostDynamicsForCategoryDTOImpl(Integer year, Integer month, Double totalSum) {
        this.year = year;
        this.month = month;
        this.totalSum = totalSum;
    }

    @Override
    public Integer getYear() {
        return year;
    }

    @Override
    public Integer getMonth() {
        return month;
    }

    @Override
    public Double getTotalSum() {
        return totalSum;
    }
}
