package com.example.personalfinancemanager.dto;

import com.example.personalfinancemanager.dto.impl.ReportCostDynamicsForCategoryDTOImpl;
import com.example.personalfinancemanager.dto.impl.ReportDayByDayDTOImpl;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReportCostDynamicsForCategoryDTOImplTest {

    @Test
    void testReportCostDynamicsForCategoryDTOConstructor() {

        ReportCostDynamicsForCategoryDTO reportCostDynamicsForCategory = new ReportCostDynamicsForCategoryDTOImpl(
                2022, 3,123.45);

        assertAll("reportCostDynamicsForCategory",
                () -> assertEquals(2022, reportCostDynamicsForCategory.getYear()),
                () -> assertEquals(3, reportCostDynamicsForCategory.getMonth()),
                () -> assertEquals(123.45, reportCostDynamicsForCategory.getTotalSum())
        );
    }
}
