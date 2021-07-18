package com.example.timetracker.service.impl;

import com.example.timetracker.domain.Activity;
import com.example.timetracker.domain.ActivityStatus;
import com.example.timetracker.domain.AppUser;
import com.example.timetracker.domain.Project;
import com.example.timetracker.dto.ActivityCreateDTO;
import com.example.timetracker.dto.ActivityReadDTO;
import com.example.timetracker.dto.ActivityUpdateDTO;
import com.example.timetracker.exception.EntityNotFoundException;
import com.example.timetracker.exception.ActionProhibitedException;
import com.example.timetracker.repository.ActivityRepository;
import com.example.timetracker.repository.AppUserRepository;
import com.example.timetracker.repository.ProjectRepository;
import com.example.timetracker.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
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
        return mapToReadDTOList(entries);
    }

    @Override
    public List<ActivityReadDTO> getUserActivitiesByDate(UUID userId, LocalDate date) {
        List<Activity> entries = activityRepository.findByUserIdAndDate(userId, date);

        // todo create new DTO with UserDTO and calculate hours by date
        return mapToReadDTOList(entries);
    }

    @Transactional
    @Override
    public ActivityReadDTO createActivity(UUID userId, ActivityCreateDTO createDTO) {
        AppUser user = getUserRequired(userId);
        Project project = getProjectRequired(createDTO.getProjectId());

        Activity activity = new Activity();
        activity.setDescription(createDTO.getDescription());
        activity.setHours(createDTO.getHours());
        activity.setProject(project);
        activity.setUser(user);
        activity.setStatus(ActivityStatus.NEW);
        activity.setDate(LocalDate.now());

        activity = activityRepository.save(activity);

        return mapToReadDTO(activity);
    }

    @Override
    public ActivityReadDTO updateActivity(UUID userId, UUID activityId, ActivityUpdateDTO updateDTO) {
        Activity activity = activityRepository.findByIdAndUserId(activityId, userId);

        if (activity.getStatus() == ActivityStatus.POSTED) {
            throw new ActionProhibitedException(activity.getId());
        }

        translationService.map(updateDTO, activity);
        activity = activityRepository.save(activity);

        return mapToReadDTO(activity);
    }

    @Override
    public ActivityReadDTO postActivity(UUID userId, UUID activityId) {
        Activity activity = activityRepository.findByIdAndUserId(activityId, userId);

        if (activity.getStatus() == ActivityStatus.POSTED) {
            throw new ActionProhibitedException(activity.getId());
        }

        activity.setStatus(ActivityStatus.POSTED);
        activity = activityRepository.save(activity);

        return mapToReadDTO(activity);
    }

    @Override
    public void deleteActivity(UUID userId, UUID activityId) {
        Activity activity = activityRepository.findByIdAndUserId(activityId, userId);

        if (activity.getStatus() == ActivityStatus.POSTED) {
            throw new ActionProhibitedException(activity.getId());
        }

        activityRepository.deleteById(activityId);
    }

    private AppUser getUserRequired(UUID id) {
        return appUserRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(AppUser.class, id));
    }

    private Project getProjectRequired(UUID id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Project.class, id));
    }

    private List<ActivityReadDTO> mapToReadDTOList(List<Activity> entries) {
        return entries.stream()
                .map(e -> {
                    ActivityReadDTO dto = translationService.translate(e, ActivityReadDTO.class);
                    dto.setUserId(e.getUser().getId());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    private ActivityReadDTO mapToReadDTO(Activity entry) {
        ActivityReadDTO dto = translationService.translate(entry, ActivityReadDTO.class);
        dto.setUserId(entry.getUser().getId());
        return dto;
    }
}
