package com.example.timetracker.controller;

import com.example.timetracker.dto.ProjectCreateDTO;
import com.example.timetracker.dto.ProjectReadDTO;
import com.example.timetracker.dto.ProjectUpdateDTO;
import com.example.timetracker.service.ProjectService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProjectController.class)
public class ProjectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private ProjectService projectService;

    @Test
    public void testGetProject() throws Exception {
        ProjectReadDTO expectedResult = createProjectReadDTO();

        Mockito.when(projectService.getProject(expectedResult.getId()))
                .thenReturn(expectedResult);

        String resultJson = mockMvc
                .perform(get("/api/v1/projects/{id}", expectedResult.getId()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        ProjectReadDTO actualResult = mapper.readValue(resultJson, ProjectReadDTO.class);

        assertEquals(expectedResult, actualResult);

        Mockito.verify(projectService).getProject(expectedResult.getId());
    }

    @Test
    public void testGetAllProjects() throws Exception {
        ProjectReadDTO p1 = createProjectReadDTO();
        ProjectReadDTO p2 = createProjectReadDTO();
        ProjectReadDTO p3 = createProjectReadDTO();

        List<ProjectReadDTO> expectedResult = List.of(p1, p2, p3);

        Mockito.when(projectService.getAllProjects())
                .thenReturn(expectedResult);

        String resultJson = mockMvc
                .perform(get("/api/v1/projects"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        List<ProjectReadDTO> actualResult = mapper.readValue(resultJson, new TypeReference<>() {
        });

        assertThat(actualResult).containsAll(expectedResult);

        Mockito.verify(projectService).getAllProjects();
    }


    @Test
    public void testCreateProject() throws Exception {
        ProjectCreateDTO createDTO = new ProjectCreateDTO();
        createDTO.setName("Project name");

        ProjectReadDTO expectedResult = createProjectReadDTO();

        Mockito.when(projectService.createProject(createDTO))
                .thenReturn(expectedResult);

        String resultJson = mockMvc
                .perform(post("/api/v1/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(createDTO)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        ProjectReadDTO actualResult = mapper.readValue(resultJson, ProjectReadDTO.class);

        assertEquals(expectedResult, actualResult);

        Mockito.verify(projectService).createProject(createDTO);
    }


    @Test
    public void testUpdateProject() throws Exception {
        ProjectUpdateDTO updateDTO = new ProjectUpdateDTO();
        updateDTO.setName("new project name");

        ProjectReadDTO expectedResult = createProjectReadDTO();

        Mockito.when(projectService.updateProject(expectedResult.getId(), updateDTO))
                .thenReturn(expectedResult);

        String resultJson = mockMvc
                .perform(put("/api/v1/projects/{id}", expectedResult.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(updateDTO)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        ProjectReadDTO actualResult = mapper.readValue(resultJson, ProjectReadDTO.class);

        assertEquals(expectedResult, actualResult);

        Mockito.verify(projectService).updateProject(expectedResult.getId(), updateDTO);
    }

    @Test
    public void testDeleteProject() throws Exception {
        UUID projectId = UUID.randomUUID();

        mockMvc.perform(delete("/api/v1/projects/{id}", projectId))
                .andExpect(status().isOk());

        Mockito.verify(projectService).deleteProject(projectId);
    }

    private ProjectReadDTO createProjectReadDTO() {
        ProjectReadDTO dto = new ProjectReadDTO();
        dto.setId(UUID.randomUUID());
        dto.setName("Project name");
        return dto;
    }
}
