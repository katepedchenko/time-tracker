package com.example.timetracker.service;

import com.example.timetracker.domain.AppUser;
import com.example.timetracker.dto.AppUserCreateDTO;
import com.example.timetracker.dto.AppUserReadDTO;
import com.example.timetracker.dto.AppUserUpdateDTO;
import com.example.timetracker.exception.EntityNotFoundException;
import com.example.timetracker.repository.AppUserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@Sql(statements = {
        "delete from app_user"},
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class AppUserServiceTest {

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private AppUserService appUserService;

    @Test
    public void testUserById() {
        AppUser expectedResult = createUser();

        AppUserReadDTO readDTO = appUserService.getUserById(expectedResult.getId());

        Assertions.assertThat(readDTO).isEqualToComparingFieldByField(expectedResult);
    }

    @Test()
    public void testGetRegistryByIdWrongId() {
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
        AppUser user = createUser();

        AppUserUpdateDTO updateDTO = new AppUserUpdateDTO();
        updateDTO.setExternalId("new_ext_id");
        updateDTO.setFullName("new name");
        updateDTO.setIsBlocked(Boolean.TRUE);

        AppUserReadDTO readDTO = appUserService.updateUser(user.getId(), updateDTO);

        Assertions.assertThat(updateDTO).isEqualToIgnoringGivenFields(readDTO);

        AppUser updatedUser = appUserRepository.findById(user.getId()).get();
        Assertions.assertThat(readDTO).isEqualToIgnoringGivenFields(updatedUser,
                "userRoles");
    }

    @Test
    public void testBlockUser() {
        AppUser user = createUser(false);

        AppUserReadDTO readDTO = appUserService.blockUserById(user.getId());

        assertTrue(readDTO.getIsBlocked());

        AppUser blockedUser = appUserRepository.findById(user.getId()).get();
        assertTrue(blockedUser.getIsBlocked());
    }

    @Test
    public void testUnblockUser() {
        AppUser user = createUser(true);

        AppUserReadDTO readDTO = appUserService.unblockUserById(user.getId());

        assertFalse(readDTO.getIsBlocked());

        AppUser unblockedUser = appUserRepository.findById(user.getId()).get();
        assertFalse(unblockedUser.getIsBlocked());
    }

    private AppUser createUser() {
        AppUser user = new AppUser();
        user.setIsBlocked(Boolean.FALSE);
        user.setFullName("Charles Folk");
        user.setExternalId("122244");

        return appUserRepository.save(user);
    }

    private AppUser createUser(boolean isBlocked) {
        AppUser user = new AppUser();
        user.setIsBlocked(isBlocked);
        user.setFullName("Charles Folk");
        user.setExternalId("122244");

        return appUserRepository.save(user);
    }
}
