package com.example.personalfinancemanager.dto.impl;

import com.example.personalfinancemanager.dto.ReportDayByDayDTO;

import java.util.Date;

public class ReportDayByDayDTOImpl implements ReportDayByDayDTO {

    private final Date date;

    private final Double totalSum;

    public ReportDayByDayDTOImpl(Date date, Double totalSum) {
        this.date = date;
        this.totalSum = totalSum;
    }

    @Override
    public Date getDate() {
        return date;
    }

    @Override
    public Double getTotalSum() {
        return totalSum;
    }
}
