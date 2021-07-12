package com.example.timetracker.service;

import com.example.timetracker.domain.Project;
import com.example.timetracker.domain.ProjectStatus;
import com.example.timetracker.dto.ProjectCreateDTO;
import com.example.timetracker.dto.ProjectReadDTO;
import com.example.timetracker.dto.ProjectUpdateDTO;
import com.example.timetracker.exception.EntityNotFoundException;
import com.example.timetracker.repository.ProjectRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ProjectServiceTest extends BaseServiceTest {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private ProjectRepository projectRepository;

    @Test
    public void testGetProject() {
        Project expectedResult = testObjectFactory.createProject();

        ProjectReadDTO readDTO = projectService.getProject(expectedResult.getId());

        Assertions.assertThat(readDTO).isEqualToComparingFieldByField(expectedResult);
    }

    @Test()
    public void testGetProjectWrongId() {
        final UUID wrongId = UUID.randomUUID();

        Assertions.assertThatThrownBy(() -> projectService.getProject(wrongId))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining(String.valueOf(wrongId));
    }

    @Test
    public void testGetAllProjects() {
        Project p1 = testObjectFactory.createProject();
        Project p2 = testObjectFactory.createProject();
        Project p3 = testObjectFactory.createProject();

        List<ProjectReadDTO> dtoList = projectService.getAllProjects();

        Assertions.assertThat(dtoList).extracting("id")
                .containsExactlyInAnyOrder(p1.getId(), p2.getId(), p3.getId());
    }

    @Test
    public void testCreateProject() {
        ProjectCreateDTO createDTO = new ProjectCreateDTO();
        createDTO.setName("Project name");

        ProjectReadDTO readDTO = projectService.createProject(createDTO);

        Assertions.assertThat(createDTO).isEqualToComparingFieldByField(readDTO);
        assertNotNull(readDTO.getId());

        Project createdProject = projectRepository.findById(readDTO.getId()).get();
        Assertions.assertThat(readDTO).isEqualToComparingFieldByField(createdProject);
    }

    @Test
    public void testUpdateProject() {
        Project project = testObjectFactory.createProject();

        ProjectUpdateDTO updateDTO = new ProjectUpdateDTO();
        updateDTO.setName("new name");

        ProjectReadDTO readDTO = projectService.updateProject(project.getId(), updateDTO);

        Assertions.assertThat(updateDTO).isEqualToComparingFieldByField(readDTO);

        Project updatedProject = projectRepository.findById(project.getId()).get();
        Assertions.assertThat(readDTO).isEqualToComparingFieldByField(updatedProject);
    }

    @Test
    public void testDeleteProject() {
        Project project = testObjectFactory.createProject();

        projectService.deleteProject(project.getId());

        Project deletedProject = projectRepository.findById(project.getId()).get();

        assertEquals(ProjectStatus.DELETED, deletedProject.getStatus());
    }
}
