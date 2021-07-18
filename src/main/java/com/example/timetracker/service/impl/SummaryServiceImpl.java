package com.example.timetracker.service.impl;

import com.example.timetracker.dto.AppUserReadDTO;
import com.example.timetracker.dto.SummaryDTO;
import com.example.timetracker.dto.WorkingDayReadDTO;
import com.example.timetracker.service.ActivityService;
import com.example.timetracker.service.AppUserService;
import com.example.timetracker.service.SummaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class SummaryServiceImpl implements SummaryService {

    @Autowired
    private AppUserService appUserService;

    @Autowired
    private ActivityService activityService;

    @Override
    public SummaryDTO createSummary(UUID userId, LocalDate beginDate, LocalDate endDate) {
        SummaryDTO summaryDTO = new SummaryDTO();

        AppUserReadDTO user = appUserService.getUserById(userId);
        summaryDTO.setUser(user);

        List<WorkingDayReadDTO> entries = new ArrayList<>();

        LocalDate date = beginDate;
        while (date.compareTo(endDate) <= 0) {
            WorkingDayReadDTO entry = activityService.getUserActivitiesByDate(userId, date);
            entries.add(entry);

            date = date.plusDays(1);
        }

        summaryDTO.setEntries(entries);
        return summaryDTO;
    }
}
