package com.example.timetracker.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class ProjectReadDTO {

    private UUID id;

    private String name;
}
