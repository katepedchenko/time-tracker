package com.example.timetracker.dto;

import com.example.timetracker.domain.EntryStatus;
import lombok.Data;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class WorkdayEntryReadDTO {

    private UUID id;

    private LocalDateTime startedAt;

    private LocalDateTime finishedAt;

    private EntryStatus status;

    private UUID userId;

    private List<TimeEntryReadDTO> timeEntries;

    private Duration totalTime;
}
