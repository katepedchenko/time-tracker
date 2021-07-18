package com.example.timetracker.controller;

import com.example.timetracker.dto.ActivityCreateDTO;
import com.example.timetracker.dto.ActivityReadDTO;
import com.example.timetracker.dto.ActivityUpdateDTO;
import com.example.timetracker.dto.WorkingDayReadDTO;
import com.example.timetracker.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users/{userId}/activities")
public class ActivityController {

    @Autowired
    private ActivityService activityService;

    @GetMapping
    public WorkingDayReadDTO getUserActivities(
            @PathVariable UUID userId,
            @RequestParam(value = "date", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        if (date != null) {
            return activityService.getUserActivitiesByDate(userId, date);
        }
        return activityService.getUserActivitiesByDate(userId, LocalDate.now());
    }

    @PostMapping
    public ActivityReadDTO createActivity(
            @PathVariable UUID userId,
            @RequestBody ActivityCreateDTO createDTO
    ) {
        return activityService.createActivity(userId, createDTO);
    }

    @PutMapping("/{activityId}")
    public ActivityReadDTO updateActivity(
            @PathVariable UUID userId,
            @PathVariable UUID activityId,
            @RequestBody ActivityUpdateDTO updateDTO
    ) {
        return activityService.updateActivity(userId, activityId, updateDTO);
    }

    @PostMapping("/{activityId}/post")
    public ActivityReadDTO postActivity(@PathVariable UUID userId, @PathVariable UUID activityId) {
        return activityService.postActivity(userId, activityId);
    }

    @DeleteMapping("/{activityId}")
    public void deleteActivity(@PathVariable UUID userId, @PathVariable UUID activityId) {
        activityService.deleteActivity(userId, activityId);
    }
}