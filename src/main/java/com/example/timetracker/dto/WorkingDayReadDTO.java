package com.example.timetracker.dto;

import lombok.Data;

import java.util.List;

@Data
public class WorkingDayReadDTO {

    private List<ActivityReadDTO> activities;

    private Integer totalTrackedHours;

    private Integer workHoursNorm;

    private Integer allowedOvertimeHours;

    private Integer allowedPausedHours;

    private Integer workHoursDelta;
}
