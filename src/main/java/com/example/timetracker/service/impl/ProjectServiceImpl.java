package com.example.timetracker.service.impl;

import com.example.timetracker.domain.Project;
import com.example.timetracker.domain.ProjectStatus;
import com.example.timetracker.dto.ProjectCreateDTO;
import com.example.timetracker.dto.ProjectReadDTO;
import com.example.timetracker.dto.ProjectUpdateDTO;
import com.example.timetracker.exception.EntityNotFoundException;
import com.example.timetracker.repository.ProjectRepository;
import com.example.timetracker.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private TranslationService translationService;

    @Override
    public List<ProjectReadDTO> getAllProjects() {
        List<Project> projects = projectRepository.findAllByStatus(ProjectStatus.ACTIVE);
        return translationService.translateList(projects, ProjectReadDTO.class);
    }

    @Override
    public ProjectReadDTO getProject(UUID id) {
        Project project = getProjectRequired(id);
        return translationService.translate(project, ProjectReadDTO.class);
    }

    @Override
    public ProjectReadDTO createProject(ProjectCreateDTO createDTO) {
        Project project = translationService.translate(createDTO, Project.class);
        project.setStatus(ProjectStatus.ACTIVE);
        project = projectRepository.save(project);

        return translationService.translate(project, ProjectReadDTO.class);
    }

    @Override
    public ProjectReadDTO updateProject(UUID id, ProjectUpdateDTO updateDTO) {
        Project project = getProjectRequired(id);
        translationService.map(updateDTO, project);
        project = projectRepository.save(project);

        return translationService.translate(project, ProjectReadDTO.class);
    }

    @Override
    public void deleteProject(UUID id) {
        Project project = getProjectRequired(id);
        project.setStatus(ProjectStatus.DELETED);
        projectRepository.save(project);
    }

    private Project getProjectRequired(UUID id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Project.class, id));
    }
}
