package com.example.timetracker.service;

import com.example.timetracker.dto.ActivityCreateDTO;
import com.example.timetracker.dto.ActivityReadDTO;
import com.example.timetracker.dto.ActivityUpdateDTO;
import com.example.timetracker.dto.WorkingDayReadDTO;

import java.time.LocalDate;
import java.util.UUID;

public interface ActivityService {

    WorkingDayReadDTO getUserActivitiesByDate(UUID userId, LocalDate date);

    ActivityReadDTO createActivity(UUID userId, ActivityCreateDTO createDTO);

    ActivityReadDTO updateActivity(UUID userId, UUID activityId, ActivityUpdateDTO updateDTO);

    ActivityReadDTO postActivity(UUID userId, UUID activityId);

    void deleteActivity(UUID userId, UUID activityId);
}
