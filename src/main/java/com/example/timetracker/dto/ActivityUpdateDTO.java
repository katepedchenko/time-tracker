package com.example.timetracker.dto;

import lombok.Data;

@Data
public class ActivityUpdateDTO {

    private String description;

    private Integer hours;
}
