package com.example.timetracker.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class ActivityCreateDTO {

    private UUID projectId;

    private String description;
}
