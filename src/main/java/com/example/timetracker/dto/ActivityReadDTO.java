package com.example.timetracker.dto;

import com.example.timetracker.domain.ActivityStatus;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class ActivityReadDTO {

    private UUID id;

    private LocalDate date;

    private Integer hours;

    private ActivityStatus status;

    private String description;

    private UUID userId;

    private ProjectReadDTO project;
}
