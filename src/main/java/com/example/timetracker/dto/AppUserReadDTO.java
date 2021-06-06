package com.example.timetracker.dto;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class AppUserReadDTO {

    private UUID id;

    private String externalId;

    private String fullName;

    private Boolean isBlocked;

    private List<UserRoleReadDTO> userRoles;
}
