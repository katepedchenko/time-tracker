package com.example.timetracker.service;

import com.example.timetracker.domain.*;
import com.example.timetracker.dto.ActivityCreateDTO;
import com.example.timetracker.dto.ActivityReadDTO;
import com.example.timetracker.repository.ActivityRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
        Activity a1 = testObjectFactory.createActivity(user1, EntryStatus.NEW, project);
        Activity a2 = testObjectFactory.createActivity(user2, EntryStatus.STARTED, project);
        Activity a3 = testObjectFactory.createActivity(user2, EntryStatus.PAUSED, project);

        List<ActivityReadDTO> userActivities = activityService.getUserActivities(user2.getId());

        Assertions.assertThat(userActivities)
                .extracting("id")
                .containsExactlyInAnyOrder(a2.getId(), a3.getId());
    }

    @Test
    public void testGetAllUserActivitiesByDate() {
        AppUser user = testObjectFactory.createUser();
        Project project = testObjectFactory.createProject();
        LocalDateTime time = LocalDateTime.of(2021, 5, 20, 9, 30, 0);
        LocalDateTime time2 = LocalDateTime.of(2021, 5, 15, 9, 30, 0);
        Activity a1 = testObjectFactory.createActivity(user, EntryStatus.NEW, project, time);
        Activity a2 = testObjectFactory.createActivity(user, EntryStatus.STARTED, project, time2);

        LocalDate date = LocalDate.of(time2.getYear(), time2.getMonth(), time2.getDayOfMonth());
        List<ActivityReadDTO> userActivities = activityService.getUserActivitiesByDate(user.getId(), date);

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
        createDTO.setProjectId(project.getId());

        ActivityReadDTO readDTO = activityService.createActivity(user.getId(), createDTO);

        assertThat(readDTO).hasNoNullFieldsOrPropertiesExcept("startedAt", "finishedAt");
        assertEquals(user.getId(), readDTO.getUserId());
        assertNotNull(readDTO.getProject());
        assertEquals(project.getId(), readDTO.getProject().getId());

        Activity entryFromDb = activityRepository.findById(readDTO.getId()).get();
        assertEquals(EntryStatus.NEW, entryFromDb.getStatus());
        assertEquals(project.getId(), entryFromDb.getProject().getId());
    }

    @Test
    public void testStartActivity() {
        AppUser user = testObjectFactory.createUser();
        Project project = testObjectFactory.createProject();
        Activity activity = testObjectFactory.createActivity(user, EntryStatus.NEW, project);

        ActivityReadDTO readDTO = activityService.startActivity(user.getId(), activity.getId());

        assertThat(readDTO).hasNoNullFieldsOrPropertiesExcept("finishedAt");
        assertEquals(user.getId(), readDTO.getUserId());

        Activity entryFromDb = activityRepository.findById(readDTO.getId()).get();
        assertEquals(1, entryFromDb.getTimeEntries().size());
        assertEquals(EntryStatus.STARTED, entryFromDb.getStatus());
    }

    @Test
    public void testPauseActivity() {
        AppUser user = testObjectFactory.createUser();
        Project project = testObjectFactory.createProject();
        Activity activity = testObjectFactory.createActivity(user, EntryStatus.STARTED, project);
        TimeEntry timeEntry = testObjectFactory.createNotFinishedTimeEntry(activity);

        ActivityReadDTO readDTO = activityService.pauseActivity(user.getId(), activity.getId());

        assertEquals(EntryStatus.PAUSED, readDTO.getStatus());
        assertEquals(1, readDTO.getTimeEntries().size());
        assertNotNull(readDTO.getTimeEntries().get(0).getFinishedAt());
        assertEquals(timeEntry.getId(), readDTO.getTimeEntries().get(0).getId());
    }

    @Test
    public void testResumeActivity() {
        AppUser user = testObjectFactory.createUser();
        Project project = testObjectFactory.createProject();
        Activity activity = testObjectFactory.createActivity(user, EntryStatus.STARTED, project);
        TimeEntry timeEntry = testObjectFactory.createFinishedTimeEntry(activity);

        ActivityReadDTO readDTO = activityService.resumeActivity(user.getId(), activity.getId());

        assertEquals(EntryStatus.STARTED, readDTO.getStatus());
        assertEquals(2, readDTO.getTimeEntries().size());
        assertNotNull(readDTO.getTimeEntries().get(0).getFinishedAt());
        assertNotNull(readDTO.getTimeEntries().get(1).getStartedAt());
        assertEquals(timeEntry.getId(), readDTO.getTimeEntries().get(0).getId());
    }

    @Test
    public void testStopActivity() {
        AppUser user = testObjectFactory.createUser();
        Project project = testObjectFactory.createProject();
        Activity activity = testObjectFactory.createActivity(user, EntryStatus.STARTED, project);
        TimeEntry timeEntry = testObjectFactory.createNotFinishedTimeEntry(activity);

        ActivityReadDTO readDTO = activityService.stopActivity(user.getId(), activity.getId());

        assertEquals(EntryStatus.FINISHED, readDTO.getStatus());
        assertEquals(1, readDTO.getTimeEntries().size());
        assertNotNull(readDTO.getTimeEntries().get(0).getFinishedAt());
        assertNotNull(readDTO.getTimeEntries().get(0).getStartedAt());
        assertEquals(timeEntry.getId(), readDTO.getTimeEntries().get(0).getId());
    }
}
