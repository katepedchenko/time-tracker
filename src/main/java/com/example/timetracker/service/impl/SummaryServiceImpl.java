package com.example.timetracker.service.impl;

import com.example.timetracker.dto.AppUserReadDTO;
import com.example.timetracker.dto.SummaryDTO;
import com.example.timetracker.dto.ActivityReadDTO;
import com.example.timetracker.service.AppUserService;
import com.example.timetracker.service.SummaryService;
import com.example.timetracker.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class SummaryServiceImpl implements SummaryService {

    @Autowired
    private AppUserService appUserService;

    @Autowired
    private ActivityService activityService;

    @Override
    public SummaryDTO createSummary(UUID userId) {
        SummaryDTO summaryDTO = new SummaryDTO();

        AppUserReadDTO user = appUserService.getUserById(userId);
        summaryDTO.setUser(user);
//
//        List<ActivityReadDTO> workdayEntries = activityService.getUserActivities(userId);
//        summaryDTO.setEntries(workdayEntries);

        return summaryDTO;
    }
}
