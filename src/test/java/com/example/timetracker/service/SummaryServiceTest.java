package com.example.timetracker.service;

import com.example.timetracker.domain.AppUser;
import com.example.timetracker.domain.EntryStatus;
import com.example.timetracker.domain.TimeEntry;
import com.example.timetracker.domain.WorkdayEntry;
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
        WorkdayEntry workdayEntry = testObjectFactory.createWorkdayEntry(user, EntryStatus.STARTED);
        TimeEntry timeEntry = testObjectFactory.createFinishedTimeEntry(workdayEntry);

        SummaryDTO summary = summaryService.createSummary(user.getId());

        assertEquals(user.getId(), summary.getUser().getId());
        assertEquals(1, summary.getEntries().size());
        assertEquals(workdayEntry.getId(), summary.getEntries().get(0).getId());
        assertEquals(timeEntry.getId(), summary.getEntries().get(0).getTimeEntries().get(0).getId());
    }
}