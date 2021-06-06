package com.example.timetracker.dto;

import com.example.timetracker.domain.UserRoleType;
import lombok.Data;

import java.util.UUID;

@Data
public class UserRoleReadDTO {

    private UUID id;

    private UserRoleType type;
}
