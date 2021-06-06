package com.example.timetracker.service;

import com.example.timetracker.dto.AppUserCreateDTO;
import com.example.timetracker.dto.AppUserReadDTO;
import com.example.timetracker.dto.AppUserUpdateDTO;

import java.util.List;
import java.util.UUID;

public interface AppUserService {
    AppUserReadDTO getUserById(UUID id);

    List<AppUserReadDTO> getAllUsers();

    AppUserReadDTO createUser(AppUserCreateDTO createDTO);

    AppUserReadDTO updateUser(UUID id, AppUserUpdateDTO updateDTO);

    AppUserReadDTO blockUserById(UUID id);

    AppUserReadDTO unblockUserById(UUID id);
}
