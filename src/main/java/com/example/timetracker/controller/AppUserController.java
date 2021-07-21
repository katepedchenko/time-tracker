package com.example.timetracker.controller;

import com.example.timetracker.dto.AppUserCreateDTO;
import com.example.timetracker.dto.AppUserReadDTO;
import com.example.timetracker.dto.AppUserUpdateDTO;
import com.example.timetracker.service.AppUserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
public class AppUserController {

    @Autowired
    private AppUserService appUserService;

    @ApiOperation(value = "Load user's info by id")
    @GetMapping("/{id}")
    public AppUserReadDTO getUser(@PathVariable UUID id) {
        return appUserService.getUserById(id);
    }

    @ApiOperation(value = "Load all user's infos")
    @GetMapping
    public List<AppUserReadDTO> getAllUsers() {
        return appUserService.getAllUsers();
    }

    @ApiOperation(value = "Add new user")
    @PostMapping
    public AppUserReadDTO createUser(@RequestBody AppUserCreateDTO createDTO) {
        return appUserService.createUser(createDTO);
    }

    @ApiOperation(value = "Update user info by id")
    @PutMapping("/{id}")
    public AppUserReadDTO updateUser(@PathVariable UUID id, @RequestBody AppUserUpdateDTO updateDTO) {
        return appUserService.updateUser(id, updateDTO);
    }

    @ApiOperation(value = "Block user by id")
    @PostMapping("/{id}/block")
    public AppUserReadDTO blockUser(@PathVariable UUID id) {
        return appUserService.blockUserById(id);
    }

    @ApiOperation(value = "Unblock user by id")
    @PostMapping("/{id}/unblock")
    public AppUserReadDTO unblockUser(@PathVariable UUID id) {
        return appUserService.unblockUserById(id);
    }
}
