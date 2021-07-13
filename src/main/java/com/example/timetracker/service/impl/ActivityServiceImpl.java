package com.example.timetracker.service.impl;

import com.example.timetracker.domain.*;
import com.example.timetracker.dto.ActivityCreateDTO;
import com.example.timetracker.dto.ActivityReadDTO;
import com.example.timetracker.exception.EntityNotFoundException;
import com.example.timetracker.repository.ActivityRepository;
import com.example.timetracker.repository.AppUserRepository;
import com.example.timetracker.repository.ProjectRepository;
import com.example.timetracker.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

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
        return entries.stream()
                .map(calcTotalTimeAndMapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ActivityReadDTO> getUserActivitiesByDate(UUID userId, LocalDate date) {
        List<Activity> entries = activityRepository.findByUserId(userId);
        return entries.stream()
                .filter(filterByDate(date))
                .map(calcTotalTimeAndMapToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public ActivityReadDTO createActivity(UUID userId, ActivityCreateDTO createDTO) {
        AppUser user = getUserRequired(userId);
        Project project = getProjectRequired(createDTO.getProjectId());

        Activity activity = new Activity();
        activity.setDescription(createDTO.getDescription());
        activity.setProject(project);
        activity.setUser(user);
        activity.setStatus(EntryStatus.NEW);

        activity = activityRepository.save(activity);

        ActivityReadDTO dto = translationService.translate(activity, ActivityReadDTO.class);
        dto.setTotalTime(calculateTotalTime(activity));
        dto.setUserId(activity.getUser().getId());
        return dto;
    }

    @Transactional
    @Override
    public ActivityReadDTO startActivity(UUID userId, UUID activityId) {
        LocalDateTime currentTime = LocalDateTime.now();

        Activity activity = activityRepository.findByIdAndUserId(activityId, userId);
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
    public ActivityReadDTO pauseActivity(UUID userId, UUID activityId) {
        Activity activity = activityRepository.findByIdAndUserId(activityId, userId);

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
    public ActivityReadDTO resumeActivity(UUID userId, UUID activityId) {
        Activity activity = activityRepository.findByIdAndUserId(activityId, userId);

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
    public ActivityReadDTO stopActivity(UUID userId, UUID activityId) {
        Activity activity = activityRepository.findByIdAndUserId(activityId, userId);

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

    private Predicate<Activity> filterByDate(LocalDate date) {
        return entry -> {
            LocalDateTime ldt = entry.getStartedAt();
            LocalDate startedDate = LocalDate.of(ldt.getYear(), ldt.getMonth(), ldt.getDayOfMonth());
            return date.equals(startedDate);
        };
    }

    private final Function<Activity, ActivityReadDTO> calcTotalTimeAndMapToDTO = entry -> {
        Duration duration = calculateTotalTime(entry);
        ActivityReadDTO dto = translationService.translate(entry, ActivityReadDTO.class);
        dto.setTotalTime(duration);
        return dto;
    };

    private Duration calculateTotalTime(Activity entry) {
        return entry.getTimeEntries()
                .stream()
                .map(TimeEntry::getDuration)
                .reduce(Duration.ZERO, Duration::plus);
    }
}
