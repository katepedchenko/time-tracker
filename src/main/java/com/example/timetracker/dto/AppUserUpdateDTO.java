package com.example.timetracker.dto;

import lombok.Data;

@Data
public class AppUserUpdateDTO {

    private String externalId;

    private String fullName;

    private Boolean isBlocked;
}
