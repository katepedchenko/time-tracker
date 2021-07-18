package com.example.timetracker.controller;

import com.example.timetracker.domain.ActivityStatus;
import com.example.timetracker.domain.UserRoleType;
import com.example.timetracker.dto.*;
import com.example.timetracker.service.SummaryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SummaryController.class)
public class SummaryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private SummaryService summaryService;

    @Test
    public void testCreateSummary() throws Exception {
        AppUserReadDTO user = createUserReadDTO(UserRoleType.USER);
        WorkingDayReadDTO w1 = createWorkingDayDTO(user.getId(), 9);
        WorkingDayReadDTO w2 = createWorkingDayDTO(user.getId(), 9);
        SummaryDTO expectedResult = createSummaryDTO(user, List.of(w1, w2));

        LocalDate beginDate = LocalDate.of(2021, 5, 11);
        LocalDate endDate = LocalDate.of(2021, 5, 11);

        Mockito.when(summaryService.createSummary(user.getId(), beginDate, endDate))
                .thenReturn(expectedResult);

        String resultJson = mockMvc
                .perform(get("/api/v1/users/{id}/summary", user.getId())
                    .param("begin-date", beginDate.toString())
                    .param("end-date", endDate.toString()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        SummaryDTO actualResult = mapper.readValue(resultJson, SummaryDTO.class);

        Assertions.assertEquals(expectedResult, actualResult);

        Mockito.verify(summaryService).createSummary(user.getId(), beginDate, endDate);
    }

    private SummaryDTO createSummaryDTO(AppUserReadDTO userDTO, List<WorkingDayReadDTO> entries) {
        SummaryDTO dto = new SummaryDTO();
        dto.setEntries(entries);
        dto.setUser(userDTO);
        return dto;
    }

    private WorkingDayReadDTO createWorkingDayDTO(UUID userId, int workHoursNorm) {
        WorkingDayReadDTO dto = new WorkingDayReadDTO();
        List<ActivityReadDTO> activities = List.of(createActivityReadDTO(userId), createActivityReadDTO(userId));
        dto.setActivities(activities);
        dto.setWorkHoursNorm(workHoursNorm);
        dto.setAllowedOvertimeHours(1);
        dto.setAllowedPausedHours(1);

        int totalHours = activities.stream().mapToInt(ActivityReadDTO::getHours).sum();
        dto.setTotalTrackedHours(totalHours);
        dto.setWorkHoursDelta(totalHours - workHoursNorm);
        return dto;
    }

    private AppUserReadDTO createUserReadDTO(UserRoleType roleType) {
        AppUserReadDTO dto = new AppUserReadDTO();
        dto.setId(UUID.randomUUID());
        dto.setFullName("Marcus Bowie");
        dto.setExternalId("2233455");
        dto.setUserRoles(List.of(createRoleReadDTO(roleType)));

        return dto;
    }

    private UserRoleReadDTO createRoleReadDTO(UserRoleType roleType) {
        UserRoleReadDTO dto = new UserRoleReadDTO();
        dto.setId(UUID.randomUUID());
        dto.setType(roleType);
        return dto;
    }

    private ActivityReadDTO createActivityReadDTO(UUID userId) {
        ActivityReadDTO dto = new ActivityReadDTO();
        dto.setId(UUID.randomUUID());
        dto.setUserId(userId);
        dto.setDate(LocalDate.of(2021, 5, 11));
        dto.setHours(4);
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
