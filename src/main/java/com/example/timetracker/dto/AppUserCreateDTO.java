package com.example.timetracker.dto;

import lombok.Data;

@Data
public class AppUserCreateDTO {

    private String externalId;

    private String fullName;

    private String email;

    private Integer workHoursNorm;

    private Integer allowedOvertimeHours;

    private Integer allowedPausedHours;
}
