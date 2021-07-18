package com.example.timetracker.controller;

import com.example.timetracker.domain.ActivityStatus;
import com.example.timetracker.dto.ActivityCreateDTO;
import com.example.timetracker.dto.ActivityReadDTO;
import com.example.timetracker.dto.ActivityUpdateDTO;
import com.example.timetracker.dto.ProjectReadDTO;
import com.example.timetracker.service.ActivityService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ActivityController.class)
public class ActivityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private ActivityService activityService;

    @Test
    public void testGetUserActivities() throws Exception {
        UUID userId = UUID.randomUUID();
        ActivityReadDTO entry1 = createReadDTO(userId);
        ActivityReadDTO entry2 = createReadDTO(userId);
        ActivityReadDTO entry3 = createReadDTO(userId);

        List<ActivityReadDTO> expectedResult = List.of(entry1, entry2, entry3);

        Mockito.when(activityService.getUserActivities(userId))
                .thenReturn(expectedResult);

        String resultJson = mockMvc.perform(get("/api/v1/users/{userId}/activities", userId))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        List<ActivityReadDTO> actualResult = mapper.readValue(resultJson, new TypeReference<>() {
        });

        assertThat(actualResult).containsAll(expectedResult);

        Mockito.verify(activityService).getUserActivities(userId);
    }

    @Test
    public void testGetUserActivitiesByDate() throws Exception {
        UUID userId = UUID.randomUUID();
        ActivityReadDTO entry1 = createReadDTO(userId);
        ActivityReadDTO entry2 = createReadDTO(userId);
        ActivityReadDTO entry3 = createReadDTO(userId);
        List<ActivityReadDTO> expectedResult = List.of(entry1, entry2, entry3);

        LocalDate date = LocalDate.of(2021, 5, 22);

        Mockito.when(activityService.getUserActivitiesByDate(userId, date))
                .thenReturn(expectedResult);

        String resultJson = mockMvc.perform(get("/api/v1/users/{userId}/activities", userId)
                .param("date", date.toString()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        List<ActivityReadDTO> actualResult = mapper.readValue(resultJson, new TypeReference<>() {
        });

        assertThat(actualResult).containsAll(expectedResult);

        Mockito.verify(activityService).getUserActivitiesByDate(userId, date);
    }

    @Test
    public void testCreateActivity() throws Exception {
        UUID userId = UUID.randomUUID();
        ActivityReadDTO expectedResult = createReadDTO(userId);

        ActivityCreateDTO createDTO = new ActivityCreateDTO();
        createDTO.setDescription("some text");
        createDTO.setHours(5);
        createDTO.setProjectId(UUID.randomUUID());

        Mockito.when(activityService.createActivity(userId, createDTO))
                .thenReturn(expectedResult);

        String resultJson = mockMvc
                .perform(post("/api/v1/users/{userId}/activities", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(createDTO)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        ActivityReadDTO actualResult = mapper.readValue(resultJson, ActivityReadDTO.class);

        assertEquals(expectedResult, actualResult);

        Mockito.verify(activityService).createActivity(userId, createDTO);
    }

    @Test
    public void testUpdateActivity() throws Exception {
        UUID userId = UUID.randomUUID();
        ActivityReadDTO expectedResult = createReadDTO(userId);

        ActivityUpdateDTO updateDTO = new ActivityUpdateDTO();
        updateDTO.setDescription("some text");
        updateDTO.setHours(5);

        Mockito.when(activityService.updateActivity(userId, expectedResult.getId(), updateDTO))
                .thenReturn(expectedResult);

        String resultJson = mockMvc
                .perform(put("/api/v1/users/{userId}/activities/{activityId}",
                        userId, expectedResult.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(updateDTO)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        ActivityReadDTO actualResult = mapper.readValue(resultJson, ActivityReadDTO.class);

        assertEquals(expectedResult, actualResult);

        Mockito.verify(activityService).updateActivity(userId, expectedResult.getId(), updateDTO);
    }

    @Test
    public void testPostActivity() throws Exception {
        UUID userId = UUID.randomUUID();
        ActivityReadDTO expectedResult = createReadDTO(userId);

        Mockito.when(activityService.postActivity(userId, expectedResult.getId()))
                .thenReturn(expectedResult);

        String resultJson = mockMvc
                .perform(post("/api/v1/users/{userId}/activities/{activityId}/post",
                        userId, expectedResult.getId()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        ActivityReadDTO actualResult = mapper.readValue(resultJson, ActivityReadDTO.class);

        assertEquals(expectedResult, actualResult);

        Mockito.verify(activityService).postActivity(userId, expectedResult.getId());
    }

    @Test
    public void testDeleteActivity() throws Exception {
        UUID userId = UUID.randomUUID();
        ActivityReadDTO expectedResult = createReadDTO(userId);

        mockMvc.perform(delete("/api/v1/users/{userId}/activities/{activityId}",
                userId, expectedResult.getId()))
                .andExpect(status().isOk());

        Mockito.verify(activityService).deleteActivity(userId, expectedResult.getId());
    }

    private ActivityReadDTO createReadDTO(UUID userId) {
        ActivityReadDTO dto = new ActivityReadDTO();
        dto.setId(UUID.randomUUID());
        dto.setUserId(userId);
        dto.setDate(LocalDate.of(2021, 5, 11));
        dto.setHours(5);
        dto.setStatus(ActivityStatus.NEW);
        dto.setDescription("activity description");
        dto.setProject(creatProjectReadDTO());
        return dto;
    }

    private ProjectReadDTO creatProjectReadDTO() {
        ProjectReadDTO dto = new ProjectReadDTO();
        dto.setName("some name");
        dto.setId(UUID.randomUUID());
        return dto;
    }
}
