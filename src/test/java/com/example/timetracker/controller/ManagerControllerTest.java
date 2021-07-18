package com.example.timetracker.controller;

import com.example.timetracker.domain.ActivityStatus;
import com.example.timetracker.dto.ActivityReadDTO;
import com.example.timetracker.dto.ActivityUpdateDTO;
import com.example.timetracker.dto.ProjectReadDTO;
import com.example.timetracker.service.ManagerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ManagerController.class)
public class ManagerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private ManagerService managerService;

    @Test
    public void testeditPostedActivity() throws Exception {
        UUID userId = UUID.randomUUID();
        ActivityReadDTO expectedResult = createReadDTO(userId);

        ActivityUpdateDTO updateDTO = new ActivityUpdateDTO();
        updateDTO.setDescription("some text");
        updateDTO.setHours(5);

        Mockito.when(managerService.editPostedActivity(userId, expectedResult.getId(), updateDTO))
                .thenReturn(expectedResult);

        String resultJson = mockMvc
                .perform(put("/api/v1/manager/users/{userId}/activities/{activityId}",
                        userId, expectedResult.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(updateDTO)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        ActivityReadDTO actualResult = mapper.readValue(resultJson, ActivityReadDTO.class);

        assertEquals(expectedResult, actualResult);

        Mockito.verify(managerService).editPostedActivity(userId, expectedResult.getId(), updateDTO);
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
