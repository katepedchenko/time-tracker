package com.example.timetracker.service;

import com.example.timetracker.domain.*;
import com.example.timetracker.dto.SummaryDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SummaryServiceTest extends BaseServiceTest {

    @Autowired
    private SummaryService summaryService;

    @Test
    public void testCreateSummary() {
        AppUser user = testObjectFactory.createUser();
        Project project = testObjectFactory.createProject();
        LocalDate beginDate = LocalDate.of(2021, 5, 15);
        LocalDate endDate = LocalDate.of(2021, 5, 20);
        testObjectFactory.createActivity(user, ActivityStatus.POSTED, project, beginDate);
        testObjectFactory.createActivity(user, ActivityStatus.NEW, project, endDate);

        SummaryDTO summary = summaryService.createSummary(user.getId(), beginDate, endDate);

        assertEquals(user.getId(), summary.getUser().getId());
        assertEquals(6, summary.getEntries().size());
        assertEquals(1, summary.getEntries().get(0).getActivities().size());
    }
}