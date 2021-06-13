package com.example.timetracker.service;

import com.example.timetracker.domain.AppUser;
import com.example.timetracker.domain.TimeEntry;
import com.example.timetracker.domain.EntryStatus;
import com.example.timetracker.domain.WorkdayEntry;
import com.example.timetracker.dto.WorkdayEntryReadDTO;
import com.example.timetracker.exception.NotFinishedWorkdayException;
import com.example.timetracker.repository.AppUserRepository;
import com.example.timetracker.repository.TimeEntryRepository;
import com.example.timetracker.repository.WorkdayEntryRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@Sql(statements = {
        "delete from time_entry",
        "delete from workday_entry",
        "delete from app_user"},
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class WorkdayEntryServiceTest {

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private WorkdayEntryRepository workdayEntryRepository;

    @Autowired
    private TimeEntryRepository timeEntryRepository;

    @Autowired
    private WorkdayEntryService workdayEntryService;

    @Test
    public void testCreateWorkdayEntry() {
        AppUser user = createUser();

        WorkdayEntryReadDTO readDTO = workdayEntryService.createWorkdayEntry(user.getId());

        assertThat(readDTO).hasNoNullFieldsOrPropertiesExcept("finishedAt");
        assertEquals(user.getId(), readDTO.getUserId());

        WorkdayEntry entryFromDb = workdayEntryRepository.findById(readDTO.getId()).get();
        assertEquals(1, entryFromDb.getTimeEntries().size());
    }

    @Test
    public void testCreateNewWorkdayEntry_WhenNotFinishedOneExists() {
        AppUser user = createUser();

        workdayEntryService.createWorkdayEntry(user.getId());

        Assertions.assertThatThrownBy(
                () -> workdayEntryService.createWorkdayEntry(user.getId()))
                .isInstanceOf(NotFinishedWorkdayException.class)
                .hasMessageContaining(user.getId().toString());
    }

    @Test
    public void testPauseWorkdayEntry() {
        AppUser user = createUser();
        WorkdayEntry workdayEntry = createWorkdayEntry(user, EntryStatus.STARTED);
        TimeEntry timeEntry = createNotFinishedTimeEntry(workdayEntry);

        WorkdayEntryReadDTO readDTO = workdayEntryService.pauseWorkdayEntry(user.getId(), workdayEntry.getId());

        assertEquals(EntryStatus.PAUSED, readDTO.getStatus());
        assertEquals(1, readDTO.getTimeEntries().size());
        assertNotNull(readDTO.getTimeEntries().get(0).getFinishedAt());
        assertEquals(timeEntry.getId(), readDTO.getTimeEntries().get(0).getId());
    }

    @Test
    public void testResumeWorkdayEntry() {
        AppUser user = createUser();
        WorkdayEntry workdayEntry = createWorkdayEntry(user, EntryStatus.STARTED);
        TimeEntry timeEntry = createFinishedTimeEntry(workdayEntry);

        WorkdayEntryReadDTO readDTO = workdayEntryService.resumeWorkdayEntry(user.getId(), workdayEntry.getId());

        assertEquals(EntryStatus.STARTED, readDTO.getStatus());
        assertEquals(2, readDTO.getTimeEntries().size());
        assertNotNull(readDTO.getTimeEntries().get(0).getFinishedAt());
        assertNotNull(readDTO.getTimeEntries().get(1).getStartedAt());
        assertEquals(timeEntry.getId(), readDTO.getTimeEntries().get(0).getId());
    }

    @Test
    public void testStopWorkdayEntry() {
        AppUser user = createUser();
        WorkdayEntry workdayEntry = createWorkdayEntry(user, EntryStatus.STARTED);
        TimeEntry timeEntry = createNotFinishedTimeEntry(workdayEntry);

        WorkdayEntryReadDTO readDTO = workdayEntryService.stopWorkdayEntry(user.getId(), workdayEntry.getId());

        assertEquals(EntryStatus.FINISHED, readDTO.getStatus());
        assertEquals(1, readDTO.getTimeEntries().size());
        assertNotNull(readDTO.getTimeEntries().get(0).getFinishedAt());
        assertNotNull(readDTO.getTimeEntries().get(0).getStartedAt());
        assertEquals(timeEntry.getId(), readDTO.getTimeEntries().get(0).getId());
    }

    private AppUser createUser() {
        AppUser user = new AppUser();
        user.setIsBlocked(Boolean.FALSE);
        user.setFullName("Charles Folk");
        user.setExternalId("122244");

        return appUserRepository.save(user);
    }

    private WorkdayEntry createWorkdayEntry(AppUser user, EntryStatus status) {
        WorkdayEntry entry = new WorkdayEntry();
        entry.setUser(user);
        entry.setStatus(status);
        entry.setStartedAt(LocalDateTime.of(2021, 5, 20, 9, 30, 0));

        return workdayEntryRepository.save(entry);
    }

    private TimeEntry createNotFinishedTimeEntry(WorkdayEntry workdayEntry) {
        TimeEntry entry = new TimeEntry();
        entry.setStartedAt(LocalDateTime.of(2021, 5, 20, 9, 30, 0));
        entry.setWorkdayEntry(workdayEntry);

        return timeEntryRepository.save(entry);
    }

    private TimeEntry createFinishedTimeEntry(WorkdayEntry workdayEntry) {
        TimeEntry entry = new TimeEntry();
        entry.setStartedAt(LocalDateTime.of(2021, 5, 20, 9, 30, 0));
        entry.setFinishedAt(LocalDateTime.of(2021, 5, 20, 12, 30, 0));
        entry.setWorkdayEntry(workdayEntry);

        return timeEntryRepository.save(entry);
    }

}
