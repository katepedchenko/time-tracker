package com.example.timetracker.service.impl;

import com.example.timetracker.domain.AppUser;
import com.example.timetracker.domain.TimeEntry;
import com.example.timetracker.domain.EntryStatus;
import com.example.timetracker.domain.WorkdayEntry;
import com.example.timetracker.dto.WorkdayEntryReadDTO;
import com.example.timetracker.exception.EntityNotFoundException;
import com.example.timetracker.exception.NotFinishedWorkdayException;
import com.example.timetracker.repository.AppUserRepository;
import com.example.timetracker.repository.WorkdayEntryRepository;
import com.example.timetracker.service.WorkdayEntryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class WorkdayEntryServiceImpl implements WorkdayEntryService {

    @Autowired
    private WorkdayEntryRepository workdayEntryRepository;

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private TranslationService translationService;

    @Override
    public List<WorkdayEntryReadDTO> getUserWorkdayEntries(UUID userId) {
        List<WorkdayEntry> entries = workdayEntryRepository.findByUserId(userId);

        List<WorkdayEntryReadDTO> dtoList = new ArrayList<>(entries.size());

        for (WorkdayEntry entry : entries) {
            WorkdayEntryReadDTO dto = translationService.translate(entry, WorkdayEntryReadDTO.class);

            Duration duration = calculateTotalTime(entry);
            dto.setTotalTime(duration);

            dtoList.add(dto);
        }

        return dtoList;
    }

    @Transactional
    @Override
    public WorkdayEntryReadDTO createWorkdayEntry(UUID userId) {
        if (hasNotFinishedWorkdayEntries(userId)) {
            throw new NotFinishedWorkdayException(userId);
        }

        AppUser user = getUserRequired(userId);

        LocalDateTime currentTime = LocalDateTime.now();

        WorkdayEntry workdayEntry = new WorkdayEntry();
        workdayEntry.setUser(user);
        workdayEntry.setStatus(EntryStatus.STARTED);
        workdayEntry.setStartedAt(currentTime);

        TimeEntry timeEntry = new TimeEntry();
        timeEntry.setStartedAt(currentTime);
        timeEntry.setWorkdayEntry(workdayEntry);
        workdayEntry.getTimeEntries().add(timeEntry);

        workdayEntry = workdayEntryRepository.save(workdayEntry);

        WorkdayEntryReadDTO dto = translationService.translate(workdayEntry, WorkdayEntryReadDTO.class);
        dto.setTotalTime(calculateTotalTime(workdayEntry));
        dto.setUserId(workdayEntry.getUser().getId());
        return dto;
    }

    @Transactional
    @Override
    public WorkdayEntryReadDTO pauseWorkdayEntry(UUID userId, UUID workdayEntryId) {
        WorkdayEntry workdayEntry = workdayEntryRepository.findByIdAndUserId(workdayEntryId, userId);

        workdayEntry.setStatus(EntryStatus.PAUSED);

        TimeEntry lastTimeEntry = workdayEntry.getTimeEntries().stream()
                .filter(e -> e.getFinishedAt() == null)
                .findFirst().get();

        lastTimeEntry.setFinishedAt(LocalDateTime.now());

        workdayEntry = workdayEntryRepository.save(workdayEntry);

        WorkdayEntryReadDTO dto = translationService.translate(workdayEntry, WorkdayEntryReadDTO.class);
        dto.setTotalTime(calculateTotalTime(workdayEntry));
        dto.setUserId(userId);
        return dto;
    }

    @Transactional
    @Override
    public WorkdayEntryReadDTO resumeWorkdayEntry(UUID userId, UUID workdayEntryId) {
        WorkdayEntry workdayEntry = workdayEntryRepository.findByIdAndUserId(workdayEntryId, userId);

        workdayEntry.setStatus(EntryStatus.STARTED);

        TimeEntry timeEntry = new TimeEntry();
        timeEntry.setStartedAt(LocalDateTime.now());
        timeEntry.setWorkdayEntry(workdayEntry);
        workdayEntry.getTimeEntries().add(timeEntry);

        workdayEntry = workdayEntryRepository.save(workdayEntry);

        WorkdayEntryReadDTO dto = translationService.translate(workdayEntry, WorkdayEntryReadDTO.class);
        dto.setTotalTime(calculateTotalTime(workdayEntry));
        dto.setUserId(userId);
        return dto;
    }

    @Transactional
    @Override
    public WorkdayEntryReadDTO stopWorkdayEntry(UUID userId, UUID workdayEntryId) {
        WorkdayEntry workdayEntry = workdayEntryRepository.findByIdAndUserId(workdayEntryId, userId);

        workdayEntry.setStatus(EntryStatus.FINISHED);

        TimeEntry lastTimeEntry = workdayEntry.getTimeEntries().stream()
                .filter(e -> e.getFinishedAt() == null)
                .findFirst().get();

        LocalDateTime currentTime = LocalDateTime.now();
        lastTimeEntry.setFinishedAt(currentTime);
        workdayEntry.setFinishedAt(currentTime);

        workdayEntry = workdayEntryRepository.save(workdayEntry);

        WorkdayEntryReadDTO dto = translationService.translate(workdayEntry, WorkdayEntryReadDTO.class);
        dto.setTotalTime(calculateTotalTime(workdayEntry));
        dto.setUserId(userId);
        return dto;
    }

    private AppUser getUserRequired(UUID id) {
        return appUserRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(AppUser.class, id));
    }

    private boolean hasNotFinishedWorkdayEntries(UUID userId) {
        List<WorkdayEntry> notFinishedEntries = workdayEntryRepository.findByUserIdAndFinishedAtIsNull(userId);
        return !notFinishedEntries.isEmpty();
    }

    private Duration calculateTotalTime(WorkdayEntry entry) {
        return entry.getTimeEntries()
                .stream()
                .map(TimeEntry::getDuration)
                .reduce(Duration.ZERO, Duration::plus);
    }
}
