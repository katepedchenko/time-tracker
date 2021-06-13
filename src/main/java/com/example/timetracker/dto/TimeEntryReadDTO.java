package com.example.timetracker.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class TimeEntryReadDTO {

    private UUID id;

    private LocalDateTime startedAt;

    private LocalDateTime finishedAt;
}
