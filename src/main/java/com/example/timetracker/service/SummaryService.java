package com.example.timetracker.service;

import com.example.timetracker.dto.SummaryDTO;

import java.util.UUID;

public interface SummaryService {

    SummaryDTO createSummary(UUID userId);
}
