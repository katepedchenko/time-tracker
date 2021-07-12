package com.example.timetracker.service.impl;

import com.example.timetracker.domain.*;
import com.example.timetracker.dto.ActivityCreateDTO;
import com.example.timetracker.dto.ActivityReadDTO;
import com.example.timetracker.exception.EntityNotFoundException;
import com.example.timetracker.exception.NotFinishedActivityException;
import com.example.timetracker.repository.AppUserRepository;
import com.example.timetracker.repository.ActivityRepository;
import com.example.timetracker.repository.ProjectRepository;
import com.example.timetracker.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ActivityServiceImpl implements ActivityService {

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private TranslationService translationService;

    @Override
    public List<ActivityReadDTO> getUserActivities(UUID userId) {
        List<Activity> entries = activityRepository.findByUserId(userId);

        List<ActivityReadDTO> dtoList = new ArrayList<>(entries.size());

        for (Activity entry : entries) {
            ActivityReadDTO dto = translationService.translate(entry, ActivityReadDTO.class);

            Duration duration = calculateTotalTime(entry);
            dto.setTotalTime(duration);

            dtoList.add(dto);
        }

        return dtoList;
    }

    @Transactional
    @Override
    public ActivityReadDTO createActivity(UUID userId, ActivityCreateDTO createDTO) {
        if (hasNotFinishedActivities(userId)) {
            throw new NotFinishedActivityException(userId);
        }

        AppUser user = getUserRequired(userId);
        Project project = getProjectRequired(createDTO.getProjectId());

        LocalDateTime currentTime = LocalDateTime.now();

        Activity activity = new Activity();
        activity.setDescription(createDTO.getDescription());
        activity.setProject(project);
        activity.setUser(user);
        activity.setStatus(EntryStatus.STARTED);
        activity.setStartedAt(currentTime);

        TimeEntry timeEntry = new TimeEntry();
        timeEntry.setStartedAt(currentTime);
        timeEntry.setActivity(activity);
        activity.getTimeEntries().add(timeEntry);

        activity = activityRepository.save(activity);

        ActivityReadDTO dto = translationService.translate(activity, ActivityReadDTO.class);
        dto.setTotalTime(calculateTotalTime(activity));
        dto.setUserId(activity.getUser().getId());
        return dto;
    }

    @Transactional
    @Override
    public ActivityReadDTO pauseActivity(UUID userId, UUID workdayEntryId) {
        Activity activity = activityRepository.findByIdAndUserId(workdayEntryId, userId);

        activity.setStatus(EntryStatus.PAUSED);

        TimeEntry lastTimeEntry = activity.getTimeEntries().stream()
                .filter(e -> e.getFinishedAt() == null)
                .findFirst().get();

        lastTimeEntry.setFinishedAt(LocalDateTime.now());

        activity = activityRepository.save(activity);

        ActivityReadDTO dto = translationService.translate(activity, ActivityReadDTO.class);
        dto.setTotalTime(calculateTotalTime(activity));
        dto.setUserId(userId);
        return dto;
    }

    @Transactional
    @Override
    public ActivityReadDTO resumeActivity(UUID userId, UUID workdayEntryId) {
        Activity activity = activityRepository.findByIdAndUserId(workdayEntryId, userId);

        activity.setStatus(EntryStatus.STARTED);

        TimeEntry timeEntry = new TimeEntry();
        timeEntry.setStartedAt(LocalDateTime.now());
        timeEntry.setActivity(activity);
        activity.getTimeEntries().add(timeEntry);

        activity = activityRepository.save(activity);

        ActivityReadDTO dto = translationService.translate(activity, ActivityReadDTO.class);
        dto.setTotalTime(calculateTotalTime(activity));
        dto.setUserId(userId);
        return dto;
    }

    @Transactional
    @Override
    public ActivityReadDTO stopActivity(UUID userId, UUID workdayEntryId) {
        Activity activity = activityRepository.findByIdAndUserId(workdayEntryId, userId);

        activity.setStatus(EntryStatus.FINISHED);

        TimeEntry lastTimeEntry = activity.getTimeEntries().stream()
                .filter(e -> e.getFinishedAt() == null)
                .findFirst().get();

        LocalDateTime currentTime = LocalDateTime.now();
        lastTimeEntry.setFinishedAt(currentTime);
        activity.setFinishedAt(currentTime);

        activity = activityRepository.save(activity);

        ActivityReadDTO dto = translationService.translate(activity, ActivityReadDTO.class);
        dto.setTotalTime(calculateTotalTime(activity));
        dto.setUserId(userId);
        return dto;
    }

    private AppUser getUserRequired(UUID id) {
        return appUserRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(AppUser.class, id));
    }

    private Project getProjectRequired(UUID id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Project.class, id));
    }

    // todo
    private boolean hasNotFinishedActivities(UUID userId) {
        List<Activity> notFinishedEntries = activityRepository.findByUserIdAndFinishedAtIsNull(userId);
        return !notFinishedEntries.isEmpty();
    }

    private Duration calculateTotalTime(Activity entry) {
        return entry.getTimeEntries()
                .stream()
                .map(TimeEntry::getDuration)
                .reduce(Duration.ZERO, Duration::plus);
    }
}
