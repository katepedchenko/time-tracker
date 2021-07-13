package com.example.timetracker.service;

import com.example.timetracker.domain.*;
import com.example.timetracker.dto.SummaryDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SummaryServiceTest extends BaseServiceTest {

    @Autowired
    private SummaryService summaryService;

    @Test
    public void testCreateSummary() {
        AppUser user = testObjectFactory.createUser();
        Project project = testObjectFactory.createProject();
        Activity activity = testObjectFactory.createActivity(user, EntryStatus.STARTED, project);
        TimeEntry timeEntry = testObjectFactory.createFinishedTimeEntry(activity);

        SummaryDTO summary = summaryService.createSummary(user.getId());

        assertEquals(user.getId(), summary.getUser().getId());
        assertEquals(1, summary.getEntries().size());
        assertEquals(activity.getId(), summary.getEntries().get(0).getId());
        assertEquals(timeEntry.getId(), summary.getEntries().get(0).getTimeEntries().get(0).getId());
    }
}