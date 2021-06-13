package com.example.timetracker.controller;

import com.example.timetracker.domain.EntryStatus;
import com.example.timetracker.dto.WorkdayEntryReadDTO;
import com.example.timetracker.service.WorkdayEntryService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(WorkdayEntryController.class)
public class WorkdayEntryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private WorkdayEntryService workdayEntryService;

    @Test
    public void testGetUserWorkdayEntries() throws Exception {
        UUID userId = UUID.randomUUID();
        WorkdayEntryReadDTO entry1 = createReadDTO(userId);
        WorkdayEntryReadDTO entry2 = createReadDTO(userId);
        WorkdayEntryReadDTO entry3 = createReadDTO(userId);

        List<WorkdayEntryReadDTO> expectedResult = List.of(entry1, entry2, entry3);

        Mockito.when(workdayEntryService.getUserWorkdayEntries(userId))
                .thenReturn(expectedResult);

        String resultJson = mockMvc.perform(get("/api/v1/users/{userId}/workday-entries", userId))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        List<WorkdayEntryReadDTO> actualResult = mapper.readValue(resultJson, new TypeReference<>() {});

        assertThat(actualResult).containsAll(expectedResult);

        Mockito.verify(workdayEntryService).getUserWorkdayEntries(userId);
    }

    @Test
    public void testStart() throws Exception {
        UUID userId = UUID.randomUUID();
        WorkdayEntryReadDTO expectedResult = createReadDTO(userId);

        Mockito.when(workdayEntryService.createWorkdayEntry(userId))
                .thenReturn(expectedResult);

        String resultJson = mockMvc
                .perform(post("/api/v1/users/{userId}/workday-entries/start", userId))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        WorkdayEntryReadDTO actualResult = mapper.readValue(resultJson, WorkdayEntryReadDTO.class);

        assertEquals(expectedResult, actualResult);

        Mockito.verify(workdayEntryService).createWorkdayEntry(userId);
    }

    @Test
    public void testPause() throws Exception {
        UUID userId = UUID.randomUUID();
        WorkdayEntryReadDTO expectedResult = createReadDTO(userId);

        Mockito.when(workdayEntryService.pauseWorkdayEntry(userId, expectedResult.getId()))
                .thenReturn(expectedResult);

        String resultJson = mockMvc
                .perform(post("/api/v1/users/{userId}/workday-entries/{id}/pause",
                        userId, expectedResult.getId()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        WorkdayEntryReadDTO actualResult = mapper.readValue(resultJson, WorkdayEntryReadDTO.class);

        assertEquals(expectedResult, actualResult);

        Mockito.verify(workdayEntryService).pauseWorkdayEntry(userId, expectedResult.getId());
    }

    @Test
    public void testResume() throws Exception {
        UUID userId = UUID.randomUUID();
        WorkdayEntryReadDTO expectedResult = createReadDTO(userId);

        Mockito.when(workdayEntryService.resumeWorkdayEntry(userId, expectedResult.getId()))
                .thenReturn(expectedResult);

        String resultJson = mockMvc
                .perform(post("/api/v1/users/{userId}/workday-entries/{id}/resume",
                        userId, expectedResult.getId()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        WorkdayEntryReadDTO actualResult = mapper.readValue(resultJson, WorkdayEntryReadDTO.class);

        assertEquals(expectedResult, actualResult);

        Mockito.verify(workdayEntryService).resumeWorkdayEntry(userId, expectedResult.getId());
    }

    @Test
    public void testStop() throws Exception {
        UUID userId = UUID.randomUUID();
        WorkdayEntryReadDTO expectedResult = createReadDTO(userId);

        Mockito.when(workdayEntryService.stopWorkdayEntry(userId, expectedResult.getId()))
                .thenReturn(expectedResult);

        String resultJson = mockMvc
                .perform(post("/api/v1/users/{userId}/workday-entries/{id}/stop",
                        userId, expectedResult.getId()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        WorkdayEntryReadDTO actualResult = mapper.readValue(resultJson, WorkdayEntryReadDTO.class);

        assertEquals(expectedResult, actualResult);

        Mockito.verify(workdayEntryService).stopWorkdayEntry(userId, expectedResult.getId());
    }

    private WorkdayEntryReadDTO createReadDTO(UUID userId) {
        WorkdayEntryReadDTO dto = new WorkdayEntryReadDTO();
        dto.setId(UUID.randomUUID());
        dto.setUserId(userId);
        dto.setStartedAt(LocalDateTime.of(2021, 5, 20, 9, 30, 0));
        dto.setFinishedAt(LocalDateTime.of(2021, 5, 20, 18, 30, 0));
        dto.setStatus(EntryStatus.FINISHED);
        return dto;
    }
}
