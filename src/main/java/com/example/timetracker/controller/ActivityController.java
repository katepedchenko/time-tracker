package com.example.timetracker.controller;

import com.example.timetracker.dto.ActivityCreateDTO;
import com.example.timetracker.dto.ActivityReadDTO;
import com.example.timetracker.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users/{userId}/activities")
public class ActivityController {

    @Autowired
    private ActivityService activityService;

    @GetMapping
    public List<ActivityReadDTO> getUserActivities(
            @PathVariable UUID userId,
            @RequestParam(value = "date", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        if (date != null) {
            return activityService.getUserActivitiesByDate(userId, date);
        }
        return activityService.getUserActivities(userId);
    }

    @PostMapping
    public ActivityReadDTO createActivity(
            @PathVariable UUID userId,
            @RequestBody ActivityCreateDTO createDTO
    ) {
        return activityService.createActivity(userId, createDTO);
    }

    @PostMapping("/{activityId}/start")
    public ActivityReadDTO start(@PathVariable UUID userId, @PathVariable UUID activityId) {
        return activityService.startActivity(userId, activityId);
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