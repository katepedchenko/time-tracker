package com.example.timetracker.controller;

import com.example.timetracker.dto.ActivityReadDTO;
import com.example.timetracker.dto.ActivityUpdateDTO;
import com.example.timetracker.service.ManagerService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/manager")
public class ManagerController {

    @Autowired
    private ManagerService managerService;

    @ApiOperation(value = "Edit already posted activity", notes = "Need MANAGER permission")
    @PutMapping("/users/{userId}/activities/{activityId}")
    public ActivityReadDTO editPostedActivity(
            @PathVariable UUID userId,
            @PathVariable UUID activityId,
            @RequestBody ActivityUpdateDTO updateDTO
    ) {
        return managerService.editPostedActivity(userId, activityId, updateDTO);
    }
}
