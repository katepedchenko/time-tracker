package com.example.timetracker.controller;

import com.example.timetracker.dto.ActivityCreateDTO;
import com.example.timetracker.dto.ActivityReadDTO;
import com.example.timetracker.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users/{userId}/activities")
public class ActivityController {

    @Autowired
    private ActivityService activityService;

    @GetMapping
    public List<ActivityReadDTO> getUserActivities(@PathVariable UUID userId) {
        return activityService.getUserActivities(userId);
    }

    @PostMapping("/start")
    public ActivityReadDTO start(@PathVariable UUID userId, @RequestBody ActivityCreateDTO createDTO) {
        return activityService.createActivity(userId, createDTO);
    }

    @PostMapping("/{activityId}/pause")
    public ActivityReadDTO pause(@PathVariable UUID userId, @PathVariable UUID activityId) {
        return activityService.pauseActivity(userId, activityId);
    }

    @PostMapping("/{activityId}/resume")
    public ActivityReadDTO resume(@PathVariable UUID userId, @PathVariable UUID activityId) {
        return activityService.resumeActivity(userId, activityId);
    }

    @PostMapping("/{activityId}/stop")
    public ActivityReadDTO stop(@PathVariable UUID userId, @PathVariable UUID activityId) {
        return activityService.stopActivity(userId, activityId);
    }
}