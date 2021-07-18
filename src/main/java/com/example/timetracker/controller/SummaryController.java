package com.example.timetracker.controller;

import com.example.timetracker.dto.SummaryDTO;
import com.example.timetracker.service.SummaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users/{userId}/summary")
public class SummaryController {

    @Autowired
    private SummaryService summaryService;

    @GetMapping
    public SummaryDTO getSummary(
            @PathVariable UUID userId,
            @RequestParam(value = "begin-date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate beginDate,
            @RequestParam(value = "end-date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        return summaryService.createSummary(userId, beginDate, endDate);
    }
}
