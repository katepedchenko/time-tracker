package com.example.timetracker.service.impl;

import com.example.timetracker.domain.Activity;
import com.example.timetracker.dto.ActivityReadDTO;
import com.example.timetracker.dto.ActivityUpdateDTO;
import com.example.timetracker.repository.ActivityRepository;
import com.example.timetracker.service.ManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ManagerServiceImpl implements ManagerService {

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private TranslationService translationService;

    @Override
    public ActivityReadDTO editPostedActivity(UUID userId, UUID activityId, ActivityUpdateDTO updateDTO) {
        Activity activity = activityRepository.findByIdAndUserId(activityId, userId);

        translationService.map(updateDTO, activity);
        activity = activityRepository.save(activity);

        ActivityReadDTO dto = translationService.translate(activity, ActivityReadDTO.class);
        dto.setUserId(activity.getUser().getId());
        return dto;
    }
}
