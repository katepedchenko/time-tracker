package com.example.timetracker.service.impl;

import com.example.timetracker.dto.AppUserReadDTO;
import com.example.timetracker.dto.SummaryDTO;
import com.example.timetracker.dto.WorkdayEntryReadDTO;
import com.example.timetracker.service.AppUserService;
import com.example.timetracker.service.SummaryService;
import com.example.timetracker.service.WorkdayEntryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class SummaryServiceImpl implements SummaryService {

    @Autowired
    private AppUserService appUserService;

    @Autowired
    private WorkdayEntryService workdayEntryService;

    @Override
    public SummaryDTO createSummary(UUID userId) {
        SummaryDTO summaryDTO = new SummaryDTO();

        AppUserReadDTO user = appUserService.getUserById(userId);
        summaryDTO.setUser(user);

        List<WorkdayEntryReadDTO> workdayEntries = workdayEntryService.getUserWorkdayEntries(userId);
        summaryDTO.setEntries(workdayEntries);

        return summaryDTO;
    }
}
