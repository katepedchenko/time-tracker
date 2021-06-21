package com.example.timetracker.service;

import com.example.timetracker.domain.AppUser;
import com.example.timetracker.domain.EntryStatus;
import com.example.timetracker.domain.TimeEntry;
import com.example.timetracker.domain.WorkdayEntry;
import com.example.timetracker.dto.WorkdayEntryReadDTO;
import com.example.timetracker.exception.NotFinishedWorkdayException;
import com.example.timetracker.repository.WorkdayEntryRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class WorkdayEntryServiceTest extends BaseServiceTest {

    @Autowired
    private WorkdayEntryRepository workdayEntryRepository;

    @Autowired
    private WorkdayEntryService workdayEntryService;

    @Test
    public void testCreateWorkdayEntry() {
        AppUser user = testObjectFactory.createUser();

        WorkdayEntryReadDTO readDTO = workdayEntryService.createWorkdayEntry(user.getId());

        assertThat(readDTO).hasNoNullFieldsOrPropertiesExcept("finishedAt");
        assertEquals(user.getId(), readDTO.getUserId());

        WorkdayEntry entryFromDb = workdayEntryRepository.findById(readDTO.getId()).get();
        assertEquals(1, entryFromDb.getTimeEntries().size());
    }

    @Test
    public void testCreateNewWorkdayEntry_WhenNotFinishedOneExists() {
        AppUser user = testObjectFactory.createUser();

        workdayEntryService.createWorkdayEntry(user.getId());

        Assertions.assertThatThrownBy(
                () -> workdayEntryService.createWorkdayEntry(user.getId()))
                .isInstanceOf(NotFinishedWorkdayException.class)
                .hasMessageContaining(user.getId().toString());
    }

    @Test
    public void testPauseWorkdayEntry() {
        AppUser user = testObjectFactory.createUser();
        WorkdayEntry workdayEntry = testObjectFactory.createWorkdayEntry(user, EntryStatus.STARTED);
        TimeEntry timeEntry = testObjectFactory.createNotFinishedTimeEntry(workdayEntry);

        WorkdayEntryReadDTO readDTO = workdayEntryService.pauseWorkdayEntry(user.getId(), workdayEntry.getId());

        assertEquals(EntryStatus.PAUSED, readDTO.getStatus());
        assertEquals(1, readDTO.getTimeEntries().size());
        assertNotNull(readDTO.getTimeEntries().get(0).getFinishedAt());
        assertEquals(timeEntry.getId(), readDTO.getTimeEntries().get(0).getId());
    }

    @Test
    public void testResumeWorkdayEntry() {
        AppUser user = testObjectFactory.createUser();
        WorkdayEntry workdayEntry = testObjectFactory.createWorkdayEntry(user, EntryStatus.STARTED);
        TimeEntry timeEntry = testObjectFactory.createFinishedTimeEntry(workdayEntry);

        WorkdayEntryReadDTO readDTO = workdayEntryService.resumeWorkdayEntry(user.getId(), workdayEntry.getId());

        assertEquals(EntryStatus.STARTED, readDTO.getStatus());
        assertEquals(2, readDTO.getTimeEntries().size());
        assertNotNull(readDTO.getTimeEntries().get(0).getFinishedAt());
        assertNotNull(readDTO.getTimeEntries().get(1).getStartedAt());
        assertEquals(timeEntry.getId(), readDTO.getTimeEntries().get(0).getId());
    }

    @Test
    public void testStopWorkdayEntry() {
        AppUser user = testObjectFactory.createUser();
        WorkdayEntry workdayEntry = testObjectFactory.createWorkdayEntry(user, EntryStatus.STARTED);
        TimeEntry timeEntry = testObjectFactory.createNotFinishedTimeEntry(workdayEntry);

        WorkdayEntryReadDTO readDTO = workdayEntryService.stopWorkdayEntry(user.getId(), workdayEntry.getId());

        assertEquals(EntryStatus.FINISHED, readDTO.getStatus());
        assertEquals(1, readDTO.getTimeEntries().size());
        assertNotNull(readDTO.getTimeEntries().get(0).getFinishedAt());
        assertNotNull(readDTO.getTimeEntries().get(0).getStartedAt());
        assertEquals(timeEntry.getId(), readDTO.getTimeEntries().get(0).getId());
    }
}
