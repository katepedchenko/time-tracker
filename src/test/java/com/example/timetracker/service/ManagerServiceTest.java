package com.example.timetracker.service;


import com.example.timetracker.domain.Activity;
import com.example.timetracker.domain.ActivityStatus;
import com.example.timetracker.domain.AppUser;
import com.example.timetracker.domain.Project;
import com.example.timetracker.dto.ActivityReadDTO;
import com.example.timetracker.dto.ActivityUpdateDTO;
import com.example.timetracker.repository.ActivityRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ManagerServiceTest extends BaseServiceTest {

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private ManagerService managerService;

    @Test
    public void testEditPostedActivity() {
        AppUser user = testObjectFactory.createUser();
        Project project = testObjectFactory.createProject();
        Activity activity = testObjectFactory.createActivity(user, ActivityStatus.NEW, project);

        ActivityUpdateDTO updateDTO = new ActivityUpdateDTO();
        updateDTO.setDescription("new description");
        updateDTO.setHours(9);

        ActivityReadDTO readDTO = managerService.editPostedActivity(user.getId(), activity.getId(), updateDTO);

        assertEquals(updateDTO.getDescription(), readDTO.getDescription());
        assertEquals(updateDTO.getHours(), readDTO.getHours());

        Activity entryFromDb = activityRepository.findById(readDTO.getId()).get();
        assertEquals(updateDTO.getDescription(), entryFromDb.getDescription());
        assertEquals(updateDTO.getHours(), entryFromDb.getHours());
    }
}
