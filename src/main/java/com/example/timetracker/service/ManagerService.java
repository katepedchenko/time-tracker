package com.example.timetracker.service;

import com.example.timetracker.dto.ActivityReadDTO;
import com.example.timetracker.dto.ActivityUpdateDTO;

import java.util.UUID;

public interface ManagerService {

    ActivityReadDTO editPostedActivity(UUID userId, UUID activityId, ActivityUpdateDTO updateDTO);
}
