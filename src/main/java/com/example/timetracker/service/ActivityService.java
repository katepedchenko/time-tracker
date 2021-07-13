package com.example.timetracker.service;

import com.example.timetracker.dto.ActivityCreateDTO;
import com.example.timetracker.dto.ActivityReadDTO;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface ActivityService {

    List<ActivityReadDTO> getUserActivities(UUID userId);

    List<ActivityReadDTO> getUserActivitiesByDate(UUID userId, LocalDate date);

    ActivityReadDTO createActivity(UUID userId, ActivityCreateDTO createDTO);

    ActivityReadDTO startActivity(UUID userId, UUID activityId);

    ActivityReadDTO pauseActivity(UUID userId, UUID activityId);

    ActivityReadDTO resumeActivity(UUID userId, UUID activityId);

    ActivityReadDTO stopActivity(UUID userId, UUID activityId);
}
