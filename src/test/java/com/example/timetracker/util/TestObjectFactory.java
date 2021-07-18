package com.example.timetracker.util;

import com.example.timetracker.domain.*;
import com.example.timetracker.repository.ActivityRepository;
import com.example.timetracker.repository.AppUserRepository;
import com.example.timetracker.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class TestObjectFactory {

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private ProjectRepository projectRepository;

    public AppUser createUser() {
        AppUser user = new AppUser();
        user.setIsBlocked(Boolean.FALSE);
        user.setFullName("Charles Folk");
        user.setExternalId("122244");
        user.setEmail("test@mail.com");
        user.setWorkHoursNorm(9);
        user.setAllowedOvertimeHours(1);
        user.setAllowedPausedHours(1);

        return appUserRepository.save(user);
    }

    public AppUser createUser(String externalId, String email) {
        AppUser user = new AppUser();
        user.setIsBlocked(Boolean.FALSE);
        user.setFullName("Charles Folk");
        user.setExternalId(externalId);
        user.setEmail(email);
        user.setWorkHoursNorm(9);
        user.setAllowedOvertimeHours(1);
        user.setAllowedPausedHours(1);

        return appUserRepository.save(user);
    }

    public AppUser createUser(boolean isBlocked) {
        AppUser user = new AppUser();
        user.setIsBlocked(isBlocked);
        user.setFullName("Charles Folk");
        user.setExternalId("122244");
        user.setEmail("test@mail.com");
        user.setWorkHoursNorm(9);
        user.setAllowedOvertimeHours(1);
        user.setAllowedPausedHours(1);

        return appUserRepository.save(user);
    }

    public Activity createActivity(AppUser user, ActivityStatus status, Project project) {
        Activity entry = new Activity();
        entry.setUser(user);
        entry.setStatus(status);
        entry.setDate(LocalDate.of(2021, 5, 20));
        entry.setHours(4);
        entry.setDescription("some text");
        entry.setProject(project);
        return activityRepository.save(entry);
    }

    public Activity createActivity(AppUser user, ActivityStatus status, Project project, LocalDate localDate) {
        Activity entry = new Activity();
        entry.setUser(user);
        entry.setStatus(status);
        entry.setDate(localDate);
        entry.setHours(4);
        entry.setDescription("some text");
        entry.setProject(project);
        return activityRepository.save(entry);
    }

    public Project createProject() {
        Project project = new Project();
        project.setName("Project name");
        project.setStatus(ProjectStatus.ACTIVE);
        return projectRepository.save(project);
    }
}
