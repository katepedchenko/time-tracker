package com.example.timetracker.service;

import com.example.timetracker.dto.ActivityCreateDTO;
import com.example.timetracker.dto.ActivityReadDTO;

import java.util.List;
import java.util.UUID;

public interface ActivityService {

    List<ActivityReadDTO> getUserActivities(UUID userId);

    ActivityReadDTO createActivity(UUID userId, ActivityCreateDTO createDTO);

    ActivityReadDTO pauseActivity(UUID userId, UUID workdayEntryId);

    ActivityReadDTO resumeActivity(UUID userId, UUID workdayEntryId);

    ActivityReadDTO stopActivity(UUID userId, UUID workdayEntryId);
}
