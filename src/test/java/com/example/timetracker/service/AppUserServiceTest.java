package com.example.timetracker.service;

import com.example.timetracker.domain.AppUser;
import com.example.timetracker.dto.AppUserCreateDTO;
import com.example.timetracker.dto.AppUserReadDTO;
import com.example.timetracker.dto.AppUserUpdateDTO;
import com.example.timetracker.exception.EntityNotFoundException;
import com.example.timetracker.repository.AppUserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class AppUserServiceTest extends BaseServiceTest {

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private AppUserService appUserService;

    @Test
    public void testGetAllUsers() {
        AppUser u1 = testObjectFactory.createUser("1111", "test1@mail.com");
        AppUser u2 = testObjectFactory.createUser("2222", "test2@mail.com");
        AppUser u3 = testObjectFactory.createUser("3333", "test3@mail.com");

        List<AppUserReadDTO> allUsers = appUserService.getAllUsers();

        Assertions.assertThat(allUsers).extracting("id")
                .containsExactlyInAnyOrder(u1.getId(), u2.getId(), u3.getId());
    }

    @Test
    public void testUserById() {
        AppUser expectedResult = testObjectFactory.createUser();

        AppUserReadDTO readDTO = appUserService.getUserById(expectedResult.getId());

        Assertions.assertThat(readDTO).isEqualToComparingFieldByField(expectedResult);
    }

    @Test()
    public void testGetUserByIdWrongId() {
        final UUID wrongId = UUID.randomUUID();

        Assertions.assertThatThrownBy(() -> appUserService.getUserById(wrongId))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining(String.valueOf(wrongId));
    }

    @Test
    public void testCreateUser() {
        AppUserCreateDTO createDTO = new AppUserCreateDTO();
        createDTO.setFullName("Lil Peep");
        createDTO.setExternalId("554433");

        AppUserReadDTO readDTO = appUserService.createUser(createDTO);

        Assertions.assertThat(createDTO).isEqualToIgnoringGivenFields(readDTO);
        assertNotNull(readDTO.getId());

        AppUser createdUser = appUserRepository.findById(readDTO.getId()).get();
        Assertions.assertThat(readDTO).isEqualToIgnoringGivenFields(createdUser,
                "userRoles");
    }

    @Test
    public void testUpdateUser() {
        AppUser user = testObjectFactory.createUser();

        AppUserUpdateDTO updateDTO = new AppUserUpdateDTO();
        updateDTO.setExternalId("new_ext_id");
        updateDTO.setFullName("new name");
        updateDTO.setIsBlocked(Boolean.TRUE);
        updateDTO.setWorkHoursNorm(9);
        updateDTO.setEmail("test@mail.com");
        updateDTO.setAllowedOvertimeHours(1);
        updateDTO.setAllowedPausedHours(1);

        AppUserReadDTO readDTO = appUserService.updateUser(user.getId(), updateDTO);

        Assertions.assertThat(updateDTO).isEqualToComparingFieldByField(readDTO);

        AppUser updatedUser = appUserRepository.findById(user.getId()).get();
        Assertions.assertThat(readDTO).isEqualToIgnoringGivenFields(updatedUser,
                "userRoles");
    }

    @Test
    public void testBlockUser() {
        AppUser user = testObjectFactory.createUser(false);

        AppUserReadDTO readDTO = appUserService.blockUserById(user.getId());

        assertTrue(readDTO.getIsBlocked());

        AppUser blockedUser = appUserRepository.findById(user.getId()).get();
        assertTrue(blockedUser.getIsBlocked());
    }

    @Test
    public void testUnblockUser() {
        AppUser user = testObjectFactory.createUser(true);

        AppUserReadDTO readDTO = appUserService.unblockUserById(user.getId());

        assertFalse(readDTO.getIsBlocked());

        AppUser unblockedUser = appUserRepository.findById(user.getId()).get();
        assertFalse(unblockedUser.getIsBlocked());
    }
}
