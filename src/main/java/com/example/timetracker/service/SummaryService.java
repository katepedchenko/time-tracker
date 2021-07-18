package com.example.timetracker.service;

import com.example.timetracker.dto.SummaryDTO;

import java.time.LocalDate;
import java.util.UUID;

public interface SummaryService {

    SummaryDTO createSummary(UUID userId, LocalDate beginDate, LocalDate endDate);
}
