package com.example.timetracker.service;

import com.example.timetracker.dto.ProjectCreateDTO;
import com.example.timetracker.dto.ProjectReadDTO;
import com.example.timetracker.dto.ProjectUpdateDTO;

import java.util.List;
import java.util.UUID;

public interface ProjectService {

    List<ProjectReadDTO> getAllProjects();

    ProjectReadDTO getProject(UUID id);

    ProjectReadDTO createProject(ProjectCreateDTO createDTO);

    ProjectReadDTO updateProject(UUID id, ProjectUpdateDTO updateDTO);

    void deleteProject(UUID id);
}
