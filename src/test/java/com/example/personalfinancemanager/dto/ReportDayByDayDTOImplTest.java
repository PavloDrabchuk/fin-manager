package com.example.personalfinancemanager.dto;

import com.example.personalfinancemanager.dto.impl.ReportDayByDayDTOImpl;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReportDayByDayDTOImplTest {

    @Test
    void testReportDayByDayDTOConstructor() {
        Date date = new Date(1647820800000L);

        ReportDayByDayDTO reportDayByDay = new ReportDayByDayDTOImpl(date, 123.45);

        assertAll("reportDayByDay",
                () -> assertEquals(date, reportDayByDay.getDate()),
                () -> assertEquals(123.45, reportDayByDay.getTotalSum())
        );
    }
}
