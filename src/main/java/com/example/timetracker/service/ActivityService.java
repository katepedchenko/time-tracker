package com.example.timetracker.service;

import com.example.timetracker.dto.ActivityCreateDTO;
import com.example.timetracker.dto.ActivityReadDTO;
import com.example.timetracker.dto.ActivityUpdateDTO;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface ActivityService {

    List<ActivityReadDTO> getUserActivities(UUID userId);

    List<ActivityReadDTO> getUserActivitiesByDate(UUID userId, LocalDate date);

    ActivityReadDTO createActivity(UUID userId, ActivityCreateDTO createDTO);

    ActivityReadDTO updateActivity(UUID userId, UUID activityId, ActivityUpdateDTO updateDTO);

    ActivityReadDTO postActivity(UUID userId, UUID activityId);

    void deleteActivity(UUID userId, UUID activityId);
}
