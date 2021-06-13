package com.example.timetracker.service;

import com.example.timetracker.dto.WorkdayEntryReadDTO;

import java.util.List;
import java.util.UUID;

public interface WorkdayEntryService {

    List<WorkdayEntryReadDTO> getUserWorkdayEntries(UUID userId);

    WorkdayEntryReadDTO createWorkdayEntry(UUID userId);

    WorkdayEntryReadDTO pauseWorkdayEntry(UUID userId, UUID workdayEntryId);

    WorkdayEntryReadDTO resumeWorkdayEntry(UUID userId, UUID workdayEntryId);

    WorkdayEntryReadDTO stopWorkdayEntry(UUID userId, UUID workdayEntryId);
}
