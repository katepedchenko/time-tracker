package com.example.timetracker.controller;

import com.example.timetracker.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/report/users/{userId}/")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @PostMapping("/xlsx")
    public ResponseEntity<InputStreamResource> exportToExcel(
            @PathVariable UUID userId,
            @RequestParam(value = "begin-date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate beginDate,
            @RequestParam(value = "end-date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        ByteArrayInputStream exportBytes = reportService.exportToExcel(userId, beginDate, endDate);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=report.xlsx");

        return ResponseEntity
                .ok()
                .headers(headers)
                .body(new InputStreamResource(exportBytes));
    }

    @PostMapping("/pdf")
    public ResponseEntity<InputStreamResource> exportToPDF(
            @PathVariable UUID userId,
            @RequestParam(value = "begin-date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate beginDate,
            @RequestParam(value = "end-date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        ByteArrayInputStream exportBytes = reportService.exportToPDF(userId, beginDate, endDate);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=report.pdf");

        return ResponseEntity
                .ok()
                .headers(headers)
                .body(new InputStreamResource(exportBytes));
    }
}
