package com.example.timetracker.service;

import com.example.timetracker.domain.*;
import com.example.timetracker.dto.ActivityCreateDTO;
import com.example.timetracker.dto.ActivityReadDTO;
import com.example.timetracker.dto.ActivityUpdateDTO;
import com.example.timetracker.exception.ActionProhibitedException;
import com.example.timetracker.repository.ActivityRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

public class ActivityServiceTest extends BaseServiceTest {

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private ActivityService activityService;

    @Test
    public void testGetUserActivities() {
        AppUser user1 = testObjectFactory.createUser("111", "111@mail.com");
        AppUser user2 = testObjectFactory.createUser("222", "222@mail.com");
        Project project = testObjectFactory.createProject();
        Activity a1 = testObjectFactory.createActivity(user1, ActivityStatus.NEW, project);
        Activity a2 = testObjectFactory.createActivity(user2, ActivityStatus.NEW, project);
        Activity a3 = testObjectFactory.createActivity(user2, ActivityStatus.NEW, project);

        List<ActivityReadDTO> userActivities = activityService.getUserActivities(user2.getId());

        Assertions.assertThat(userActivities)
                .extracting("id")
                .containsExactlyInAnyOrder(a2.getId(), a3.getId());
    }

    @Test
    public void testGetAllUserActivitiesByDate() {
        AppUser user = testObjectFactory.createUser();
        Project project = testObjectFactory.createProject();
        LocalDate date = LocalDate.of(2021, 5, 20);
        LocalDate date2 = LocalDate.of(2021, 5, 15);
        Activity a1 = testObjectFactory.createActivity(user, ActivityStatus.NEW, project, date);
        Activity a2 = testObjectFactory.createActivity(user, ActivityStatus.NEW, project, date2);

        List<ActivityReadDTO> userActivities = activityService.getUserActivitiesByDate(user.getId(), date2);

        Assertions.assertThat(userActivities)
                .extracting("id")
                .containsExactly(a2.getId());
    }

    @Test
    public void testCreateActivity() {
        AppUser user = testObjectFactory.createUser();
        Project project = testObjectFactory.createProject();

        ActivityCreateDTO createDTO = new ActivityCreateDTO();
        createDTO.setDescription("some text");
        createDTO.setHours(5);
        createDTO.setProjectId(project.getId());

        ActivityReadDTO readDTO = activityService.createActivity(user.getId(), createDTO);

        assertEquals(user.getId(), readDTO.getUserId());
        assertNotNull(readDTO.getProject());
        assertNotNull(readDTO.getDate());
        assertNotNull(readDTO.getHours());
        assertEquals(project.getId(), readDTO.getProject().getId());

        Activity entryFromDb = activityRepository.findById(readDTO.getId()).get();
        assertEquals(ActivityStatus.NEW, entryFromDb.getStatus());
        assertEquals(project.getId(), entryFromDb.getProject().getId());
    }

    @Test
    public void testUpdateActivity() {
        AppUser user = testObjectFactory.createUser();
        Project project = testObjectFactory.createProject();
        Activity activity = testObjectFactory.createActivity(user, ActivityStatus.NEW, project);

        ActivityUpdateDTO updateDTO = new ActivityUpdateDTO();
        updateDTO.setDescription("new description");
        updateDTO.setHours(9);

        ActivityReadDTO readDTO = activityService.updateActivity(user.getId(), activity.getId(), updateDTO);

        assertEquals(updateDTO.getDescription(), readDTO.getDescription());
        assertEquals(updateDTO.getHours(), readDTO.getHours());

        Activity entryFromDb = activityRepository.findById(readDTO.getId()).get();
        assertEquals(updateDTO.getDescription(), entryFromDb.getDescription());
        assertEquals(updateDTO.getHours(), entryFromDb.getHours());
    }

    @Test
    public void testUpdatePostedActivity() {
        AppUser user = testObjectFactory.createUser();
        Project project = testObjectFactory.createProject();
        Activity activity = testObjectFactory.createActivity(user, ActivityStatus.POSTED, project);

        ActivityUpdateDTO updateDTO = new ActivityUpdateDTO();
        updateDTO.setDescription("new description");
        updateDTO.setHours(9);

        assertThatThrownBy(() -> activityService.updateActivity(user.getId(), activity.getId(), updateDTO))
                .isInstanceOf(ActionProhibitedException.class);
    }

    @Test
    public void testPostActivity() {
        AppUser user = testObjectFactory.createUser();
        Project project = testObjectFactory.createProject();
        Activity activity = testObjectFactory.createActivity(user, ActivityStatus.NEW, project);

        ActivityReadDTO readDTO = activityService.postActivity(user.getId(), activity.getId());
        assertEquals(ActivityStatus.POSTED, readDTO.getStatus());

        Activity entryFromDb = activityRepository.findById(readDTO.getId()).get();
        assertEquals(ActivityStatus.POSTED, entryFromDb.getStatus());

    }

    @Test
    public void testPostAlreadyPostedActivity() {
        AppUser user = testObjectFactory.createUser();
        Project project = testObjectFactory.createProject();
        Activity activity = testObjectFactory.createActivity(user, ActivityStatus.POSTED, project);

        assertThatThrownBy(() -> activityService.postActivity(user.getId(), activity.getId()))
                .isInstanceOf(ActionProhibitedException.class);
    }

    @Test
    public void testDeleteActivity() {
        AppUser user = testObjectFactory.createUser();
        Project project = testObjectFactory.createProject();
        Activity activity = testObjectFactory.createActivity(user, ActivityStatus.NEW, project);

        activityService.deleteActivity(user.getId(), activity.getId());

        assertFalse(activityRepository.existsById(activity.getId()));
    }

    @Test
    public void testDeleteAlreadyPostedActivity() {
        AppUser user = testObjectFactory.createUser();
        Project project = testObjectFactory.createProject();
        Activity activity = testObjectFactory.createActivity(user, ActivityStatus.POSTED, project);

        assertThatThrownBy(() -> activityService.deleteActivity(user.getId(), activity.getId()))
                .isInstanceOf(ActionProhibitedException.class);

        assertTrue(activityRepository.existsById(activity.getId()));
    }
}
