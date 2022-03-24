package com.example.personalfinancemanager.dto;

import java.time.Month;
import java.time.Year;

public interface ReportCostDynamicsForCategoryDTO {
    Integer getYear();

    Integer getMonth();

    Double getTotalSum();
}
