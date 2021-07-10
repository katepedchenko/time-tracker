package com.example.timetracker.controller;

import com.example.timetracker.domain.UserRoleType;
import com.example.timetracker.dto.AppUserCreateDTO;
import com.example.timetracker.dto.AppUserReadDTO;
import com.example.timetracker.dto.AppUserUpdateDTO;
import com.example.timetracker.dto.UserRoleReadDTO;
import com.example.timetracker.service.AppUserService;
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

@WebMvcTest(AppUserController.class)
public class AppUserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private AppUserService appUserService;

    @Test
    public void testGetUser() throws Exception {
        AppUserReadDTO expectedResult = createUserReadDTO(UserRoleType.USER);

        Mockito.when(appUserService.getUserById(expectedResult.getId()))
                .thenReturn(expectedResult);

        String resultJson = mockMvc
                .perform(get("/api/v1/users/{id}", expectedResult.getId()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        AppUserReadDTO actualResult = mapper.readValue(resultJson, AppUserReadDTO.class);

        assertEquals(expectedResult, actualResult);

        Mockito.verify(appUserService).getUserById(expectedResult.getId());
    }

    @Test
    public void testGetAllUser() throws Exception {
        AppUserReadDTO u1 = createUserReadDTO(UserRoleType.USER);
        AppUserReadDTO u2 = createUserReadDTO(UserRoleType.USER);
        AppUserReadDTO u3 = createUserReadDTO(UserRoleType.USER);

        List<AppUserReadDTO> expectedResult = List.of(u1, u2, u3);

        Mockito.when(appUserService.getAllUsers())
                .thenReturn(expectedResult);

        String resultJson = mockMvc
                .perform(get("/api/v1/users"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        List<AppUserReadDTO> actualResult = mapper.readValue(resultJson, new TypeReference<>() {});

        assertThat(actualResult).containsAll(expectedResult);

        Mockito.verify(appUserService).getAllUsers();
    }

    @Test
    public void testCreateUser() throws Exception {
        AppUserCreateDTO createDTO = new AppUserCreateDTO();
        createDTO.setExternalId("2233455");
        createDTO.setFullName("Marcus Bowie");
        createDTO.setWorkHoursNorm(9);
        createDTO.setEmail("test@mail.com");
        createDTO.setAllowedOvertimeHours(1);
        createDTO.setAllowedPausedHours(1);

        AppUserReadDTO expectedResult = createUserReadDTO(UserRoleType.USER);

        Mockito.when(appUserService.createUser(createDTO))
                .thenReturn(expectedResult);

        String resultJson = mockMvc
                .perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(createDTO)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        AppUserReadDTO actualResult = mapper.readValue(resultJson, AppUserReadDTO.class);

        assertEquals(expectedResult, actualResult);

        Mockito.verify(appUserService).createUser(createDTO);
    }

    @Test
    public void testUpdateUser() throws Exception {
        AppUserUpdateDTO updateDTO = new AppUserUpdateDTO();
        updateDTO.setExternalId("2233455");
        updateDTO.setFullName("Marcus Bowie");
        updateDTO.setIsBlocked(Boolean.FALSE);
        updateDTO.setWorkHoursNorm(9);
        updateDTO.setEmail("test@mail.com");
        updateDTO.setAllowedOvertimeHours(1);
        updateDTO.setAllowedPausedHours(1);

        AppUserReadDTO expectedResult = createUserReadDTO(UserRoleType.USER);

        Mockito.when(appUserService.updateUser(expectedResult.getId(), updateDTO))
                .thenReturn(expectedResult);

        String resultJson = mockMvc
                .perform(put("/api/v1/users/{id}", expectedResult.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(updateDTO)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        AppUserReadDTO actualResult = mapper.readValue(resultJson, AppUserReadDTO.class);

        assertEquals(expectedResult, actualResult);

        Mockito.verify(appUserService).updateUser(expectedResult.getId(), updateDTO);
    }

    @Test
    public void testBlockUser() throws Exception {
        AppUserReadDTO expectedResult = createUserReadDTO(UserRoleType.USER);

        Mockito.when(appUserService.blockUserById(expectedResult.getId()))
                .thenReturn(expectedResult);

        String resultJson = mockMvc
                .perform(post("/api/v1/users/{id}/block", expectedResult.getId()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        AppUserReadDTO actualResult = mapper.readValue(resultJson, AppUserReadDTO.class);

        assertEquals(expectedResult, actualResult);

        Mockito.verify(appUserService).blockUserById(expectedResult.getId());
    }

    @Test
    public void testUnblockUser() throws Exception {
        AppUserReadDTO expectedResult = createUserReadDTO(UserRoleType.USER);

        Mockito.when(appUserService.unblockUserById(expectedResult.getId()))
                .thenReturn(expectedResult);

        String resultJson = mockMvc
                .perform(post("/api/v1/users/{id}/unblock", expectedResult.getId()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        AppUserReadDTO actualResult = mapper.readValue(resultJson, AppUserReadDTO.class);

        assertEquals(expectedResult, actualResult);

        Mockito.verify(appUserService).unblockUserById(expectedResult.getId());
    }

    private AppUserReadDTO createUserReadDTO(UserRoleType roleType) {
        AppUserReadDTO dto = new AppUserReadDTO();
        dto.setId(UUID.randomUUID());
        dto.setFullName("Marcus Bowie");
        dto.setExternalId("2233455");
        dto.setWorkHoursNorm(9);
        dto.setAllowedOvertimeHours(1);
        dto.setAllowedPausedHours(1);
        dto.setEmail("test@mail.com");
        dto.setUserRoles(List.of(createRoleReadDTO(roleType)));

        return dto;
    }

    private UserRoleReadDTO createRoleReadDTO(UserRoleType roleType) {
        UserRoleReadDTO dto = new UserRoleReadDTO();
        dto.setId(UUID.randomUUID());
        dto.setType(roleType);
        return dto;
    }
}
