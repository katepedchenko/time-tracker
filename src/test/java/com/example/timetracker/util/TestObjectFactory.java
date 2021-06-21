package com.example.timetracker.util;

import com.example.timetracker.domain.AppUser;
import com.example.timetracker.domain.EntryStatus;
import com.example.timetracker.domain.TimeEntry;
import com.example.timetracker.domain.WorkdayEntry;
import com.example.timetracker.repository.AppUserRepository;
import com.example.timetracker.repository.TimeEntryRepository;
import com.example.timetracker.repository.WorkdayEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class TestObjectFactory {

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private WorkdayEntryRepository workdayEntryRepository;

    @Autowired
    private TimeEntryRepository timeEntryRepository;

    public AppUser createUser() {
        AppUser user = new AppUser();
        user.setIsBlocked(Boolean.FALSE);
        user.setFullName("Charles Folk");
        user.setExternalId("122244");

        return appUserRepository.save(user);
    }

    public AppUser createUser(boolean isBlocked) {
        AppUser user = new AppUser();
        user.setIsBlocked(isBlocked);
        user.setFullName("Charles Folk");
        user.setExternalId("122244");

        return appUserRepository.save(user);
    }

    public TimeEntry createFinishedTimeEntry(WorkdayEntry workdayEntry) {
        TimeEntry entry = new TimeEntry();
        entry.setStartedAt(LocalDateTime.of(2021, 5, 20, 9, 30, 0));
        entry.setFinishedAt(LocalDateTime.of(2021, 5, 20, 12, 30, 0));
        entry.setWorkdayEntry(workdayEntry);

        return timeEntryRepository.save(entry);
    }

    public TimeEntry createNotFinishedTimeEntry(WorkdayEntry workdayEntry) {
        TimeEntry entry = new TimeEntry();
        entry.setStartedAt(LocalDateTime.of(2021, 5, 20, 9, 30, 0));
        entry.setWorkdayEntry(workdayEntry);

        return timeEntryRepository.save(entry);
    }

    public WorkdayEntry createWorkdayEntry(AppUser user, EntryStatus status) {
        WorkdayEntry entry = new WorkdayEntry();
        entry.setUser(user);
        entry.setStatus(status);
        entry.setStartedAt(LocalDateTime.of(2021, 5, 20, 9, 30, 0));

        return workdayEntryRepository.save(entry);
    }

}
