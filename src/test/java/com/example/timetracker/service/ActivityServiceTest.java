package com.example.timetracker.service;

import com.example.timetracker.domain.*;
import com.example.timetracker.dto.ActivityCreateDTO;
import com.example.timetracker.dto.ActivityReadDTO;
import com.example.timetracker.exception.NotFinishedActivityException;
import com.example.timetracker.repository.ActivityRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ActivityServiceTest extends BaseServiceTest {

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private ActivityService activityService;

    @Test
    public void testCreateActivity() {
        AppUser user = testObjectFactory.createUser();
        Project project = testObjectFactory.createProject();

        ActivityCreateDTO createDTO = new ActivityCreateDTO();
        createDTO.setDescription("some text");
        createDTO.setProjectId(project.getId());

        ActivityReadDTO readDTO = activityService.createActivity(user.getId(), createDTO);

        assertThat(readDTO).hasNoNullFieldsOrPropertiesExcept("finishedAt");
        assertEquals(user.getId(), readDTO.getUserId());
        assertNotNull(readDTO.getProject());
        assertEquals(project.getId(), readDTO.getProject().getId());

        Activity entryFromDb = activityRepository.findById(readDTO.getId()).get();
        assertEquals(1, entryFromDb.getTimeEntries().size());
    }

    @Test
    public void testCreateNewActivity_WhenNotFinishedOneExists() {
        AppUser user = testObjectFactory.createUser();

        Project project = testObjectFactory.createProject();

        ActivityCreateDTO createDTO = new ActivityCreateDTO();
        createDTO.setDescription("some text");
        createDTO.setProjectId(project.getId());

        activityService.createActivity(user.getId(), createDTO);

        Assertions.assertThatThrownBy(
                () -> activityService.createActivity(user.getId(), createDTO))
                .isInstanceOf(NotFinishedActivityException.class)
                .hasMessageContaining(user.getId().toString());
    }

    @Test
    public void testPauseActivity() {
        AppUser user = testObjectFactory.createUser();
        Activity activity = testObjectFactory.createActivity(user, EntryStatus.STARTED);
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
        Activity activity = testObjectFactory.createActivity(user, EntryStatus.STARTED);
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
        Activity activity = testObjectFactory.createActivity(user, EntryStatus.STARTED);
        TimeEntry timeEntry = testObjectFactory.createNotFinishedTimeEntry(activity);

        ActivityReadDTO readDTO = activityService.stopActivity(user.getId(), activity.getId());

        assertEquals(EntryStatus.FINISHED, readDTO.getStatus());
        assertEquals(1, readDTO.getTimeEntries().size());
        assertNotNull(readDTO.getTimeEntries().get(0).getFinishedAt());
        assertNotNull(readDTO.getTimeEntries().get(0).getStartedAt());
        assertEquals(timeEntry.getId(), readDTO.getTimeEntries().get(0).getId());
    }
}
