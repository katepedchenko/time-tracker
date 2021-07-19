package com.example.timetracker.service;

import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.util.UUID;

public interface ReportService {

    ByteArrayInputStream exportToExcel(UUID userId, LocalDate beginDate, LocalDate endDate);

    ByteArrayInputStream exportToPDF(UUID userId, LocalDate beginDate, LocalDate endDate);
}
