package com.example.timetracker.dto;

import lombok.Data;

import java.util.List;

@Data
public class SummaryDTO {

    private AppUserReadDTO user;

    private List<ActivityReadDTO> entries;
}
