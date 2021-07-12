package com.example.timetracker.controller;

import com.example.timetracker.dto.ProjectCreateDTO;
import com.example.timetracker.dto.ProjectReadDTO;
import com.example.timetracker.dto.ProjectUpdateDTO;
import com.example.timetracker.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/projects")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @GetMapping("/{id}")
    public ProjectReadDTO getProject(@PathVariable UUID id) {
        return projectService.getProject(id);
    }

    @GetMapping
    public List<ProjectReadDTO> getAllProjects() {
        return projectService.getAllProjects();
    }

    @PostMapping
    public ProjectReadDTO createProject(@RequestBody ProjectCreateDTO createDTO) {
        return projectService.createProject(createDTO);
    }

    @PutMapping("/{id}")
    public ProjectReadDTO updateProject(@PathVariable UUID id, @RequestBody ProjectUpdateDTO updateDTO) {
        return projectService.updateProject(id, updateDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteProject(@PathVariable UUID id) {
        projectService.deleteProject(id);
    }
}
