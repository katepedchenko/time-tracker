package com.example.timetracker.service.impl;

import com.example.timetracker.dto.SummaryDTO;
import com.example.timetracker.service.ReportService;
import com.example.timetracker.service.SummaryService;
import com.example.timetracker.service.helper.ExcelReportHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.util.UUID;

@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private SummaryService summaryService;

    @Autowired
    private ExcelReportHelper excelReportHelper;

    @Override
    public ByteArrayInputStream exportToExcel(UUID userId, LocalDate beginDate, LocalDate endDate) {
        SummaryDTO summary = summaryService.createSummary(userId, beginDate, endDate);
        byte[] report = excelReportHelper.createReport(summary);
        return new ByteArrayInputStream(report);
    }

    @Override
    public ByteArrayInputStream exportToPDF(UUID userId, LocalDate beginDate, LocalDate endDate) {
        return new ByteArrayInputStream(new byte[0]);
    }
}
